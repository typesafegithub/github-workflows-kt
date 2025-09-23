package io.github.typesafegithub.workflows.testutils

import io.kotest.matchers.shouldBe
import java.io.ByteArrayInputStream
import java.io.DataInputStream
import java.io.InputStream
import kotlin.reflect.KClass

infix fun KClass<*>.shouldHaveBytecodeVersion(expectedVersion: String) {
    this::class.java.classLoader
        .getResourceAsStream(
            this.qualifiedName!!.replace(".", "/") + ".class",
        )!!
        .use { inputStream ->
            inputStream shouldHaveBytecodeVersion expectedVersion
        }
}

infix fun ByteArray.shouldHaveBytecodeVersion(expectedVersion: String) =
    ByteArrayInputStream(this) shouldHaveBytecodeVersion expectedVersion

private infix fun InputStream.shouldHaveBytecodeVersion(expectedVersion: String) {
    val dataInputStream = DataInputStream(this)
    require(dataInputStream.readInt() == 0xCAFEBABE.toInt()) { "Invalid class header" }
    val minor = dataInputStream.readUnsignedShort()
    val major = dataInputStream.readUnsignedShort()

    val actualVersion = "$major.$minor"
    actualVersion shouldBe expectedVersion
}
