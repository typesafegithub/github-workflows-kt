package it.krzeminski.githubactions.wrappergenerator.updating

import it.krzeminski.githubactions.wrappergenerator.domain.ActionCoords
import it.krzeminski.githubactions.wrappergenerator.versions.*
import it.krzeminski.githubactions.wrappergenerator.wrappersToGenerate
import kotlinx.serialization.decodeFromString
import okhttp3.Request
import java.nio.file.Path

fun main() {
    val githubToken = getGithubToken()

    wrappersToGenerate.forEach { wrapperRequest ->
        println("For action ${wrapperRequest.actionCoords}")
        val commitHash = wrapperRequest.actionCoords.fetchCommitHash(githubToken)
            ?: error("There was a problem fetching commit hash for ${wrapperRequest.actionCoords}")
        println("  $commitHash")

        val commitHashFilePath = wrapperRequest.actionCoords.buildCommitHashFilePath()

        commitHashFilePath.toFile().apply {
            deleteRecursively()
            writeText(commitHash)
        }
    }
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
