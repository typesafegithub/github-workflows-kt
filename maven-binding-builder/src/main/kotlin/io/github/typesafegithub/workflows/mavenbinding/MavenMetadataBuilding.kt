package io.github.typesafegithub.workflows.mavenbinding

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import kotlinx.serialization.Serializable
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

internal suspend fun buildMavenMetadataFile(
    owner: String,
    name: String,
    githubToken: String,
    httpClient: HttpClient,
): String {
    val lastUpdated =
        DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
            .withZone(ZoneId.systemDefault())
            .format(Instant.now())
    val availableMajorVersions =
        fetchAvailableVersions(owner = owner, name = name.substringBefore("__"), githubToken = githubToken, httpClient = httpClient)
            .filter { it.removePrefix("v").toIntOrNull() != null }
    val newest = availableMajorVersions.maxBy { it.removePrefix("v") }
    return """
        <?xml version="1.0" encoding="UTF-8"?>
        <metadata>
          <groupId>$owner</groupId>
          <artifactId>${name.replace("__", "/")}</artifactId>
          <versioning>
            <latest>$newest</latest>
            <release>$newest</release>
            <versions>
${availableMajorVersions.joinToString(separator = "\n") {
        "              <version>$it</version>"
    }}
            </versions>
            <lastUpdated>$lastUpdated</lastUpdated>
          </versioning>
        </metadata>
        """.trimIndent()
}

private suspend fun fetchAvailableVersions(
    owner: String,
    name: String,
    githubToken: String,
    httpClient: HttpClient,
): List<String> =
    listOf(
        apiTagsUrl(owner = owner, name = name),
        apiBranchesUrl(owner = owner, name = name),
    ).flatMap { url -> fetchGithubRefs(url, githubToken, httpClient) }
        .map { it.ref.substringAfterLast("/") }

private suspend fun fetchGithubRefs(
    url: String,
    githubToken: String,
    httpClient: HttpClient,
): List<GithubRef> =
    httpClient.get(urlString = url) {
        bearerAuth(githubToken)
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
data class GithubRef(
    val ref: String,
)
