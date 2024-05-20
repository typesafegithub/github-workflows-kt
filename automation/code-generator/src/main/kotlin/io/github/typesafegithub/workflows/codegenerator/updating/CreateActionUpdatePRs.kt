package io.github.typesafegithub.workflows.codegenerator.updating

import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.CommitHash
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.isTopLevel
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.prettyPrint
import io.github.typesafegithub.workflows.actionbindinggenerator.generation.ActionBinding
import io.github.typesafegithub.workflows.actionbindinggenerator.generation.generateBinding
import io.github.typesafegithub.workflows.codegenerator.bindingsToGenerate
import io.github.typesafegithub.workflows.codegenerator.model.ActionBindingRequest
import io.github.typesafegithub.workflows.shared.internal.getGithubToken
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.nio.file.Path
import java.time.Instant
import kotlin.io.path.Path
import kotlin.io.path.pathString
import kotlin.io.path.readText

suspend fun main() {
    val githubToken = getGithubToken()

    bindingsToGenerate
        .filter { it.actionCoords.isTopLevel }
        .forEach { actionBindingRequest ->
            println("➡\uFE0F For action ${actionBindingRequest.actionCoords.prettyPrint}")

            val commitHashFilePath = actionBindingRequest.actionCoords.buildCommitHashFilePath()
            val currentCommitHash = commitHashFilePath.toFile().readText().trim()

            val newestCommitHash =
                actionBindingRequest.actionCoords.fetchCommitHash(githubToken)
                    ?: error("There was a problem fetching commit hash for ${actionBindingRequest.actionCoords}")

            val (_, path) = actionBindingRequest.generateBindingForCommit(currentCommitHash)
            val codeCurrent = Path(path).readText()
            val (codeNewest, _) = actionBindingRequest.generateBindingForCommit(newestCommitHash)

            if (codeCurrent != codeNewest) {
                println("\uD83D\uDEA8 GENERATED CODE CHANGED! $path \uD83D\uDEA8")
                createPullRequest(
                    actionBindingRequest = actionBindingRequest,
                    path = path,
                    bindingCode = codeNewest,
                    commitHashFilePath = commitHashFilePath,
                    newCommitHash = newestCommitHash,
                    githubToken = githubToken,
                )
            } else {
                println("Generated code is the same, doing nothing.")
            }
        }
}

private suspend fun createPullRequest(
    actionBindingRequest: ActionBindingRequest,
    path: String,
    bindingCode: String,
    commitHashFilePath: Path,
    newCommitHash: String,
    githubToken: String,
) {
    println("Creating a PR:")
    createPullRequest(
        branchName = "update-${actionBindingRequest.actionCoords.prettyPrint}-${Instant.now().toEpochMilli()}",
        prTitle = "feat(actions): update ${actionBindingRequest.actionCoords.prettyPrint}",
        prBody = "Created automatically.",
        fileNamesToContents =
            mapOf(
                path to bindingCode,
                commitHashFilePath.pathString to newCommitHash,
            ),
        githubToken = githubToken,
        githubRepoOwner = "typesafegithub",
        githubRepoName = "github-workflows-kt",
    )
}

private suspend fun ActionBindingRequest.generateBindingForCommit(commitHash: String): ActionBinding =
    actionCoords.generateBinding(
        metadataRevision = CommitHash(commitHash),
    ) ?: error("Couldn't generate binding for ${this.actionCoords}")

private suspend fun ActionCoords.fetchCommitHash(githubToken: String): String? {
    suspend fun fetch(detailsUrl: String): String? {
        val responseJson =
            tryFetchingCommitHash(
                owner = this.owner,
                name = this.name,
                detailsUrl = detailsUrl,
                githubToken = githubToken,
            )
        return responseJson?.let {
            val response = json.decodeFromString<GithubRef>(it)

            when (response.obj?.type) {
                "commit" -> response.obj.sha
                "tag" -> {
                    val responseJson2 = fetchGithubUrl(url = response.obj.url, githubToken = githubToken)
                    val response2 = json.decodeFromString<GithubTag>(responseJson2 ?: error("URL given by GitHub doesn't work!"))

                    when (response2.obj?.type) {
                        "commit" -> response2.obj.sha
                        "tag" -> {
                            val responseJson3 = fetchGithubUrl(url = response2.obj.url, githubToken = githubToken)
                            val response3 = json.decodeFromString<GithubTag>(responseJson3 ?: error("URL given by GitHub doesn't work!"))

                            response3.obj?.sha
                        }
                        else -> error("Unsupported reference type: '${response2.obj?.type}'")
                    }
                }
                else -> error("Unsupported reference type: '${response.obj?.type}'")
            }
        }
    }
    return fetch("ref/tags/$version")
        ?: fetch("ref/tags/${version.removePrefix("v")}")
        ?: fetch("ref/heads/$version")
        ?: fetch("ref/heads/${version.removePrefix("v")}")
}

private suspend fun tryFetchingCommitHash(
    owner: String,
    name: String,
    detailsUrl: String,
    githubToken: String,
): String? {
    val url = "https://api.github.com/repos/$owner/$name/git/$detailsUrl"
    return fetchGithubUrl(url, githubToken)
}

private suspend fun fetchGithubUrl(
    url: String,
    githubToken: String,
): String? {
    println("    trying $url")
    val response =
        httpClient.get(urlString = url) {
            bearerAuth(githubToken)
        }
    if (response.status == HttpStatusCode.NotFound) {
        return null
    }
    return response.bodyAsText()
}

@Serializable
private data class GithubRef(
    val ref: String,
    @SerialName("object")
    val obj: GithubRefObject?,
)

@Serializable
private data class GithubRefObject(
    val sha: String,
    val type: String,
    val url: String,
)

@Serializable
private data class GithubTag(
    @SerialName("object")
    val obj: GithubTagObject?,
)

@Serializable
private data class GithubTagObject(
    val sha: String,
    val type: String,
    val url: String,
)

private fun ActionCoords.buildCommitHashFilePath(): Path =
    Path.of("actions").resolve(owner).resolve(name).resolve(version).resolve("commit-hash.txt")

internal val httpClient by lazy {
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

private val json = Json { ignoreUnknownKeys = true }
