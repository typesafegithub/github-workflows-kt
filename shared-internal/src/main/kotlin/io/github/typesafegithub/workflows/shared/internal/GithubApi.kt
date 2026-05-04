package io.github.typesafegithub.workflows.shared.internal

import arrow.core.Either
import arrow.core.left
import arrow.core.raise.either
import arrow.core.raise.ensure
import arrow.core.right
import io.github.oshai.kotlinlogging.KotlinLogging.logger
import io.github.typesafegithub.workflows.shared.internal.model.Version
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel.ALL
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.plugin
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import io.micrometer.core.instrument.MeterRegistry
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.time.ZonedDateTime

private val logger = logger { }

private const val MAX_REF_PARTS = 3

private const val GITHUB_ENDPOINT = "https://api.github.com"

suspend fun fetchVersionSha(
    repo: String,
    version: String,
    githubAuthToken: String?,
    meterRegistry: MeterRegistry? = null,
    githubEndpoint: String = GITHUB_ENDPOINT,
): Either<String, String> =
    either {
        buildHttpClient(meterRegistry = meterRegistry).use { httpClient ->
            return listOf(
                apiTagUrl(githubEndpoint = githubEndpoint, repo = repo, version = version),
                apiBranchUrl(githubEndpoint = githubEndpoint, repo = repo, version = version),
            ).flatMap { url -> fetchGithubRefs(url, githubAuthToken, httpClient).bind() }
                .firstOrNull {
                    val refParts = it.ref.split('/', limit = 4)
                    (refParts.size == MAX_REF_PARTS) && (refParts[2] == version)
                }?.`object`
                ?.let {
                    it.getCommitSha(
                        httpClient
                            .get(urlString = it.url) {
                                if (githubAuthToken != null) {
                                    bearerAuth(githubAuthToken)
                                }
                            },
                    )
                }?.right()
                ?: "Specified version $version not found".left()
        }
    }

suspend fun fetchAvailableVersions(
    owner: String,
    name: String,
    githubAuthToken: String?,
    meterRegistry: MeterRegistry? = null,
    githubEndpoint: String = GITHUB_ENDPOINT,
): Either<String, List<Version>> =
    either {
        buildHttpClient(meterRegistry = meterRegistry).use { httpClient ->
            return listOf(
                apiTagsUrl(githubEndpoint = githubEndpoint, owner = owner, name = name),
                apiBranchesUrl(githubEndpoint = githubEndpoint, owner = owner, name = name),
            ).flatMap { url -> fetchGithubRefs(url, githubAuthToken, httpClient).bind() }
                .filter { it.ref.split('/', limit = MAX_REF_PARTS + 1).size == MAX_REF_PARTS }
                .versions(githubAuthToken, meterRegistry = meterRegistry)
        }
    }

private fun List<GithubRef>.versions(
    githubAuthToken: String?,
    meterRegistry: MeterRegistry?,
): Either<String, List<Version>> =
    either {
        this@versions.map { githubRef ->
            val version = githubRef.ref.substringAfterLast("/")
            val objectProvider: suspend () -> HttpResponse = {
                buildHttpClient(meterRegistry = meterRegistry).use { httpClient ->
                    httpClient
                        .get(urlString = githubRef.`object`.url) {
                            if (githubAuthToken != null) {
                                bearerAuth(githubAuthToken)
                            }
                        }
                }
            }
            Version(
                version,
                shaProvider = { githubRef.`object`.getCommitSha(objectProvider()) },
            ) {
                val response = objectProvider()
                val releaseDate =
                    when (githubRef.`object`.type) {
                        "tag" -> response.body<Tag>().tagger
                        "commit" -> response.body<Commit>().author
                        else -> error("Unexpected target object type ${githubRef.`object`.type}")
                    }.date
                ZonedDateTime.parse(releaseDate)
            }
        }
    }

private suspend fun Object.getCommitSha(httpResponse: HttpResponse) =
    when (type) {
        "tag" -> httpResponse.body<Tag>().`object`.sha
        "commit" -> httpResponse.body<Commit>().sha
        else -> error("Unexpected target object type $type")
    }

private suspend fun fetchGithubRefs(
    url: String,
    githubAuthToken: String?,
    httpClient: HttpClient,
): Either<String, List<GithubRef>> =
    either {
        val response =
            httpClient
                .get(urlString = url) {
                    if (githubAuthToken != null) {
                        bearerAuth(githubAuthToken)
                    }
                }
        ensure(response.status.isSuccess()) {
            "Unexpected response when fetching refs from $url. " +
                "Status: ${response.status}, response: ${response.bodyAsText()}"
        }
        response.body()
    }

private fun apiTagUrl(
    githubEndpoint: String,
    repo: String,
    version: String,
): String = "$githubEndpoint/repos/$repo/git/matching-refs/tags/$version"

private fun apiTagsUrl(
    githubEndpoint: String,
    owner: String,
    name: String,
): String = "$githubEndpoint/repos/$owner/$name/git/matching-refs/tags/v"

private fun apiBranchUrl(
    githubEndpoint: String,
    repo: String,
    version: String,
): String = "$githubEndpoint/repos/$repo/git/matching-refs/heads/$version"

private fun apiBranchesUrl(
    githubEndpoint: String,
    owner: String,
    name: String,
): String = "$githubEndpoint/repos/$owner/$name/git/matching-refs/heads/v"

@Serializable
private data class GithubRef(
    val ref: String,
    val `object`: Object,
)

@Serializable
private data class Object(
    val type: String,
    val sha: String,
    val url: String,
)

@Serializable
private data class Tag(
    val tagger: Person,
    val `object`: Object,
)

@Serializable
private data class Commit(
    val author: Person,
    val sha: String,
)

@Serializable
private data class Person(
    val date: String,
)

private fun buildHttpClient(meterRegistry: MeterRegistry?) =
    HttpClient {
        val klogger = logger
        install(Logging) {
            logger =
                object : Logger {
                    override fun log(message: String) {
                        klogger.trace { message }
                    }
                }
            level = ALL
        }
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                },
            )
        }
    }.apply {
        if (meterRegistry != null) {
            plugin(HttpSend).intercept { request ->
                if (request.url.host == "api.github.com") {
                    val counter =
                        meterRegistry.counter(
                            "calls_to_github",
                            listOf(
                                io.micrometer.core.instrument.Tag
                                    .of("type", "api"),
                            ),
                        )
                    counter.increment()
                }
                execute(request)
            }
        }
    }
