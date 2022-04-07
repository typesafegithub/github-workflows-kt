package it.krzeminski.githubactions.wrappergenerator.versions

import it.krzeminski.githubactions.wrappergenerator.domain.ActionCoords
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request

val ActionCoords.apiTagsUrl: String
    get() = "https://api.github.com/repos/$owner/$name/git/matching-refs/tags/v"

@kotlinx.serialization.Serializable
data class GithubTag(
    val ref: String,
)

val json = Json { ignoreUnknownKeys = true }

val okhttpClient by lazy {
    OkHttpClient()
}

fun ActionCoords.fetchAvailableVersions(GITHUB_TOKEN: String): List<String> {
    val request: Request = Request.Builder()
        .header("Authorization", "token $GITHUB_TOKEN")
        .url(apiTagsUrl)
        .build()

    val content = okhttpClient.newCall(request).execute().use { response ->
        if (response.isSuccessful.not()) {
            println(response.headers)
            error("API rate reached?  See https://docs.github.com/en/rest/overview/resources-in-the-rest-api#rate-limiting")
        }
        response.body!!.string()
    }
    val data = json.decodeFromString(ListSerializer(GithubTag.serializer()), content)
    return data.versions()
}
