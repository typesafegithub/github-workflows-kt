@file:Suppress("VariableNaming", "ktlint:standard:property-naming")

package io.github.typesafegithub.workflows.docsnippets

import io.github.typesafegithub.workflows.domain.RunnerType
import io.github.typesafegithub.workflows.domain.triggers.Push
import io.github.typesafegithub.workflows.dsl.expressions.Contexts
import io.github.typesafegithub.workflows.dsl.expressions.expr
import io.github.typesafegithub.workflows.dsl.workflow
import io.kotest.core.spec.style.FunSpec
import io.kotest.engine.spec.tempdir

class TypeSafeExpressionsSnippets :
    FunSpec({
        val gitRootDir =
            tempdir()
                .also {
                    it.resolve(".git").mkdirs()
                }.toPath()
        val sourceTempFile = gitRootDir.resolve(".github/workflows/some_workflow.main.kts").toFile()

        test("illExample") {
            workflow(
                name = "Test workflow",
                on = listOf(Push()),
                sourceFile = sourceTempFile,
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
                sourceFile = sourceTempFile,
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
                sourceFile = sourceTempFile,
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

        test("vars") {
            workflow(
                name = "Test workflow",
                on = listOf(Push()),
                sourceFile = sourceTempFile,
            ) {
                // --8<-- [start:secrets]
                val SOME_VARIABLE by Contexts.vars

                job(id = "job1", runsOn = RunnerType.UbuntuLatest) {
                    run(
                        name = "Some Variable",
                        command = "echo someVariable=${expr { SOME_VARIABLE }}",
                    )
                }
                // --8<-- [end:secrets]
            }
        }
    })
