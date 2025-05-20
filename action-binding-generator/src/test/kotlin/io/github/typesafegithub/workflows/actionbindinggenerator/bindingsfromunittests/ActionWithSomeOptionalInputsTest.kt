package io.github.typesafegithub.workflows.actionbindinggenerator.bindingsfromunittests

import io.github.typesafegithub.workflows.actionbindinggenerator.constructAction
import io.github.typesafegithub.workflows.actionbindinggenerator.withAllBindingVersions
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class ActionWithSomeOptionalInputsTest : DescribeSpec({
    context("renders with defaults") {
        withAllBindingVersions { bindingVersion ->
            // given
            val action = constructAction(
                owner = "johnsmith",
                classBaseName = "ActionWithSomeOptionalInputs",
                bindingVersion = bindingVersion,
                arguments = mapOf(
                    "bazGoo" to "def456",
                    "package" to "qwe789",
                ),
            )

            // when
            val yaml = action.toYamlArguments()

            // then
            yaml shouldBe linkedMapOf(
                "baz-goo" to "def456",
                "package" to "qwe789",
            )
        }
    }
})
