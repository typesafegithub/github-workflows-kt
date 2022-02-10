package it.krzeminski.githubactions.wrappergenerator.generation

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import it.krzeminski.githubactions.wrappergenerator.domain.ActionCoords

class ClassNamingTest : FunSpec({
    context("buildActionClassName") {
        listOf(
            ActionCoords("irrelevant", "some-action-name", "v2") to "SomeActionNameV2",
            ActionCoords("irrelevant", "some-action-name", "v3.2.1") to "SomeActionNameV3",
            ActionCoords("irrelevant", "some-action-name", "latest") to "SomeActionName",
        ).forEach { (input, output) ->
            test("should get '$input' and produde '$output'") {
                input.buildActionClassName() shouldBe output
            }
        }
    }
})
