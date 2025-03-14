package io.github.typesafegithub.workflows.actionbindinggenerator.bindingsfromunittests

import io.github.typesafegithub.workflows.actionbindinggenerator.constructAction
import io.github.typesafegithub.workflows.actionbindinggenerator.withAllBindingVersions
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class SimpleActionWithRequiredStringInputsTest : DescribeSpec({
    context("renders with defaults") {
        withAllBindingVersions { bindingVersion ->
            // given
            val action = constructAction(
                owner = "johnsmith",
                classBaseName = "SimpleActionWithRequiredStringInputs",
                bindingVersion = bindingVersion,
                arguments = mapOf(
                    "fooBar" to "abc123",
                    "bazGoo" to "def456",
                ),
            )

            // when
            val yaml = action.toYamlArguments()

            // then
            yaml shouldBe linkedMapOf(
                "foo-bar" to "abc123",
                "baz-goo" to "def456",
            )
        }
    }
})
