package it.krzeminski.githubactions.wrappergenerator.generation

import io.kotest.core.spec.style.FunSpec
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import it.krzeminski.githubactions.wrappergenerator.metadata.Input

class InputNullabilityTest : FunSpec({
    context("shouldBeNonNullInWrapper") {
        listOf(
            // No info about default value or input being required, so let's assume more freedom here - nullable.
            row(null, null, false),
            // Default given so let's assume it's nullable.
            row("some default", null, false),
            // Default not given and not required: assuming some default, empty value is provided by GH Actions.
            row(null, false, false),
            // Default given and not required: it's definitely nullable.
            row("some default", false, false),
            // Required = true, a no-brainer it should be non-null.
            row(null, true, true),
            // A required input with default: it should never happen, but let's try allowing null here.
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
},)
