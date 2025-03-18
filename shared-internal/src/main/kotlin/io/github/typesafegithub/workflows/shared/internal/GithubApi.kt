package io.github.typesafegithub.workflows.shared.internal

import io.github.typesafegithub.workflows.shared.internal.model.Version
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.time.ZonedDateTime

suspend fun fetchAvailableVersions(
    owner: String,
    name: String,
    githubToken: String?,
    httpClientEngine: HttpClientEngine = CIO.create(),
): List<Version> {
    val httpClient = buildHttpClient(engine = httpClientEngine)
    return listOf(
        apiTagsUrl(owner = owner, name = name),
        apiBranchesUrl(owner = owner, name = name),
    ).flatMap { url -> fetchGithubRefs(url, githubToken, httpClient) }
        .versions(githubToken, httpClient)
}

private fun List<GithubRef>.versions(
    githubToken: String?,
    httpClient: HttpClient,
): List<Version> =
    this.map { githubRef ->
        val version = githubRef.ref.substringAfterLast("/")
        Version(version) {
            val response =
                httpClient
                    .get(urlString = githubRef.`object`.url) {
                        if (githubToken != null) {
                            bearerAuth(githubToken)
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

private suspend fun fetchGithubRefs(
    url: String,
    githubToken: String?,
    httpClient: HttpClient,
): List<GithubRef> =
    httpClient
        .get(urlString = url) {
            if (githubToken != null) {
                bearerAuth(githubToken)
            }
        }.body()

private fun apiTagsUrl(
    owner: String,
    name: String,
): String = "https://api.github.com/repos/$owner/$name/git/matching-refs/tags/v"

private fun apiBranchesUrl(
    owner: String,
    name: String,
): String = "https://api.github.com/repos/$owner/$name/git/matching-refs/heads/v"

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

private fun buildHttpClient(engine: HttpClientEngine) =
    HttpClient(engine) {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                },
            )
        }
    }
