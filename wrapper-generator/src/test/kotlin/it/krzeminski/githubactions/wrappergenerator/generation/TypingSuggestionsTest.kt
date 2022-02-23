package it.krzeminski.githubactions.wrappergenerator.generation

import io.kotest.core.spec.style.FunSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe
import it.krzeminski.githubactions.wrappergenerator.metadata.Input


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

    test("Detect ListOfStringsTyping") {
        listOf(
            Input(description = "a list of file names for caching multiple dependencies."),
            Input(description = "Accept paths to code"),
            Input(description = "comma separated"),
            Input(description = "newline separated"),
            Input(description = "labels to set on the Pull Request comma separated"),
            Input(description = "users to request a review from (comma or newline-separated)."),
        ).forAll { input ->
            input.suggestTyping() shouldBe """ListOfStringsTyping(TODO("please check"))"""
        }
    }

})