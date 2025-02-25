package io.github.typesafegithub.workflows.mavenbinding

import java.security.MessageDigest

internal fun ByteArray.sha256Checksum(): String {
    val sha256 = MessageDigest.getInstance("SHA-256")
    val hashBytes = sha256.digest(this)
    return hashBytes.joinToString("") { "%02x".format(it) }
}

internal fun String.sha256Checksum(): String = this.toByteArray(charset = Charsets.UTF_8).sha256Checksum()
