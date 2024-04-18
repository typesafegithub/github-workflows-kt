package io.github.typesafegithub.workflows.internal

import io.github.typesafegithub.workflows.domain.actions.RegularAction
import io.github.typesafegithub.workflows.internal.model.Version
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

internal val RegularAction<*>.apiTagsUrl: String
    get() = "https://api.github.com/repos/$actionOwner/$actionName/git/matching-refs/tags/v"

internal val RegularAction<*>.apiBranchesUrl: String
    get() = "https://api.github.com/repos/$actionOwner/$actionName/git/matching-refs/heads/v"

internal fun getGithubToken(): String =
    getGithubTokenOrNull()
        ?: error(
            """
            Missing environment variable export GITHUB_TOKEN=token
            Create a personal token at https://github.com/settings/tokens
            The token needs to have public_repo scope.
            """.trimIndent(),
        )

internal fun getGithubTokenOrNull(): String? = System.getenv("GITHUB_TOKEN")

@Serializable
public data class GithubRef(
    val ref: String,
    @SerialName("object")
    val obj: GithubRefObject?,
)

@Serializable
public data class GithubRefObject(
    val sha: String,
    val type: String,
    val url: String,
)

internal val json = Json { ignoreUnknownKeys = true }

internal val httpClient by lazy {
    HttpClient {
        install(ContentNegotiation) {
            json(json)
        }
    }
}

public fun List<GithubRef>.versions(): List<Version> =
    this.map { githubRef ->
        val version = githubRef.ref.substringAfterLast("/")
        Version(version)
    }

internal suspend fun RegularAction<*>.fetchAvailableVersions(
    githubToken: String?,
    branches: Boolean = false,
): List<GithubRef> =
    listOfNotNull(
        apiTagsUrl,
        apiBranchesUrl.takeIf { branches },
    )
        .flatMap { url -> fetchGithubRefs(url, githubToken) }

internal suspend fun fetchGithubRefs(
    url: String,
    githubToken: String?,
): List<GithubRef> =
    httpClient.get(urlString = url) {
        githubToken?.let { token ->
            bearerAuth(token)
        }
    }.body()
