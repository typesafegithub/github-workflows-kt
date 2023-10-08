package io.github.typesafegithub.workflows.codegenerator.versions

import io.github.typesafegithub.workflows.actionsmetadata.model.ActionCoords
import io.github.typesafegithub.workflows.actionsmetadata.model.Version
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

val ActionCoords.apiTagsUrl: String
    get() = "https://api.github.com/repos/$owner/$name/git/matching-refs/tags/v"

val ActionCoords.apiBranchesUrl: String
    get() = "https://api.github.com/repos/$owner/$name/git/matching-refs/heads/v"

fun getGithubToken(): String =
    System.getenv("GITHUB_TOKEN")
        ?: error(
            """
            Missing environment variable export GITHUB_TOKEN=token
            Create a personal token at https://github.com/settings/tokens
            The token needs to have public_repo scope.
            """.trimIndent(),
        )

@Serializable
data class GithubRef(
    val ref: String,
    @SerialName("object")
    val obj: GithubRefObject?,
)

@Serializable
data class GithubRefObject(
    val sha: String,
    val type: String,
    val url: String,
)

@Serializable
data class GithubTag(
    @SerialName("object")
    val obj: GithubTagObject?,
)

@Serializable
data class GithubTagObject(
    val sha: String,
    val type: String,
    val url: String,
)

val json = Json { ignoreUnknownKeys = true }

val httpClient by lazy {
    HttpClient {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                },
            )
        }
    }
}

suspend fun ActionCoords.fetchAvailableVersions(githubToken: String): List<Version> =
    listOf(apiTagsUrl, apiBranchesUrl)
        .flatMap { url -> fetchGithubRefs(url, githubToken) }
        .versions()

private suspend fun fetchGithubRefs(
    url: String,
    githubToken: String,
): List<GithubRef> =
    httpClient.get(urlString = url) {
        bearerAuth(githubToken)
    }.body()
