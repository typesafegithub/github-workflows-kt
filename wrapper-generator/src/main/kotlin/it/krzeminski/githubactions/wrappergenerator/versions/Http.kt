package it.krzeminski.githubactions.wrappergenerator.versions

import it.krzeminski.githubactions.wrappergenerator.domain.ActionCoords
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request

val ActionCoords.apiTagsUrl: String
    get() = "https://api.github.com/repos/$owner/$name/git/matching-refs/tags/v"

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

val okhttpClient by lazy {
    OkHttpClient()
}

fun ActionCoords.fetchAvailableVersions(githubToken: String): List<Version> {
    val request: Request = Request.Builder()
        .header("Authorization", "token $githubToken")
        .url(apiTagsUrl)
        .build()

    val content = okhttpClient.newCall(request).execute().use { response ->
        if (response.isSuccessful.not()) {
            println(response.headers)
            error("API rate reached?  See https://docs.github.com/en/rest/overview/resources-in-the-rest-api#rate-limiting")
        }
        response.body!!.string()
    }
    val data = json.decodeFromString<List<GithubRef>>(content)
    return data.versions()
}
