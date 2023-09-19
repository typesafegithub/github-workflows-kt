package io.github.typesafegithub.workflows.actionbindinggenerator

import io.github.typesafegithub.workflows.actionsmetadata.model.ActionCoords
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

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
})
