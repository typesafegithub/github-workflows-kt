package io.github.typesafegithub.workflows.shared.internal.app

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.io.File
import java.time.Instant

/**
 * JWT generator that creates JSON Web Tokens (JWT) signed with RSA256.
 *
 * This class signs tokens using a provided RSA [privateKey].
 *
 * @param githubClientId the public client ID used as the JWT issuer.
 * @param privateKey the RSA private key file used to sign tokens (expected to be in PKCS#8 format).
 */
class JwtGenerator(
    val githubClientId: String = "Iv23liIZ17VJKUpjacBs", // Public Client ID
    val privateKey: File = File("/config/app.pem"), // Either mounted via docker or present at location
) {
    fun generateJWT(): String {
        val key = PrivateKeyLoader(privateKey).load()
        val algorithm = Algorithm.RSA256(null, key)
        val now = Instant.now()
        return JWT
            .create()
            .withIssuer(githubClientId)
            .withIssuedAt(now.minusMinutes(1))
            .withExpiresAt(now.plusMinutes(9))
            .sign(algorithm)
    }
}

private fun Instant.minusMinutes(minutes: Long): Instant = minusSeconds(minutes * 60)

private fun Instant.plusMinutes(minutes: Long): Instant = plusSeconds(minutes * 60)
