package io.github.typesafegithub.workflows.mavenbinding

import java.security.MessageDigest

internal fun ByteArray.md5Checksum() = checksum("MD5")

internal fun String.md5Checksum() = checksum("MD5")

internal fun ByteArray.sha1Checksum() = checksum("SHA-1")

internal fun String.sha1Checksum() = checksum("SHA-1")

internal fun ByteArray.sha256Checksum() = checksum("SHA-256")

internal fun String.sha256Checksum() = checksum("SHA-256")

internal fun ByteArray.sha512Checksum() = checksum("SHA-512")

internal fun String.sha512Checksum() = checksum("SHA-512")

private fun ByteArray.checksum(algorithm: String): String {
    val digest = MessageDigest.getInstance(algorithm)
    val hashBytes = digest.digest(this)
    return hashBytes.joinToString("") { "%02x".format(it) }
}

private fun String.checksum(algorithm: String) = toByteArray(charset = Charsets.UTF_8).checksum(algorithm)
