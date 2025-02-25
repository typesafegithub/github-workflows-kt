package io.github.typesafegithub.workflows.mavenbinding

import java.security.MessageDigest

internal fun ByteArray.sha1Checksum(): String {
    val sha1 = MessageDigest.getInstance("SHA-1")
    val hashBytes = sha1.digest(this)
    return hashBytes.joinToString("") { "%02x".format(it) }
}

internal fun String.sha1Checksum(): String = this.toByteArray(charset = Charsets.UTF_8).sha1Checksum()
