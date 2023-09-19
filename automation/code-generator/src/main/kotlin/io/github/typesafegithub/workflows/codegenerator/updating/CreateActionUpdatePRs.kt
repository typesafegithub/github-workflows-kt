package io.github.typesafegithub.workflows.codegenerator.updating

import io.github.typesafegithub.workflows.actionsmetadata.model.ActionCoords
import io.github.typesafegithub.workflows.actionsmetadata.model.WrapperRequest
import io.github.typesafegithub.workflows.actionsmetadata.model.isTopLevel
import io.github.typesafegithub.workflows.actionsmetadata.wrappersToGenerate
import io.github.typesafegithub.workflows.codegenerator.types.provideTypes
import io.github.typesafegithub.workflows.codegenerator.versions.GithubRef
import io.github.typesafegithub.workflows.codegenerator.versions.GithubTag
import io.github.typesafegithub.workflows.codegenerator.versions.getGithubToken
import io.github.typesafegithub.workflows.codegenerator.versions.httpClient
import io.github.typesafegithub.workflows.codegenerator.versions.json
import io.github.typesafegithub.workflows.wrappergenerator.Wrapper
import io.github.typesafegithub.workflows.wrappergenerator.fetchMetadata
import io.github.typesafegithub.workflows.wrappergenerator.generateWrapper
import io.github.typesafegithub.workflows.wrappergenerator.prettyPrint
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import java.nio.file.Path
import kotlin.io.path.pathString

suspend fun main() {
    val githubToken = getGithubToken()

    wrappersToGenerate
        .filter { it.actionCoords.isTopLevel }
        .forEach { wrapperRequest ->
            println("➡\uFE0F For action ${wrapperRequest.actionCoords.prettyPrint}")

            val commitHashFilePath = wrapperRequest.actionCoords.buildCommitHashFilePath()
            val currentCommitHash = commitHashFilePath.toFile().readText().trim()

            val newestCommitHash = wrapperRequest.actionCoords.fetchCommitHash(githubToken)
                ?: error("There was a problem fetching commit hash for ${wrapperRequest.actionCoords}")

            val (codeCurrent, path) = wrapperRequest.generateWrapperForCommit(currentCommitHash)
            val (codeNewest, _) = wrapperRequest.generateWrapperForCommit(newestCommitHash)

            if (codeCurrent != codeNewest) {
                println("\uD83D\uDEA8 GENERATED CODE CHANGED! $path \uD83D\uDEA8")
                createPullRequest(
                    wrapperRequest = wrapperRequest,
                    path = path,
                    wrapperCode = codeNewest,
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
    wrapperRequest: WrapperRequest,
    path: String,
    wrapperCode: String,
    commitHashFilePath: Path,
    newCommitHash: String,
    githubToken: String,
) {
    println("Creating a PR:")
    createPullRequest(
        branchName = "update-${wrapperRequest.actionCoords.prettyPrint}",
        prTitle = "feat(actions): update ${wrapperRequest.actionCoords.prettyPrint}",
        prBody = "Created automatically.",
        fileNamesToContents = mapOf(
            path to wrapperCode,
            commitHashFilePath.pathString to newCommitHash,
        ),
        githubToken = githubToken,
        githubRepoOwner = "typesafegithub",
        githubRepoName = "github-workflows-kt",
    )
}

private fun WrapperRequest.generateWrapperForCommit(commitHash: String): Wrapper =
    actionCoords.generateWrapper(
        inputTypings = provideTypes(getCommitHash = { commitHash }),
        fetchMetadataImpl = { fetchMetadata(commitHash = commitHash, useCache = false) },
    )

private suspend fun ActionCoords.fetchCommitHash(githubToken: String): String? {
    suspend fun fetch(detailsUrl: String): String? {
        val responseJson = tryFetchingCommitHash(
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

private suspend fun tryFetchingCommitHash(owner: String, name: String, detailsUrl: String, githubToken: String): String? {
    val url = "https://api.github.com/repos/$owner/$name/git/$detailsUrl"
    return fetchGithubUrl(url, githubToken)
}

private suspend fun fetchGithubUrl(url: String, githubToken: String): String? {
    println("    trying $url")
    val response = httpClient.get(urlString = url) {
        bearerAuth(githubToken)
    }
    if (response.status == HttpStatusCode.NotFound) {
        return null
    }
    return response.bodyAsText()
}

private fun ActionCoords.buildCommitHashFilePath(): Path =
    Path.of("actions").resolve(owner).resolve(name).resolve(version).resolve("commit-hash.txt")
