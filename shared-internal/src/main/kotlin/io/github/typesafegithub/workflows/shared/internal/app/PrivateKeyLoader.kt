package io.github.typesafegithub.workflows.shared.internal.app

import java.io.File
import java.security.KeyFactory
import java.security.interfaces.RSAPrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import java.util.Base64

class PrivateKeyLoader(val privateKey: File) {
    fun load(): RSAPrivateKey {
        val content = privateKey.readLines()
        val filtered = content.filter { !it.trim().startsWith("-----") }.joinToString("").replace("\\s".toRegex(), "")
        val keyBytes = Base64.getDecoder().decode(filtered)
        val keySpec = PKCS8EncodedKeySpec(keyBytes)
        val keyFactory = KeyFactory.getInstance("RSA")
        return keyFactory.generatePrivate(keySpec) as RSAPrivateKey
    }
}
