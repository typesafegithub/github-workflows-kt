package io.github.typesafegithub.workflows.shared.internal

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.github.oshai.kotlinlogging.KotlinLogging.logger
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel.ALL
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
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

private val logger = logger { }

/**
 * Returns an installation access token for the GitHub app, usable with API call to GitHub.
 * If `null` is returned, it means that the environment wasn't configured to generate the token.
 */
suspend fun getInstallationAccessToken(): String? {
    if (cachedAccessToken?.isExpired() == false) return cachedAccessToken!!.token
    val jwtToken = generateJWTToken() ?: return null
    cachedAccessToken =
        httpClient
            .post("https://api.github.com/app/installations/$INSTALLATION_ID/access_tokens") {
                header("Accept", "application/vnd.github+json")
                header("Authorization", "Bearer $jwtToken")
                header("X-GitHub-Api-Version", "2022-11-28")
            }.body()
    return cachedAccessToken!!.token
}

private const val INSTALLATION_ID = "62885502"

private var cachedAccessToken: Token? = null

@Serializable
private data class Token(
    val token: String,
    val expires_at: String,
    val permissions: Map<String, String>,
    val repository_selection: String,
) {
    @Transient
    private val expiresAtDateTime = ZonedDateTime.parse(expires_at)

    fun isExpired() = ZonedDateTime.now().isAfter(expiresAtDateTime)
}

private val httpClient =
    HttpClient {
        val klogger = logger
        install(Logging) {
            logger =
                object : Logger {
                    override fun log(message: String) {
                        klogger.trace { message }
                    }
                }
            level = ALL
        }
        install(ContentNegotiation) {
            json(
                Json { ignoreUnknownKeys = false },
            )
        }
    }

private const val GITHUB_CLIENT_ID = "Iv23liIZ17VJKUpjacBs"

private fun generateJWTToken(): String? {
    val key = loadRsaKey() ?: return null
    val algorithm = Algorithm.RSA256(null, key)
    val now = Instant.now()
    return JWT
        .create()
        .withIssuer(GITHUB_CLIENT_ID)
        .withIssuedAt(now.minusMinutes(1))
        .withExpiresAt(now.plusMinutes(9))
        .sign(algorithm)
}

private fun loadRsaKey(): RSAPrivateKey? {
    val privateKey = System.getenv("APP_PRIVATE_KEY") ?: return null
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
