package io.github.typesafegithub.workflows.docsnippets

import io.github.typesafegithub.workflows.domain.RunnerType
import io.github.typesafegithub.workflows.domain.actions.Action
import io.github.typesafegithub.workflows.domain.actions.CustomAction
import io.github.typesafegithub.workflows.domain.triggers.Push
import io.github.typesafegithub.workflows.dsl.expressions.expr
import io.github.typesafegithub.workflows.dsl.workflow
import io.kotest.core.spec.style.FunSpec

class UsingActionsSnippets : FunSpec({
    test("actionWithoutOutputs") {
        // --8<-- [start:action-without-outputs]
        class MyCoolActionV3(
            private val someArgument: String,
        ) : Action<Action.Outputs>("acmecorp", "cool-action", "v3") {
            override fun toYamlArguments() = linkedMapOf(
                "some-argument" to someArgument,
            )

            override fun buildOutputObject(stepId: String) = Outputs(stepId)
        }
        // --8<-- [end:action-without-outputs]
    }

    test("actionWithOutputs") {
        // --8<-- [start:action-with-outputs-1]
        class MyCoolActionV3(
            private val someArgument: String,
        ) : Action<MyCoolActionV3.Outputs>("acmecorp", "cool-action", "v3") {
            override fun toYamlArguments() = linkedMapOf(
                "some-argument" to someArgument,
            )

            override fun buildOutputObject(stepId: String) = Outputs(stepId)

            // --8<-- [end:action-with-outputs-1]
            inner
            // --8<-- [start:action-with-outputs-2]
            class Outputs(stepId: String) : Action.Outputs(stepId) {
                public val coolOutput: String = "steps.$stepId.outputs.coolOutput"
            }
        }
        // --8<-- [end:action-with-outputs-2]

        workflow(
            name = "Test workflow",
            on = listOf(Push()),
        ) {
            job(id = "test-job", runsOn = RunnerType.UbuntuLatest) {
                // --8<-- [start:using]
                uses(
                    name = "FooBar",
                    action = MyCoolActionV3(someArgument = "foobar"),
                )
                // --8<-- [end:using]
            }
        }
    }

    test("customAction") {
        // --8<-- [start:custom-action]
        val customAction = CustomAction(
            actionOwner = "xu-cheng",
            actionName = "latex-action",
            actionVersion = "v2",
            inputs = linkedMapOf(
                "root_file" to "report.tex",
                "compiler" to "latexmk",
            ),
        )
        // --8<-- [end:custom-action]

        workflow(
            name = "Test workflow",
            on = listOf(Push()),
        ) {
            // --8<-- [start:custom-action-outputs]
            job("test_job", runsOn = RunnerType.UbuntuLatest) {
                val customActionStep = uses(
                    name = "Some step with output",
                    action = customAction,
                )

                // use your outputs:
                println(expr(customActionStep.outputs["custom-output"]))
            }
            // --8<-- [end:custom-action-outputs]
        }
    }
})
