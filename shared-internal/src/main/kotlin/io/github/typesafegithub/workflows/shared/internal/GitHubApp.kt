package io.github.typesafegithub.workflows.shared.internal

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.Json
import java.security.KeyFactory
import java.security.interfaces.RSAPrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import java.time.Instant
import java.time.ZonedDateTime
import java.util.Base64

private var cachedAccessToken: Token? = null

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

private val httpClient =
    HttpClient {
        install(ContentNegotiation) {
            json(
                Json { ignoreUnknownKeys = false },
            )
        }
    }

suspend fun getInstallationAccessToken(installationId: String = "62885502"): String {
    if (cachedAccessToken?.isExpired() == false) return cachedAccessToken!!.token
    cachedAccessToken =
        httpClient
            .post("https://api.github.com/app/installations/$installationId/access_tokens") {
                header("Accept", "application/vnd.github+json")
                header("Authorization", "Bearer ${generateJWTToken()}")
                header("X-GitHub-Api-Version", "2022-11-28")
            }.body()
    return cachedAccessToken!!.token
}

private fun generateJWTToken(githubClientId: String = "Iv23liIZ17VJKUpjacBs"): String {
    val key = loadRsaKey()
    val algorithm = Algorithm.RSA256(null, key)
    val now = Instant.now()
    return JWT
        .create()
        .withIssuer(githubClientId)
        .withIssuedAt(now.minusMinutes(1))
        .withExpiresAt(now.plusMinutes(9))
        .sign(algorithm)
}

private fun loadRsaKey(privateKey: String = System.getenv("APP_PRIVATE_KEY")): RSAPrivateKey {
    val filtered =
        privateKey
            .replace("-----BEGIN PRIVATE KEY-----", "")
            .replace("-----END PRIVATE KEY-----", "")
            .replace("\\s".toRegex(), "")
    val keyBytes = Base64.getDecoder().decode(filtered)
    val keySpec = PKCS8EncodedKeySpec(keyBytes)
    val keyFactory = KeyFactory.getInstance("RSA")
    return keyFactory.generatePrivate(keySpec) as RSAPrivateKey
}

private fun Instant.minusMinutes(minutes: Long): Instant = minusSeconds(minutes * 60)

private fun Instant.plusMinutes(minutes: Long): Instant = plusSeconds(minutes * 60)
