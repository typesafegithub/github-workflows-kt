package io.github.typesafegithub.workflows.actionbindinggenerator.generation

import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.SignificantVersion.FULL
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.SignificantVersion.MAJOR
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class ClassNamingTest :
    FunSpec({
        context("buildActionClassName") {
            listOf(
                ActionCoords("irrelevant", "some-action-name", "v2") to "SomeActionName",
                ActionCoords("irrelevant", "some-action-name", "v2", FULL, "subaction") to "SomeActionNameSubaction",
                ActionCoords("irrelevant", "some-action-name", "v2", FULL, "foo/bar/baz") to "SomeActionNameFooBarBaz",
                ActionCoords("irrelevant", "some-action-name", "v2", MAJOR, "foo/bar/baz") to "SomeActionNameFooBarBaz",
            ).forEach { (input, output) ->
                test("should get '$input' and produce '$output'") {
                    input.buildActionClassName() shouldBe output
                }
            }
        }
    })
