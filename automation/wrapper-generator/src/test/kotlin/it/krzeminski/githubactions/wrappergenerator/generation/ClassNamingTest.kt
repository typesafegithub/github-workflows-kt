package it.krzeminski.githubactions.wrappergenerator.generation

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import it.krzeminski.githubactions.actionsmetadata.model.ActionCoords

class ClassNamingTest : FunSpec({
    context("buildActionClassName") {
        listOf(
            ActionCoords("irrelevant", "some-action-name", "v2") to "SomeActionNameV2",
            ActionCoords("irrelevant", "some-action-name/subaction", "v2") to "SomeActionNameSubactionV2",
            ActionCoords("irrelevant", "some-action-name/foo/bar/baz", "v2") to "SomeActionNameFooBarBazV2",
        ).forEach { (input, output) ->
            test("should get '$input' and produce '$output'") {
                input.buildActionClassName() shouldBe output
            }
        }
    }
},)
