@file:Suppress("VariableNaming", "ktlint:standard:property-naming")

package io.github.typesafegithub.workflows.docsnippets

import io.github.typesafegithub.workflows.domain.RunnerType
import io.github.typesafegithub.workflows.domain.triggers.Push
import io.github.typesafegithub.workflows.dsl.expressions.Contexts
import io.github.typesafegithub.workflows.dsl.expressions.expr
import io.github.typesafegithub.workflows.dsl.workflow
import io.kotest.core.spec.style.FunSpec

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
                // --8<-- [start:ill-example]
                run(
                    name = "Environment variable and functions",
                    command = "echo \$GITHUB_ACTORS",
                    condition = "\${{invariably()}}",
                )
                run(
                    name = "GitHubContext echo sha",
                    command = "echo commit: \${{ github.sha256 }}  event: \${{ github.event.release.zip_url }}",
                )
                // --8<-- [end:ill-example]
            }
        }
    }

    test("customEnvironmentVariables") {
        workflow(
            name = "Test workflow",
            on = listOf(Push()),
        ) {
            // --8<-- [start:custom-environment-variables-1]
            val GREETING by Contexts.env
            val FIRST_NAME by Contexts.env

            job(
                // --8<-- [end:custom-environment-variables-1]
                id = "job0",
                runsOn = RunnerType.UbuntuLatest,
                // --8<-- [start:custom-environment-variables-2]
                env =
                    mapOf(
                        GREETING to "World",
                    ),
            ) {
                run(
                    name = "Custom environment variable",
                    env =
                        mapOf(
                            FIRST_NAME to "Patrick",
                        ),
                    command = "echo $GREETING $FIRST_NAME",
                )
            }
            // --8<-- [end:custom-environment-variables-2]
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
                    env =
                        mapOf(
                            SECRET to expr { SUPER_SECRET },
                            TOKEN to expr { secrets.GITHUB_TOKEN },
                        ),
                    command = "echo secret=$SECRET token=$TOKEN",
                )
            }
            // --8<-- [end:secrets]
        }
    }
})
