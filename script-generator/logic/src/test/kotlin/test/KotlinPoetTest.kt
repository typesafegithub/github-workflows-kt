package test

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.asTypeName
import io.github.typesafegithub.workflows.actions.actions.SetupNodeV3
import io.github.typesafegithub.workflows.domain.triggers.PullRequest
import io.github.typesafegithub.workflows.scriptgenerator.CodeBlock
import io.github.typesafegithub.workflows.scriptgenerator.Members
import io.github.typesafegithub.workflows.scriptgenerator.enumMemberName
import io.github.typesafegithub.workflows.scriptgenerator.joinToCode
import io.github.typesafegithub.workflows.scriptgenerator.templateOf
import io.github.typesafegithub.workflows.wrappergenerator.generation.toCamelCase
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class KotlinPoetTest : DescribeSpec({

    it("CodeBlock { }") {
        val block = CodeBlock { builder ->
            builder.add("println(%S)", "hello world")
        }
        block.toString() shouldBe """
            println("hello world")
        """.trimIndent()
    }

    it("enumMemberName()") {
        val validEnum = enumMemberName<PullRequest.Type>("assigned")
        validEnum?.toString() shouldBe "io.github.typesafegithub.workflows.domain.triggers.PullRequest.Type.Assigned"

        val invalidEnum = enumMemberName<PullRequest.Type>("invalid")
        invalidEnum?.toString() shouldBe null
    }

    describe("List.joinToCodeBlock()") {
        it("generates listOf(...)") {
            val codeBlock = listOf(1, 2, 3)
                .joinToCode(
                    prefix = CodeBlock.of("listOf("),
                    transform = { CodeBlock.of("%L", it * it) },
                )

            codeBlock.toString() shouldBe """
                listOf(
                  1,
                  4,
                  9,
                )

            """.trimIndent()
        }

        it("generates linkedMapOf(...)") {
            val codeBlock = listOf(1, 2, 3)
                .joinToCode(
                    separator = ", ",
                    prefix = CodeBlock.of("%M(", Members.linkedMapOf),
                    transform = { CodeBlock.of("%S", "$it$it") },
                )

            codeBlock.toString() shouldBe """
                kotlin.collections.linkedMapOf("11", "22", "33")

            """.trimIndent()
        }

        it("generates empty list") {
            val list: List<Int> = emptyList()

            val codeBlock = list.joinToCode(
                ifEmpty = CodeBlock.of("emptyList<String>()"),
                newLineAtEnd = false,
            )

            codeBlock.toString() shouldBe "emptyList<String>()"
        }

        it("generates call to a constructor from a map") {
            val actionMap: Map<String, Any?> = mapOf(
                "always-auth" to false,
                "node-version" to "1.0.0",
                "architecture" to null,
                "token" to "my-token",
            )

            val codeblock = actionMap.joinToCode(
                prefix = CodeBlock.of("val %L = %T(", "myAction", SetupNodeV3::class.asTypeName()),
                transform = { key, value ->
                    val template = templateOf(value)
                    if (value == null || template == null) {
                        null
                    } else {
                        CodeBlock.of("%L = $template", key.toCamelCase(), value)
                    }
                },
            )

            codeblock.toString() shouldBe """
              val myAction = io.github.typesafegithub.workflows.actions.actions.SetupNodeV3(
                alwaysAuth = false,
                nodeVersion = "1.0.0",
                token = "my-token",
              )

            """.trimIndent()
        }

        it("renders an empty map or empty list") {
            emptyMap<String, String>().joinToCode(
                transform = { key, value -> CodeBlock.of("$key => $value") },
            ).toString() shouldBe ""

            emptyList<String>().joinToCode()
                .toString() shouldBe ""
        }
    }
})
