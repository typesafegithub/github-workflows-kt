package io.github.typesafegithub.workflows

import io.github.typesafegithub.workflows.domain.Workflow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.io.DataInputStream

class JavaBytecodeVersionTest :
    FunSpec(
        {
            test("library is built with desired Java bytecode version") {
                Workflow::class.java.classLoader
                    .getResourceAsStream(
                        "io/github/typesafegithub/workflows/domain/Workflow.class",
                    )!!
                    .use { inputStream ->
                        val dataInputStream = DataInputStream(inputStream)
                        require(dataInputStream.readInt() == 0xCAFEBABE.toInt()) { "Invalid class header" }
                        val minor = dataInputStream.readUnsignedShort()
                        val major = dataInputStream.readUnsignedShort()

                        // Corresponds to JDK 11
                        // See https://javaalmanac.io/bytecode/versions/
                        major shouldBe 55
                        minor shouldBe 0
                    }
            }
        },
    )
