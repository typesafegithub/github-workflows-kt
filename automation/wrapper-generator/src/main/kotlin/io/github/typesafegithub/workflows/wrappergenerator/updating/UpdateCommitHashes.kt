package io.github.typesafegithub.workflows.wrappergenerator.updating

import io.github.typesafegithub.workflows.actionsmetadata.model.ActionCoords
import io.github.typesafegithub.workflows.actionsmetadata.model.WrapperRequest
import io.github.typesafegithub.workflows.actionsmetadata.model.isTopLevel
import io.github.typesafegithub.workflows.actionsmetadata.wrappersToGenerate
import io.github.typesafegithub.workflows.wrappergenerator.metadata.prettyPrint
import io.github.typesafegithub.workflows.wrappergenerator.versions.GithubRef
import io.github.typesafegithub.workflows.wrappergenerator.versions.GithubTag
import io.github.typesafegithub.workflows.wrappergenerator.versions.getGithubToken
import io.github.typesafegithub.workflows.wrappergenerator.versions.json
import io.github.typesafegithub.workflows.wrappergenerator.versions.okhttpClient
import kotlinx.serialization.decodeFromString
import okhttp3.Request
import java.nio.file.Path

fun main() {
    val githubToken = getGithubToken()

    wrappersToGenerate
        .filter { it.actionCoords.isTopLevel }
        .forEach { wrapperRequest ->
            println("For action ${wrapperRequest.actionCoords.prettyPrint}")

            if (wrapperRequest.didCommitHashChange(githubToken = githubToken)) {
                print("  it DID change, checking further")
            } else {
                print("  it didn't change, skipping")
            }


//            commitHashFilePath.toFile().apply {
//                deleteRecursively()
//                writeText(newestCommitHash)
//            }
        }
}

private fun WrapperRequest.didCommitHashChange(githubToken: String): Boolean {
    println("  checking if commit hash changed:")

    val commitHashFilePath = this.actionCoords.buildCommitHashFilePath()
    val currentCommitHash = commitHashFilePath.toFile().readText().trim()
    print("    current: $currentCommitHash")

    val newestCommitHash = this.actionCoords.fetchCommitHash(githubToken)
        ?: error("There was a problem fetching commit hash for ${this.actionCoords}")
    println("    newest: $newestCommitHash")

    return currentCommitHash != newestCommitHash
}

private fun ActionCoords.fetchCommitHash(githubToken: String): String? {
    fun fetch(detailsUrl: String): String? {
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

private fun tryFetchingCommitHash(owner: String, name: String, detailsUrl: String, githubToken: String): String? {
    val url = "https://api.github.com/repos/$owner/$name/git/$detailsUrl"
    return fetchGithubUrl(url, githubToken)
}

private fun fetchGithubUrl(url: String, githubToken: String): String? {
    println("    trying $url")
    val request: Request = Request.Builder()
        .header("Authorization", "token $githubToken")
        .url(url)
        .build()

    return okhttpClient.newCall(request).execute().use { response ->
        if (response.isSuccessful.not()) {
            if (response.code == 404) {
                return null
            } else {
                print("HTTP code was ${response.code}")
                error("There was an error making request!")
            }
        }
        response.body!!.string()
    }
}

private fun ActionCoords.buildCommitHashFilePath(): Path =
    Path.of("actions").resolve(owner).resolve(name).resolve(version).resolve("commit-hash.txt")
