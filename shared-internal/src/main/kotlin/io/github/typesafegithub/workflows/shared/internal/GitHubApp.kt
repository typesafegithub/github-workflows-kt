package io.github.typesafegithub.workflows.shared.internal

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.github.oshai.kotlinlogging.KotlinLogging.logger
import org.kohsuke.github.GHAppInstallationToken
import org.kohsuke.github.GitHubBuilder
import java.security.KeyFactory
import java.security.interfaces.RSAPrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import java.time.Instant
import java.util.Base64

private val logger = logger { }

private var cachedAccessToken: GHAppInstallationToken? = null

/**
 * Returns an installation access token for the GitHub app, usable with API call to GitHub.
 * If `null` is returned, it means that the environment wasn't configured to generate the token.
 */
@Suppress("ReturnCount")
fun getInstallationAccessToken(): String? {
    if (cachedAccessToken?.isExpired() == false) return cachedAccessToken!!.token
    val jwtToken = generateJWTToken() ?: return null
    val installationId = System.getenv("APP_INSTALLATION_ID") ?: return null

    val gitHubApp = GitHubBuilder().withJwtToken(jwtToken).build().app

    cachedAccessToken =
        gitHubApp.getInstallationById(installationId.toLong()).createToken().create().also {
            logger.info { "Fetched new GitHub App installation access token for repository ${it.repositorySelection}" }
        }
    return cachedAccessToken!!.token
}

private fun GHAppInstallationToken.isExpired() = Instant.now() >= expiresAt.toInstant()

@Suppress("ReturnCount", "MagicNumber")
private fun generateJWTToken(): String? {
    val key = loadRsaKey() ?: return null
    val appClientId = System.getenv("APP_CLIENT_ID") ?: return null
    val algorithm = Algorithm.RSA256(null, key)
    val now = Instant.now()
    return JWT
        .create()
        .withIssuer(appClientId)
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

@Suppress("MagicNumber")
private fun Instant.minusMinutes(minutes: Long): Instant = minusSeconds(minutes * 60)

@Suppress("MagicNumber")
private fun Instant.plusMinutes(minutes: Long): Instant = plusSeconds(minutes * 60)
