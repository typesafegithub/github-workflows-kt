package io.github.typesafegithub.workflows.shared.internal.app

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.kotest.matchers.date.shouldBeCloseTo
import java.io.File
import java.security.KeyPairGenerator
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.time.Instant
import java.util.Base64
import kotlin.time.Duration.Companion.seconds

class JwtGeneratorTest : FunSpec({

    test("generateJWT produces a valid JWT with correct issuer") {
        // Generate an RSA key pair for testing.
        val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
        keyPairGenerator.initialize(2048)
        val keyPair = keyPairGenerator.genKeyPair()
        val privateKey = keyPair.private as RSAPrivateKey
        val publicKey = keyPair.public as RSAPublicKey

        // Convert the private key to a PEM-formatted string with header/footer.
        val base64Key = Base64.getEncoder().encodeToString(privateKey.encoded)
        val pemContent = """
            -----BEGIN PRIVATE KEY-----
            ${base64Key.chunked(64).joinToString("\n")}
            -----END PRIVATE KEY-----
        """.trimIndent()

        // Write the PEM content to a temporary file.
        val tempFile = File.createTempFile("testkey", ".pem")
        tempFile.writeText(pemContent)

        // Define a test GitHub client ID.
        val testClientId = "TestClientId"

        // Create the JwtGenerator instance using the temporary private key file.
        val jwtGenerator = JwtGenerator(testClientId, tempFile)

        // Generate the JWT token.
        val token = jwtGenerator.generateJWT()

        // Verify the token using the corresponding public key.
        val algorithm = Algorithm.RSA256(publicKey, null)
        val verifier = JWT.require(algorithm)
            .withIssuer(testClientId)
            .build()
        val decodedJWT = verifier.verify(token)

        // Assert that the issuer matches the test client ID.
        decodedJWT.issuer shouldBe testClientId

        // Optionally, assert that the issuedAt and expiresAt claims are valid.
        val now = Instant.now()
        decodedJWT.issuedAt.toInstant().shouldBeCloseTo(now.minusSeconds(60), 5.seconds)
        decodedJWT.expiresAt.toInstant().shouldBeCloseTo(now.plusSeconds(540), 5.seconds)
    }
})
