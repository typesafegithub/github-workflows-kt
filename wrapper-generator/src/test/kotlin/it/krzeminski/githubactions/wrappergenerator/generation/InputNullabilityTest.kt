package it.krzeminski.githubactions.wrappergenerator.generation

import io.kotest.core.spec.style.FunSpec
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import it.krzeminski.githubactions.wrappergenerator.metadata.Input

class InputNullabilityTest : FunSpec({
    context("shouldBeNonNullInWrapper") {
        listOf(
            row(null, null, true),
            row("some default", null, false),
            row(null, false, false),
            row("some default", false, false),
            row(null, true, true),
            row("some default", true, false),
        ).forEach { (default, required, result) ->
            test("test for default = $default, required = $required - should be non-null: $result") {
                Input(
                    description = "Some input",
                    default = default,
                    required = required,
                ).shouldBeNonNullInWrapper() shouldBe result
            }
        }
    }
})
