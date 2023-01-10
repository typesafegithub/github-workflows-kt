package it.krzeminski.githubactions.wrappergenerator.generation

import io.kotest.core.spec.style.FunSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe
import it.krzeminski.githubactions.wrappergenerator.metadata.Input
import it.krzeminski.githubactions.wrappergenerator.metadata.Metadata

class TypingSuggestionsTest : FunSpec({

    test("Detect BooleanTyping") {
        listOf(
            Input("description 1", default = "true"),
            Input("description 2", default = "false"),
        ).forAll { input ->
            input.suggestTyping() shouldBe "BooleanTyping"
        }
    }

    test("Detect IntegerTyping") {
        listOf(
            Input("description 1", default = "0"),
            Input("description 2", default = "42"),
        ).forAll { input ->
            input.suggestTyping() shouldBe "IntegerTyping"
        }
    }

    test("Detect ListOfTypings") {
        listOf(
            Input(description = "a list of file names for caching multiple dependencies."),
            Input(description = "Accept paths to code"),
            Input(description = "comma separated"),
            Input(description = "newline separated"),
            Input(description = "labels to set on the Pull Request comma separated"),
            Input(description = "users to request a review from (comma or newline-separated)."),
        ).forAll { input ->
            input.suggestTyping() shouldBe """ListOfTypings(TODO("please check"))"""
        }
    }

    test("Suggestions") {
        val inputs = mapOf(
            "bool" to Input("description 1", default = "true"),
            "int" to Input("description 2", default = "42"),
            "list" to Input(description = "labels to set on the Pull Request comma separated"),
        )

        val actionYaml = Metadata("action", "description", inputs)

        actionYaml.suggestAdditionalTypings(setOf("bool", "int", "list")) shouldBe null

        actionYaml.suggestAdditionalTypings(emptySet()) shouldBe """
            |    mapOf(
            |            "bool" to BooleanTyping,
            |            "int" to IntegerTyping,
            |            "list" to ListOfTypings(TODO("please check")),
            |    )
        """.trimMargin()
    }
},)
