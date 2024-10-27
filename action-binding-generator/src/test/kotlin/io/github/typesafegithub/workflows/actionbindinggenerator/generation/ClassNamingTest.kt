package io.github.typesafegithub.workflows.actionbindinggenerator.generation

import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class ClassNamingTest :
    FunSpec({
        context("buildActionClassName") {
            listOf(
                ActionCoords("irrelevant", "some-action-name", "v2") to "SomeActionName",
                ActionCoords("irrelevant", "some-action-name", "v2", "subaction") to "SomeActionNameSubaction",
                ActionCoords("irrelevant", "some-action-name", "v2", "foo/bar/baz") to "SomeActionNameFooBarBaz",
            ).forEach { (input, output) ->
                test("should get '$input' and produce '$output'") {
                    input.buildActionClassName() shouldBe output
                }
            }
        }
    })
