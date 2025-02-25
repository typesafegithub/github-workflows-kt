package io.github.typesafegithub.workflows.mavenbinding

import java.security.MessageDigest

internal fun ByteArray.sha512Checksum(): String {
    val sha512 = MessageDigest.getInstance("SHA-512")
    val hashBytes = sha512.digest(this)
    return hashBytes.joinToString("") { "%02x".format(it) }
}

internal fun String.sha512Checksum(): String = this.toByteArray(charset = Charsets.UTF_8).sha512Checksum()
