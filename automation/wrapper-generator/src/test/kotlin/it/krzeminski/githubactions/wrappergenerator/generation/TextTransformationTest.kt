package it.krzeminski.githubactions.wrappergenerator.generation

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class TextTransformationTest : FunSpec({
    context("toKotlinPackageName") {
        listOf(
            "some-name" to "somename",
            "SomeName" to "somename",
        ).forEach { (input, output) ->
            test("should convert '$input' to '$output'") {
                input.toKotlinPackageName() shouldBe output
            }
        }
    }

    context("toPascalCase") {
        listOf(
            "some-name" to "SomeName",
            "some_name" to "SomeName",
            "SOME_PROPERTY" to "SomeProperty",
            "some+name" to "SomePlusName",
            "some name with spaces" to "SomeNameWithSpaces",
            "some.name.with.dots" to "SomeNameWithDots",
        ).forEach { (input, output) ->
            test("should convert '$input' to '$output'") {
                input.toPascalCase() shouldBe output
            }
        }
    }

    context("toCamelCase") {
        listOf(
            "some-name" to "someName",
            "some_name" to "someName",
            "some+name" to "somePlusName",
            "SOME_NAME" to "someName",
        ).forEach { (input, output) ->
            test("should convert '$input' to '$output'") {
                input.toCamelCase() shouldBe output
            }
        }
    }
},)
