package io.github.typesafegithub.workflows.docsnippets

import io.github.typesafegithub.workflows.domain.JobOutputs
import io.github.typesafegithub.workflows.domain.RunnerType
import io.github.typesafegithub.workflows.domain.actions.Action
import io.github.typesafegithub.workflows.domain.actions.RegularAction
import io.github.typesafegithub.workflows.domain.triggers.Push
import io.github.typesafegithub.workflows.dsl.expressions.expr
import io.github.typesafegithub.workflows.dsl.workflow
import io.kotest.core.spec.style.FunSpec
import java.util.LinkedHashMap

@Suppress("ktlint:standard:no-consecutive-comments")
class JobOutputsSnippets : FunSpec({
    test("jobOutputs") {
        workflow(
            name = "Test workflow",
            on = listOf(Push()),
        ) {
            // --8<-- [start:define-job-outputs-1]
            val myJob =
                job(
                    id = "my_job",
                    runsOn = RunnerType.UbuntuLatest,
                    outputs =
                        object : JobOutputs() {
                            var myOutput by output()
                            var anotherOutput by output()
                        },
                    // --8<-- [end:define-job-outputs-1]
                /*
                // --8<-- [start:define-job-outputs-2]
                ) { ... }
                // --8<-- [end:define-job-outputs-2]
                 */
                ) {
                    class DocTest : RegularAction<DocTest.Outputs>("doc", "test", "v0") {
                        override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf()

                        override fun buildOutputObject(stepId: String): Outputs = Outputs(stepId)

                        inner class Outputs(stepId: String) : Action.Outputs(stepId) {
                            val someStepOutput: String = ""
                        }
                    }
                    val someStep = uses(action = DocTest())

                    // --8<-- [start:set-job-outputs]
                    jobOutputs.myOutput = someStep.outputs.someStepOutput
                    jobOutputs.anotherOutput = someStep.outputs["custom-output"]
                    // --8<-- [end:set-job-outputs]
                }

            // --8<-- [start:use-job-outputs]
            job(
                id = "use_output",
                runsOn = RunnerType.UbuntuLatest,
                needs = listOf(myJob),
            ) {
                run(
                    name = "Use outputs",
                    command =
                        """
                        echo ${expr { myJob.outputs.myOutput }}
                        echo ${expr { myJob.outputs.anotherOutput }}
                        """.trimIndent(),
                )
            }
            // --8<-- [end:use-job-outputs]
        }
    }
})
