package io.github.typesafegithub.workflows.mavenbinding

import java.security.MessageDigest

internal fun ByteArray.md5Checksum(): String {
    val md5 = MessageDigest.getInstance("MD5")
    val hashBytes = md5.digest(this)
    return hashBytes.joinToString("") { "%02x".format(it) }
}

internal fun String.md5Checksum(): String = this.toByteArray(charset = Charsets.UTF_8).md5Checksum()
