package io.github.typesafegithub.workflows.shared.internal.app

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.Json
import java.time.ZonedDateTime

class GitHubApp(
    private val installationId: String = "62885502",
) {
    val httpClient =
        HttpClient {
            install(ContentNegotiation) {
                json(
                    Json { ignoreUnknownKeys = false },
                )
            }
        }

    var token: Token? = null

    suspend fun accessToken(): String {
        if (token?.isExpired() == false) return token!!.token
        token =
            httpClient
                .post("https://api.github.com/app/installations/$installationId/access_tokens") {
                    header("Accept", "application/vnd.github+json")
                    header("Authorization", "Bearer ${generateJWT()}")
                    header("X-GitHub-Api-Version", "2022-11-28")
                }.body()
        return token!!.token
    }

    private fun generateJWT(): String = JwtGenerator().generateJWT()

    @Serializable
    data class Token(
        val token: String,
        val expires_at: String,
        val permissions: Map<String, String>,
        val repository_selection: String,
    ) {
        @Transient
        private val expiresAtDate = ZonedDateTime.parse(expires_at)

        fun isExpired() = expiresAtDate.isBefore(ZonedDateTime.now())
    }
}
