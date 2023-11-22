package io.github.typesafegithub.workflows

import io.github.typesafegithub.workflows.actions.actions.CheckoutV4
import io.github.typesafegithub.workflows.actions.actions.GithubScriptV7
import io.github.typesafegithub.workflows.actions.actions.SetupPythonV4
import io.github.typesafegithub.workflows.domain.JobOutputs
import io.github.typesafegithub.workflows.domain.Mode
import io.github.typesafegithub.workflows.domain.Permission
import io.github.typesafegithub.workflows.domain.RunnerType
import io.github.typesafegithub.workflows.domain.actions.Action
import io.github.typesafegithub.workflows.domain.actions.CustomAction
import io.github.typesafegithub.workflows.domain.actions.CustomDockerAction
import io.github.typesafegithub.workflows.domain.actions.CustomLocalAction
import io.github.typesafegithub.workflows.domain.actions.DockerAction
import io.github.typesafegithub.workflows.domain.actions.LocalAction
import io.github.typesafegithub.workflows.domain.actions.RegularAction
import io.github.typesafegithub.workflows.domain.triggers.PullRequest
import io.github.typesafegithub.workflows.domain.triggers.Push
import io.github.typesafegithub.workflows.dsl.expressions.Contexts
import io.github.typesafegithub.workflows.dsl.expressions.expr
import io.github.typesafegithub.workflows.dsl.workflow
import io.github.typesafegithub.workflows.yaml.writeToFile
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.nio.file.Path

@Suppress("VariableNaming", "ktlint:standard:property-naming")
class RunOnGitHubTest : FunSpec({
    test("generate workflow") {
        val targetPath = Path.of("../.github/workflows/Integration tests.yaml")
        val expectedYaml = targetPath.toFile().readText()
        val trivialWorkflow =
            workflow(
                name = "Integration tests",
                on =
                    listOf(
                        Push(branches = listOf("main")),
                        PullRequest(),
                    ),
                sourceFile = Path.of("../.github/workflows/Integration tests.main.kts"),
            ) {
                val GREETING by Contexts.env
                val FIRST_NAME by Contexts.env
                val SECRET by Contexts.env
                val TOKEN by Contexts.env
                val SUPER_SECRET by Contexts.secrets

                val testJob1 =
                    job(
                        id = "test_job_1",
                        runsOn = RunnerType.UbuntuLatest,
                        env =
                            linkedMapOf(
                                GREETING to "World",
                            ),
                        permissions =
                            mapOf(
                                Permission.Actions to Mode.Read,
                                Permission.Checks to Mode.Write,
                                Permission.Contents to Mode.None,
                            ),
                        outputs =
                            object : JobOutputs() {
                                var scriptKey by output()
                                var scriptKey2 by output()
                                var scriptResult by output()
                            },
                    ) {
                        run(
                            name = "Hello world!",
                            command = "echo 'hello!'",
                        )

                        run(
                            name = "Hello world! Multiline command with pipes",
                            command =
                                """
                                less test.txt \
                                | grep -P "foobar" \
                                | sort \
                                > result.txt
                                """.trimIndent(),
                        )

                        uses(
                            name = "Check out",
                            action =
                                CustomAction(
                                    actionOwner = "actions",
                                    actionName = "checkout",
                                    actionVersion = "v4",
                                ),
                        )

                        uses(
                            name = "Run local action",
                            action =
                                CustomLocalAction(
                                    actionPath = "./.github/workflows/test-local-action",
                                    inputs =
                                        mapOf(
                                            "name" to "Rocky",
                                        ),
                                ),
                        )

                        uses(
                            name = "Run alpine",
                            action =
                                CustomDockerAction(
                                    actionImage = "alpine",
                                    actionTag = "latest",
                                ),
                        )

                        uses(
                            name = "Check out again",
                            action =
                                object : RegularAction<Action.Outputs>(
                                    actionOwner = "actions",
                                    actionName = "checkout",
                                    actionVersion = "v4",
                                ) {
                                    override fun toYamlArguments() =
                                        linkedMapOf(
                                            "repository" to "actions/checkout",
                                            "ref" to "v3",
                                            "path" to "./.github/actions/checkout",
                                            "clean" to "false",
                                        )

                                    override fun buildOutputObject(stepId: String) = Action.Outputs(stepId)
                                },
                        )

                        uses(
                            name = "Run local action",
                            action =
                                object : LocalAction<Action.Outputs>(
                                    actionPath = "./.github/workflows/test-local-action",
                                ) {
                                    override fun toYamlArguments() =
                                        linkedMapOf(
                                            "name" to "Balboa",
                                        )

                                    override fun buildOutputObject(stepId: String) = Action.Outputs(stepId)
                                },
                        )

                        uses(
                            name = "Run alpine",
                            action =
                                object : DockerAction<Action.Outputs>(
                                    actionImage = "alpine",
                                    actionTag = "latest",
                                ) {
                                    override fun toYamlArguments() = linkedMapOf<String, String>()

                                    override fun buildOutputObject(stepId: String) = Action.Outputs(stepId)
                                },
                        )

                        val addAndCommit = uses(action = SetupPythonV4())

                        uses(
                            name = "Some step consuming other step's output",
                            action =
                                CheckoutV4(
                                    sshKey = expr(addAndCommit.outputs.pythonVersion),
                                    path = expr(addAndCommit.outputs["my-unsafe-output"]),
                                ),
                        )

                        run(
                            name = "Custom environment variable",
                            env =
                                linkedMapOf(
                                    FIRST_NAME to "Patrick",
                                ),
                            command = "echo $GREETING $FIRST_NAME",
                        )
                        run(
                            name = "Encrypted secret",
                            env =
                                linkedMapOf(
                                    SECRET to expr { SUPER_SECRET },
                                    TOKEN to expr { secrets.GITHUB_TOKEN },
                                ),
                            command = "echo secret=$SECRET token=$TOKEN",
                        )
                        run(
                            name = "RunnerContext create temp directory",
                            command = "mkdir " + expr { runner.temp } + "/build_logs",
                        )
                        run(
                            name = "GitHubContext echo sha",
                            command = "echo " + expr { github.sha } + " ev " + expr { github.eventRelease.release.url },
                        )
                        run(
                            name = "Default environment variable",
                            command = "action=${Contexts.env.GITHUB_ACTION} repo=${Contexts.env.GITHUB_REPOSITORY}",
                            condition = expr { always() },
                        )
                        val scriptStep =
                            uses(
                                action =
                                    GithubScriptV7(
                                        script =
                                            """
                                            core.setOutput("key", "value")
                                            core.setOutput("key2", "value2")
                                            return "return"
                                            """.trimIndent(),
                                    ),
                            )
                        jobOutputs.scriptKey = scriptStep.outputs["key"]
                        jobOutputs.scriptKey2 = scriptStep.outputs["key2"]
                        jobOutputs.scriptResult = scriptStep.outputs.result
                    }

                job(
                    id = "test_job_2",
                    runsOn = RunnerType.UbuntuLatest,
                    condition = "\${{ always() }}",
                    needs = listOf(testJob1),
                ) {
                    run(
                        name = "Hello world, again!",
                        command = "echo 'hello again!'",
                    )
                    run(
                        name = "use output of script",
                        command =
                            """
                            echo ${expr { testJob1.outputs.scriptKey }}
                            echo ${expr { testJob1.outputs.scriptKey2 }}
                            echo ${expr { testJob1.outputs.scriptResult }}
                            """.trimIndent(),
                    )
                }
            }

        // when
        trivialWorkflow.writeToFile(addConsistencyCheck = false)

        // then
        val actualYaml = targetPath.toFile().readText()
        actualYaml shouldBe expectedYaml
    }
})
