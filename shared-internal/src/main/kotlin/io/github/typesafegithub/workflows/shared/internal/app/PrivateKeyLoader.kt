package io.github.typesafegithub.workflows.shared.internal.app

import java.security.KeyFactory
import java.security.interfaces.RSAPrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import java.util.Base64

class PrivateKeyLoader(
    val privateKey: String,
) {
    fun load(): RSAPrivateKey {
        val filtered = privateKey
            // Remove the standard PEM headers if present (even if they're on the same line)
            .replace("-----BEGIN PRIVATE KEY-----", "")
            .replace("-----END PRIVATE KEY-----", "")
            // Remove all remaining whitespace
            .replace("\\s".toRegex(), "")
        val keyBytes = Base64.getDecoder().decode(filtered)
        val keySpec = PKCS8EncodedKeySpec(keyBytes)
        val keyFactory = KeyFactory.getInstance("RSA")
        return keyFactory.generatePrivate(keySpec) as RSAPrivateKey
    }
}
