package io.github.typesafegithub.workflows.actionbindinggenerator

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
})
