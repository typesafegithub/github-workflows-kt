#!/usr/bin/env kotlin
@file:Repository("file://~/.m2/repository/")
@file:DependsOn("io.github.typesafegithub:github-workflows-kt:3.5.1-SNAPSHOT")
@file:DependsOn("io.github.typesafegithub:action-updates-checker:3.5.1-SNAPSHOT")
@file:Repository("https://bindings.krzeminski.it")
@file:DependsOn("actions:checkout:v5")
@file:DependsOn("actions:github-script:v7")
@file:DependsOn("actions:setup-java:v4")
@file:DependsOn("actions:setup-python:v5")
@file:DependsOn("gradle:actions__setup-gradle:v4")
@file:DependsOn("Wandalen:wretry.action:v3")
@file:OptIn(ExperimentalKotlinLogicStep::class)

import io.github.typesafegithub.workflows.actions.actions.*
import io.github.typesafegithub.workflows.actions.gradle.ActionsSetupGradle
import io.github.typesafegithub.workflows.actions.wandalen.WretryAction
import io.github.typesafegithub.workflows.annotations.ExperimentalKotlinLogicStep
import io.github.typesafegithub.workflows.domain.JobOutputs
import io.github.typesafegithub.workflows.domain.Mode
import io.github.typesafegithub.workflows.domain.Permission
import io.github.typesafegithub.workflows.domain.RunnerType
import io.github.typesafegithub.workflows.domain.actions.*
import io.github.typesafegithub.workflows.domain.triggers.PullRequest
import io.github.typesafegithub.workflows.domain.triggers.Push
import io.github.typesafegithub.workflows.dsl.JobBuilder
import io.github.typesafegithub.workflows.dsl.expressions.Contexts
import io.github.typesafegithub.workflows.dsl.expressions.expr
import io.github.typesafegithub.workflows.dsl.workflow
import io.github.typesafegithub.workflows.yaml.DEFAULT_CONSISTENCY_CHECK_JOB_CONFIG
import io.github.typesafegithub.workflows.updates.reportAvailableUpdates
import java.time.Instant

fun JobBuilder<*>.publishToMavenLocal() {
    uses(
        name = "Set up JDK",
        action = SetupJava(
            javaVersion = "11",
            distribution = SetupJava.Distribution.Zulu,
        ),
    )
    uses(action = ActionsSetupGradle())
    run(
        name = "Publish to Maven local",
        command = "./gradlew publishToMavenLocal",
    )
}

workflow(
    name = "End-to-end tests",
    on = listOf(
        Push(branches = listOf("main")),
        PullRequest(),
    ),
    consistencyCheckJobConfig = DEFAULT_CONSISTENCY_CHECK_JOB_CONFIG.copy(
        env = mapOf(
            "GITHUB_TOKEN" to expr("secrets.GITHUB_TOKEN")
        ),
        additionalSteps = {
            publishToMavenLocal()
        },
    ),
    useWorkflow = { it.reportAvailableUpdates() },
    sourceFile = __FILE__,
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
            env = mapOf(
                GREETING to "World",
            ),
            permissions = mapOf(
                Permission.Actions to Mode.Read,
                Permission.Checks to Mode.Write,
                Permission.Contents to Mode.None,
            ),
            outputs = object : JobOutputs() {
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
                command = """
                    less test.txt \
                    | grep -P "foobar" \
                    | sort \
                    > result.txt
                 """.trimIndent(),
            )

            uses(
                name = "Check out",
                action = CustomAction(
                    actionOwner = "actions",
                    actionName = "checkout",
                    actionVersion = "v4",
                ),
            )

            uses(
                name = "Run local action",
                action = CustomLocalAction(
                    actionPath = "./.github/workflows/test-local-action",
                    inputs = mapOf(
                        "name" to "Rocky",
                    ),
                ),
            )

            uses(
                name = "Run alpine",
                action = CustomDockerAction(
                    actionImage = "alpine",
                    actionTag = "latest",
                ),
            )

            uses(
                name = "Check out again",
                action = object : RegularAction<Action.Outputs>(
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
                action = object : LocalAction<Action.Outputs>(
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
                action = object : DockerAction<Action.Outputs>(
                    actionImage = "alpine",
                    actionTag = "latest",
                ) {
                    override fun toYamlArguments() = linkedMapOf<String, String>()

                    override fun buildOutputObject(stepId: String) = Action.Outputs(stepId)
                },
            )

            val addAndCommit = uses(action = SetupPython())

            uses(
                name = "Some step consuming other step's output",
                action = Checkout(
                    sshKey = expr(addAndCommit.outputs.pythonVersion),
                    path = expr(addAndCommit.outputs["my-unsafe-output"]),
                ),
            )

            run(
                name = "Custom environment variable",
                env = mapOf(
                    FIRST_NAME to "Patrick",
                ),
                // The assertion below presents the current, undesired behavior related to
                // env vars, used either from shell or GitHub Actions expressions.
                // TODO: fix in https://github.com/typesafegithub/github-workflows-kt/issues/1956
                command = """
                    cat << EOF > actual
                    $GREETING-$FIRST_NAME
                    EOF

                    cat << EOF > expected
                    -
                    EOF

                    diff actual expected
                """.trimIndent(),
            )
            run(
                name = "Encrypted secret",
                env = mapOf(
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

            publishToMavenLocal()

            run(
                name = "Step with a Kotlin-based logic",
                ifKotlin = { Instant.now() > Instant.parse("2022-03-04T12:34:56.00Z") },
            ) {
                println("Hello from Kotlin! Now it's ${Instant.now()}")
                println("Running for commit ${github.sha}, branch ${github.ref}")
            }

            val scriptStep =
                uses(
                    action = GithubScript(
                        script = """
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
            command = """
                echo ${expr { testJob1.outputs.scriptKey }}
                echo ${expr { testJob1.outputs.scriptKey2 }}
                echo ${expr { testJob1.outputs.scriptResult }}
            """.trimIndent(),
        )

        val setupJava11And20 = SetupJava(
            javaVersion = """
                11
                20
            """.trimIndent(),
            distribution = SetupJava.Distribution.Temurin,
        )

        uses(
            name = "Setup Java 11 and 20",
            action = WretryAction(
                action = setupJava11And20.usesString,
                with = setupJava11And20.yamlArgumentsString,
            )
        )
    }
}

workflow(
    name = "End-to-end tests (2nd workflow)",
    on = listOf(
        Push(branches = listOf("main")),
        PullRequest(),
    ),
    consistencyCheckJobConfig = DEFAULT_CONSISTENCY_CHECK_JOB_CONFIG.copy(
        additionalSteps = {
            publishToMavenLocal()
        },
    ),
    sourceFile = __FILE__,
    targetFileName = "end-to-end-tests-2nd-workflow.yaml",
) {
    job(
        id = "another_job",
        runsOn = RunnerType.UbuntuLatest,
    ) {
        uses(
            name = "Check out",
            action = Checkout(),
        )

        publishToMavenLocal()

        run(
            name = "Step with a Kotlin-based logic, in a different workflow",
        ) {
            println("Hello from Kotlin!")
        }
    }
}
