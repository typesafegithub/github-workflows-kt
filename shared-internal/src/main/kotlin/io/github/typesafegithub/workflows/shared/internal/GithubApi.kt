package io.github.typesafegithub.workflows.shared.internal

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
import io.github.oshai.kotlinlogging.KotlinLogging.logger
import io.github.typesafegithub.workflows.shared.internal.model.Version
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel.ALL
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.time.ZonedDateTime

private val logger = logger { }

suspend fun fetchAvailableVersions(
    owner: String,
    name: String,
    githubAuthToken: String?,
    githubEndpoint: String = "https://api.github.com",
): Either<String, List<Version>> =
    either {
        buildHttpClient().use { httpClient ->
            return listOf(
                apiTagsUrl(githubEndpoint = githubEndpoint, owner = owner, name = name),
                apiBranchesUrl(githubEndpoint = githubEndpoint, owner = owner, name = name),
            ).flatMap { url -> fetchGithubRefs(url, githubAuthToken, httpClient).bind() }
                .versions(githubAuthToken)
        }
    }

private fun List<GithubRef>.versions(githubAuthToken: String?): Either<String, List<Version>> =
    either {
        this@versions.map { githubRef ->
            val version = githubRef.ref.substringAfterLast("/")
            Version(version) {
                val response =
                    buildHttpClient().use { httpClient ->
                        httpClient
                            .get(urlString = githubRef.`object`.url) {
                                if (githubAuthToken != null) {
                                    bearerAuth(githubAuthToken)
                                }
                            }
                    }
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

private fun apiTagsUrl(
    githubEndpoint: String,
    owner: String,
    name: String,
): String = "$githubEndpoint/repos/$owner/$name/git/matching-refs/tags/v"

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
    val url: String,
)

@Serializable
private data class Tag(
    val tagger: Person,
)

@Serializable
private data class Commit(
    val author: Person,
)

@Serializable
private data class Person(
    val date: String,
)

private fun buildHttpClient() =
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
    }
