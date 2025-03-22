package io.github.typesafegithub.workflows.shared.internal.app

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.security.KeyPairGenerator
import java.security.interfaces.RSAPrivateKey
import java.util.Base64

class PrivateKeyLoaderTest :
    FunSpec({

        test("should load valid RSA private key from file with header/footer") {
            // Generate an RSA key pair for testing.
            val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
            keyPairGenerator.initialize(2048)
            val keyPair = keyPairGenerator.genKeyPair()
            val originalPrivateKey = keyPair.private as RSAPrivateKey

            // Encode the private key in PKCS#8 format to Base64.
            val keyBytes = originalPrivateKey.encoded
            val base64Key = Base64.getEncoder().encodeToString(keyBytes)

            // Create PEM content with header and footer.
            val pemContent =
                """
                -----BEGIN PRIVATE KEY-----
                ${base64Key.chunked(64).joinToString("\n")}
                -----END PRIVATE KEY-----
                """.trimIndent()

            // Load the private key using PrivateKeyLoader.
            val loadedKey = PrivateKeyLoader(pemContent).load()

            // Assert that the loaded key matches the original key.
            loadedKey shouldBe originalPrivateKey
        }

        test("should load valid RSA private key from file without header/footer") {
            // Generate an RSA key pair for testing.
            val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
            keyPairGenerator.initialize(2048)
            val keyPair = keyPairGenerator.genKeyPair()
            val originalPrivateKey = keyPair.private as RSAPrivateKey

            // Encode the private key in PKCS#8 format to Base64.
            val keyBytes = originalPrivateKey.encoded
            val base64Key = Base64.getEncoder().encodeToString(keyBytes)

            // Create content without header and footer (only the Base64-encoded key).
            val pemContent = base64Key.chunked(64).joinToString("\n")

            // Load the private key using PrivateKeyLoader.
            val loadedKey = PrivateKeyLoader(pemContent).load()

            // Assert that the loaded key matches the original key.
            loadedKey shouldBe originalPrivateKey
        }
    })
