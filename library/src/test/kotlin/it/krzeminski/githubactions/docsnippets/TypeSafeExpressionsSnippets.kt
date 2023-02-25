@file:Suppress("VariableNaming")

package it.krzeminski.githubactions.docsnippets

import io.kotest.core.spec.style.FunSpec
import it.krzeminski.githubactions.domain.RunnerType
import it.krzeminski.githubactions.domain.triggers.Push
import it.krzeminski.githubactions.dsl.expressions.Contexts
import it.krzeminski.githubactions.dsl.expressions.expr
import it.krzeminski.githubactions.dsl.workflow

class TypeSafeExpressionsSnippets : FunSpec({
    test("illExample") {
        workflow(
            name = "Test workflow",
            on = listOf(Push()),
        ) {
            job(
                id = "test_job",
                runsOn = RunnerType.UbuntuLatest,
            ) {
                // --8<-- [start:illExample]
                run(
                    name = "Environment variable and functions",
                    command = "echo \$GITHUB_ACTORS",
                    condition = "\${{invariably()}}",
                )
                run(
                    name = "GitHubContext echo sha",
                    command = "echo commit: \${{ github.sha256 }}  event: \${{ github.event.release.zip_url }}",
                )
                // --8<-- [end:illExample]
            }
        }
    }

    test("customEnvironmentVariables") {
        workflow(
            name = "Test workflow",
            on = listOf(Push()),
        ) {
            // --8<-- [start:customEnvironmentVariables1]
            val GREETING by Contexts.env
            val FIRST_NAME by Contexts.env

            job(
                // --8<-- [end:customEnvironmentVariables1]
                id = "job0",
                runsOn = RunnerType.UbuntuLatest,
                // --8<-- [start:customEnvironmentVariables2]
                env = linkedMapOf(
                    GREETING to "World",
                ),
            ) {
                run(
                    name = "Custom environment variable",
                    env = linkedMapOf(
                        FIRST_NAME to "Patrick",
                    ),
                    command = "echo $GREETING $FIRST_NAME",
                )
            }
            // --8<-- [end:customEnvironmentVariables2]
        }
    }

    test("secrets") {
        workflow(
            name = "Test workflow",
            on = listOf(Push()),
        ) {
            // --8<-- [start:secrets]
            val SUPER_SECRET by Contexts.secrets

            val SECRET by Contexts.env
            val TOKEN by Contexts.env

            job(id = "job1", runsOn = RunnerType.UbuntuLatest) {
                run(
                    name = "Encrypted secret",
                    env = linkedMapOf(
                        SECRET to expr { SUPER_SECRET },
                        TOKEN to expr { secrets.GITHUB_TOKEN },
                    ),
                    command = "echo secret=$SECRET token=$TOKEN",
                )
            }
            // --8<-- [end:secrets]
        }
    }
},)
