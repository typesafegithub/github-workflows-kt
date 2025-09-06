package io.github.typesafegithub.workflows.testutils

import io.kotest.matchers.shouldBe
import io.kotest.mpp.qualifiedNameOrNull
import java.io.DataInputStream
import kotlin.reflect.KClass

infix fun KClass<*>.shouldHaveBytecodeVersion(expectedVersion: String) {
    this::class.java.classLoader
        .getResourceAsStream(
            this.qualifiedNameOrNull()!!.replace(".", "/") + ".class",
        )!!
        .use { inputStream ->
            val dataInputStream = DataInputStream(inputStream)
            require(dataInputStream.readInt() == 0xCAFEBABE.toInt()) { "Invalid class header" }
            val minor = dataInputStream.readUnsignedShort()
            val major = dataInputStream.readUnsignedShort()

            val actualVersion = "$major.$minor"
            actualVersion shouldBe expectedVersion
        }
}
