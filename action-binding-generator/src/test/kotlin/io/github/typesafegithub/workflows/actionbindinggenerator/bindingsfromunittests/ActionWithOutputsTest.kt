package io.github.typesafegithub.workflows.actionbindinggenerator.bindingsfromunittests

import io.github.typesafegithub.workflows.actionbindinggenerator.constructAction
import io.github.typesafegithub.workflows.actionbindinggenerator.versioning.BindingVersion.V1
import io.github.typesafegithub.workflows.actionbindinggenerator.versioning.BindingVersion.V2
import io.github.typesafegithub.workflows.actionbindinggenerator.withAllBindingVersions
import io.github.typesafegithub.workflows.actions.johnsmith.ActionWithOutputsBindingV1
import io.github.typesafegithub.workflows.actions.johnsmith.ActionWithOutputsBindingV2
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class ActionWithOutputsTest : DescribeSpec({
    context("fields have correct output placeholders") {
        withAllBindingVersions { bindingVersion ->
            // given
            val outputs = constructAction(
                owner = "johnsmith",
                classBaseName = "ActionWithOutputs",
                bindingVersion = bindingVersion,
                arguments = mapOf("fooBar" to "1"),
            ).buildOutputObject("someStepId")

            // when & then
            when (bindingVersion) {
                V1 -> {
                    outputs as ActionWithOutputsBindingV1.Outputs
                    outputs.bazGoo shouldBe "steps.someStepId.outputs.baz-goo"
                    outputs.looWoz shouldBe "steps.someStepId.outputs.loo-woz"
                }

                V2 -> {
                    outputs as ActionWithOutputsBindingV2.Outputs
                    outputs.bazGoo shouldBe "steps.someStepId.outputs.baz-goo"
                    outputs.looWoz shouldBe "steps.someStepId.outputs.loo-woz"
                }
            }
            outputs["custom-output"] shouldBe "steps.someStepId.outputs.custom-output"
        }
    }
})
