package test

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.asTypeName
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.data.forAll
import io.kotest.data.headers
import io.kotest.data.row
import io.kotest.data.table
import io.kotest.matchers.shouldBe
import it.krzeminski.githubactions.actions.actions.SetupNodeV2
import it.krzeminski.githubactions.domain.triggers.PullRequest
import it.krzeminski.githubactions.scriptgenerator.CodeBlock
import it.krzeminski.githubactions.scriptgenerator.Members
import it.krzeminski.githubactions.scriptgenerator.TemplateArg
import it.krzeminski.githubactions.scriptgenerator.enumMemberName
import it.krzeminski.githubactions.scriptgenerator.joinToCodeBlock
import it.krzeminski.githubactions.scriptgenerator.templateOf
import it.krzeminski.githubactions.scriptgenerator.valueWithTyping
import it.krzeminski.githubactions.wrappergenerator.domain.ActionCoords
import it.krzeminski.githubactions.wrappergenerator.domain.typings.BooleanTyping
import it.krzeminski.githubactions.wrappergenerator.domain.typings.EnumTyping
import it.krzeminski.githubactions.wrappergenerator.domain.typings.IntegerTyping
import it.krzeminski.githubactions.wrappergenerator.domain.typings.IntegerWithSpecialValueTyping
import it.krzeminski.githubactions.wrappergenerator.domain.typings.ListOfTypings
import it.krzeminski.githubactions.wrappergenerator.domain.typings.StringTyping
import it.krzeminski.githubactions.wrappergenerator.generation.toCamelCase

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
        validEnum?.toString() shouldBe "it.krzeminski.githubactions.domain.triggers.PullRequest.Type.Assigned"

        val invalidEnum = enumMemberName<PullRequest.Type>("invalid")
        invalidEnum?.toString() shouldBe null
    }

    describe("List.joinToCodeBlock()") {
        it("generates listOf(...)") {
            val codeBlock = listOf(1, 2, 3)
                .joinToCodeBlock(
                    prefix = CodeBlock.of("listOf("),
                    transform = { CodeBlock.of("%L", it * it) }
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
                .joinToCodeBlock(
                    separator = CodeBlock.of(", "),
                    prefix = CodeBlock.of("%M(", Members.linkedMapOf),
                    transform = { CodeBlock.of("%S", "$it$it") }
                )

            codeBlock.toString() shouldBe """
                kotlin.collections.linkedMapOf("11", "22", "33")
                
            """.trimIndent()
        }

        it("generates empty list") {
            val list: List<Int> = emptyList()

            val codeBlock = list.joinToCodeBlock(
                ifEmpty = CodeBlock.of("emptyList<String>()"),
                newLineAtEnd = false
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

            val codeblock = actionMap.joinToCodeBlock(
                prefix = CodeBlock.of("val %L = %T(", "myAction", SetupNodeV2::class.asTypeName()),
                transform = { key, value ->
                    val template = templateOf(value)
                    if (value == null || template == null) {
                        null
                    } else {
                        CodeBlock.of("%L = $template", key.toCamelCase(), value)
                    }
                }
            )

            codeblock.toString() shouldBe """
              val myAction = it.krzeminski.githubactions.actions.actions.SetupNodeV2(
                alwaysAuth = false,
                nodeVersion = "1.0.0",
                token = "my-token",
              )
              
            """.trimIndent()
        }

        it("renders an empty map or empty list") {
            emptyMap<String, String>().joinToCodeBlock(
                transform = { key, value -> CodeBlock.of("$key => $value") }
            ).toString() shouldBe ""

            emptyList<String>().joinToCodeBlock()
                .toString() shouldBe ""
        }
    }

    it("it understands typings") {
        val coords = ActionCoords("actions", "cache", "v2")
        val enumTyping = EnumTyping("Enum", listOf("enum1", "enum2"))
        val listTyping1 = ListOfTypings(",", StringTyping)
        val listTyping2 = ListOfTypings("\\n", enumTyping)
        val listTyping3 = ListOfTypings(",", IntegerTyping)
        val specialIntTyping = IntegerWithSpecialValueTyping("SpecialInt", mapOf("answer" to 4))

        table(
            headers("typing", "value", "expected"),
            row(StringTyping, "hello", TemplateArg("%S", "hello")),
            row(IntegerTyping, "42", TemplateArg("%L", "42")),
            row(BooleanTyping, "true", TemplateArg("%L", "true")),
            row(enumTyping, "enum1", TemplateArg("%L", "CacheV2.Enum.Enum1")),
            row(listTyping1, "hello,world", TemplateArg("%L", """listOf("hello", "world")""")),
            row(listTyping2, "enum1\nenum2", TemplateArg("%L", "listOf(CacheV2.Enum.Enum1, CacheV2.Enum.Enum2)")),
            row(listTyping3, "1,2", TemplateArg("%L", """listOf(1, 2)""")),
            row(specialIntTyping, "42", TemplateArg("%L", "CacheV2.SpecialInt.Value(42)")),
        ).forAll { typing, value, templateArg ->
            valueWithTyping(value, typing, coords) shouldBe templateArg
        }
    }
})
