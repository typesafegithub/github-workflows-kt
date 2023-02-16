package it.krzeminski.githubactions.docsnippets

import io.kotest.core.spec.style.FunSpec
import it.krzeminski.githubactions.domain.RunnerType
import it.krzeminski.githubactions.domain.actions.Action
import it.krzeminski.githubactions.domain.actions.CustomAction
import it.krzeminski.githubactions.domain.triggers.Push
import it.krzeminski.githubactions.dsl.expressions.expr
import it.krzeminski.githubactions.dsl.workflow

class UsingActionsSnippets : FunSpec({
    test("actionWithoutOutputs") {
/* ktlint-disable indent */
// --8<-- [start:actionWithoutOutputs]
class MyCoolActionV3(
    private val someArgument: String,
) : Action<Action.Outputs>("acmecorp", "cool-action", "v3") {
    override fun toYamlArguments() = linkedMapOf(
        "some-argument" to someArgument,
    )

    override fun buildOutputObject(stepId: String) = Outputs(stepId)
}
// --8<-- [end:actionWithoutOutputs]
/* ktlint-enable indent */
    }

    test("actionWithOutputs") {
/* ktlint-disable indent */
// --8<-- [start:actionWithOutputs1]
class MyCoolActionV3(
    private val someArgument: String,
) : Action<MyCoolActionV3.Outputs>("acmecorp", "cool-action", "v3") {
    override fun toYamlArguments() = linkedMapOf(
        "some-argument" to someArgument,
    )

    override fun buildOutputObject(stepId: String) = Outputs(stepId)

// --8<-- [end:actionWithOutputs1]
    inner
// --8<-- [start:actionWithOutputs2]
    class Outputs(stepId: String) : Action.Outputs(stepId) {
        public val coolOutput: String = "steps.$stepId.outputs.coolOutput"
    }
}
// --8<-- [end:actionWithOutputs2]
/* ktlint-enable indent */

        workflow(
            name = "Test workflow",
            on = listOf(Push()),
        ) {
            job(id = "test-job", runsOn = RunnerType.UbuntuLatest) {
/* ktlint-disable indent */
// --8<-- [start:using]
uses(
    name = "FooBar",
    action = MyCoolActionV3(someArgument = "foobar"),
)
// --8<-- [end:using]
/* ktlint-enable indent */
            }
        }
    }

    test("customAction") {
/* ktlint-disable indent */
// --8<-- [start:customAction]
val customAction = CustomAction(
    actionOwner = "xu-cheng",
    actionName = "latex-action",
    actionVersion = "v2",
    inputs = linkedMapOf(
        "root_file" to "report.tex",
        "compiler" to "latexmk",
    ),
)
// --8<-- [end:customAction]
/* ktlint-enable indent */

        workflow(
            name = "Test workflow",
            on = listOf(Push()),
        ) {
/* ktlint-disable indent */
// --8<-- [start:customActionOutputs]
job("test_job", runsOn = RunnerType.UbuntuLatest) {
    val customActionStep = uses(
        name = "Some step with output",
        action = customAction,
    )

    // use your outputs:
    println(expr(customActionStep.outputs["custom-output"]))
}
// --8<-- [end:customActionOutputs]
/* ktlint-enable indent */
        }
    }
},)
