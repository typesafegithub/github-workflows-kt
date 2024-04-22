package io.github.typesafegithub.workflows.shared.internal

import io.github.typesafegithub.workflows.shared.internal.model.Version
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.expectSuccess
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

suspend fun fetchAvailableVersions(
    owner: String,
    name: String,
    githubToken: String?,
): List<Version> =
    listOf(
        apiTagsUrl(owner = owner, name = name),
        apiBranchesUrl(owner = owner, name = name),
    ).flatMap { url -> fetchGithubRefs(url, githubToken) }
        .versions()

private fun List<GithubRef>.versions(): List<Version> =
    this.map { githubRef ->
        val version = githubRef.ref.substringAfterLast("/")
        Version(version)
    }

private suspend fun fetchGithubRefs(
    url: String,
    githubToken: String?,
): List<GithubRef> =
    httpClient.get(urlString = url) {
        expectSuccess = true
        if (githubToken != null) {
            bearerAuth(githubToken)
        }
    }.body()

private fun repoName(name: String): String = name.substringBefore('/')

private fun apiTagsUrl(
    owner: String,
    name: String,
): String = "https://api.github.com/repos/$owner/${repoName(name)}/git/matching-refs/tags/v"

private fun apiBranchesUrl(
    owner: String,
    name: String,
): String = "https://api.github.com/repos/$owner/${repoName(name)}/git/matching-refs/heads/v"

@Serializable
private data class GithubRef(
    val ref: String,
)

private val httpClient by lazy {
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
