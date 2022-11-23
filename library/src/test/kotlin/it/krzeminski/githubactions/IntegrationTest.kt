package it.krzeminski.githubactions

import io.kotest.core.spec.style.FunSpec
import io.kotest.engine.spec.tempdir
import io.kotest.matchers.shouldBe
import it.krzeminski.githubactions.actions.actions.CheckoutV3
import it.krzeminski.githubactions.actions.actions.GithubScriptV6
import it.krzeminski.githubactions.actions.actions.SetupPythonV4
import it.krzeminski.githubactions.actions.endbug.AddAndCommitV9
import it.krzeminski.githubactions.domain.Concurrency
import it.krzeminski.githubactions.domain.JobOutputs
import it.krzeminski.githubactions.domain.RunnerType
import it.krzeminski.githubactions.domain.triggers.Push
import it.krzeminski.githubactions.dsl.WorkflowBuilder
import it.krzeminski.githubactions.dsl.expressions.Contexts
import it.krzeminski.githubactions.dsl.expressions.expr
import it.krzeminski.githubactions.dsl.workflow
import it.krzeminski.githubactions.yaml.toYaml
import it.krzeminski.githubactions.yaml.writeToFile
import java.nio.file.Path

@Suppress("LargeClass", "VariableNaming")
class IntegrationTest : FunSpec({

    val gitRootDir = tempdir().also {
        it.resolve(".git").mkdirs()
    }.toPath()
    val workflow = workflow(
        name = "Test workflow",
        on = listOf(Push()),
        sourceFile = gitRootDir.resolve(".github/workflows/some_workflow.main.kts"),
    ) {
        job(
            id = "test_job",
            name = "Test Job",
            runsOn = RunnerType.UbuntuLatest,
        ) {
            uses(CheckoutV3())
            run("echo 'hello!'")
        }
    }

    test("toYaml() - 'hello world' workflow") {
        // when
        val actualYaml = workflow.toYaml()

        // then
        actualYaml shouldBe """
            # This file was generated using Kotlin DSL (.github/workflows/some_workflow.main.kts).
            # If you want to modify the workflow, please change the Kotlin file and regenerate this YAML file.
            # Generated with https://github.com/krzema12/github-workflows-kt

            name: Test workflow
            on:
              push: {}
            jobs:
              check_yaml_consistency:
                runs-on: ubuntu-latest
                steps:
                - id: step-0
                  name: Check out
                  uses: actions/checkout@v3
                - id: step-1
                  name: Consistency check
                  run: diff -u '.github/workflows/some_workflow.yaml' <('.github/workflows/some_workflow.main.kts')
              test_job:
                name: Test Job
                runs-on: ubuntu-latest
                needs:
                - check_yaml_consistency
                steps:
                - id: step-0
                  uses: actions/checkout@v3
                - id: step-1
                  run: echo 'hello!'

        """.trimIndent()
    }

    @Suppress("MaxLineLength")
    test("writeToFile() - workflow with one job depending on another") {
        testRanWithGitHub("one job depending on another") {
            val testJob1 = job(
                id = "test_job_1",
                runsOn = RunnerType.UbuntuLatest,
            ) {
                run(
                    name = "Hello world!",
                    command = "echo 'hello!'",
                )
            }

            job(
                id = "test_job_2",
                runsOn = RunnerType.UbuntuLatest,
                needs = listOf(testJob1),
            ) {
                run(
                    name = "Hello world, again!",
                    command = "echo 'hello again!'",
                )
            }
        }
    }

    test("toYaml() - 'hello world' workflow without consistency check") {
        // when
        val actualYaml = workflow.toYaml(addConsistencyCheck = false)

        // then
        actualYaml shouldBe """
            # This file was generated using Kotlin DSL (.github/workflows/some_workflow.main.kts).
            # If you want to modify the workflow, please change the Kotlin file and regenerate this YAML file.
            # Generated with https://github.com/krzema12/github-workflows-kt

            name: Test workflow
            on:
              push: {}
            jobs:
              test_job:
                name: Test Job
                runs-on: ubuntu-latest
                steps:
                - id: step-0
                  uses: actions/checkout@v3
                - id: step-1
                  run: echo 'hello!'

        """.trimIndent()
    }

    test("writeToFile() - multiline command with pipes") {
        testRanWithGitHub("multiline command with pipes") {
            job(
                id = "test_job",
                runsOn = RunnerType.UbuntuLatest,
            ) {
                run(
                    name = "Hello world!",
                    command = """
                        less test.txt \
                        | grep -P "foobar" \
                        | sort \
                        > result.txt
                    """.trimIndent(),
                )
            }
        }
    }

    test("writeToFile() - trivial workflow") {
        testRanWithGitHub("trivial workflow") {
            job(
                id = "test_job",
                runsOn = RunnerType.UbuntuLatest,
            ) {
                uses(
                    name = "Check out",
                    action = CheckoutV3(),
                )

                run(
                    name = "Hello world!",
                    command = "echo 'hello!'",
                )
            }
        }
    }

    test("writeToFile(addConsistencyCheck = true) - 'hello world' workflow") {
        // given
        val sourceTempFile = gitRootDir.resolve(".github/workflows/some_workflow.main.kts").toFile()
        val targetTempFile = gitRootDir.resolve(".github/workflows/some_workflow.yaml").toFile()
        val workflowWithTempTargetFile = workflow(
            name = "Test workflow",
            on = listOf(Push()),
            sourceFile = sourceTempFile.toPath(),
        ) {
            job(
                id = "test_job",
                runsOn = RunnerType.UbuntuLatest,
            ) {
                uses(
                    name = "Check out",
                    action = CheckoutV3(),
                )

                run(
                    name = "Hello world!",
                    command = "echo 'hello!'",
                )
            }
        }

        // when
        workflowWithTempTargetFile.writeToFile(addConsistencyCheck = true)

        // then
        targetTempFile.readText() shouldBe """
            # This file was generated using Kotlin DSL (.github/workflows/some_workflow.main.kts).
            # If you want to modify the workflow, please change the Kotlin file and regenerate this YAML file.
            # Generated with https://github.com/krzema12/github-workflows-kt

            name: Test workflow
            on:
              push: {}
            jobs:
              check_yaml_consistency:
                runs-on: ubuntu-latest
                steps:
                - id: step-0
                  name: Check out
                  uses: actions/checkout@v3
                - id: step-1
                  name: Execute script
                  run: rm '.github/workflows/some_workflow.yaml' && '.github/workflows/some_workflow.main.kts'
                - id: step-2
                  name: Consistency check
                  run: git diff --exit-code '.github/workflows/some_workflow.yaml'
              test_job:
                runs-on: ubuntu-latest
                needs:
                - check_yaml_consistency
                steps:
                - id: step-0
                  name: Check out
                  uses: actions/checkout@v3
                - id: step-1
                  name: Hello world!
                  run: echo 'hello!'

        """.trimIndent()
    }

    test("writeToYaml() - job condition") {
        testRanWithGitHub("job condition") {
            job(
                id = "test_job",
                runsOn = RunnerType.UbuntuLatest,
                condition = "\${{ always() }}",
            ) {
                uses(
                    name = "Check out",
                    action = CheckoutV3(),
                )
            }
        }
    }

    test("toYaml() - environment variables and continueOnError") {
        // when
        val actualYaml = workflow(
            name = "Test workflow",
            on = listOf(Push()),
            env = linkedMapOf(
                "SIMPLE_VAR" to "simple-value-workflow",
                "MULTILINE_VAR" to """
                    hey,
                    hi,
                    hello! workflow
                """.trimIndent(),
            ),
            sourceFile = gitRootDir.resolve(".github/workflows/some_workflow.main.kts"),
            _customArguments = mapOf(
                "name" to "Overridden name!",
                "foo-bar" to "baz",
            ),
        ) {
            job(
                id = "test_job",
                runsOn = RunnerType.UbuntuLatest,
                condition = "\${{ always() }}",
                env = linkedMapOf(
                    "SIMPLE_VAR" to "simple-value-job",
                    "MULTILINE_VAR" to """
                        hey,
                        hi,
                        hello! job
                    """.trimIndent(),
                ),
            ) {
                uses(
                    name = "Check out",
                    action = CheckoutV3(),
                    env = linkedMapOf(
                        "SIMPLE_VAR" to "simple-value-uses",
                        "MULTILINE_VAR" to """
                            hey,
                            hi,
                            hello! uses
                        """.trimIndent(),
                    ),
                    continueOnError = true,
                )

                run(
                    name = "Hello world!",
                    command = "echo 'hello!'",
                    env = linkedMapOf(
                        "SIMPLE_VAR" to "simple-value-run",
                        "MULTILINE_VAR" to """
                            hey,
                            hi,
                            hello! run
                        """.trimIndent(),
                    ),
                    continueOnError = true,
                )
            }
        }.toYaml(addConsistencyCheck = false)

        // then
        actualYaml shouldBe """
            # This file was generated using Kotlin DSL (.github/workflows/some_workflow.main.kts).
            # If you want to modify the workflow, please change the Kotlin file and regenerate this YAML file.
            # Generated with https://github.com/krzema12/github-workflows-kt

            name: Overridden name!
            on:
              push: {}
            env:
              SIMPLE_VAR: simple-value-workflow
              MULTILINE_VAR: |-
                hey,
                hi,
                hello! workflow
            jobs:
              test_job:
                runs-on: ubuntu-latest
                env:
                  SIMPLE_VAR: simple-value-job
                  MULTILINE_VAR: |-
                    hey,
                    hi,
                    hello! job
                if: ${'$'}{{ always() }}
                steps:
                - id: step-0
                  name: Check out
                  continue-on-error: true
                  uses: actions/checkout@v3
                  env:
                    SIMPLE_VAR: simple-value-uses
                    MULTILINE_VAR: |-
                      hey,
                      hi,
                      hello! uses
                - id: step-1
                  name: Hello world!
                  env:
                    SIMPLE_VAR: simple-value-run
                    MULTILINE_VAR: |-
                      hey,
                      hi,
                      hello! run
                  continue-on-error: true
                  run: echo 'hello!'
            foo-bar: baz

        """.trimIndent()
    }

    test("writeToFile() - step with outputs") {
        testRanWithGitHub("step with outputs") {
            job(
                id = "test_job",
                runsOn = RunnerType.UbuntuLatest,
            ) {
                val addAndCommit = uses(SetupPythonV4())

                uses(
                    name = "Some step consuming other step's output",
                    action = CheckoutV3(
                        sshKey = expr(addAndCommit.outputs.pythonVersion),
                        path = expr(addAndCommit.outputs["my-unsafe-output"]),
                    ),
                )
            }
        }
    }

    test("toYaml() - with concurrency, default behavior") {
        // when
        val actualYaml = workflow(
            name = "Test workflow",
            on = listOf(Push()),
            sourceFile = gitRootDir.resolve(".github/workflows/some_workflow.main.kts"),
            concurrency = Concurrency("workflow_staging_environment"),
        ) {
            job(
                id = "test_job",
                runsOn = RunnerType.UbuntuLatest,
                concurrency = Concurrency("job_staging_environment"),
            ) {
                val addAndCommit = uses(AddAndCommitV9())

                uses(
                    name = "Some step consuming other step's output",
                    action = CheckoutV3(
                        repository = expr(addAndCommit.id),
                        ref = expr(addAndCommit.outputs.commitSha),
                        token = expr(addAndCommit.outputs["my-unsafe-output"]),
                    ),
                )
            }
        }.toYaml(addConsistencyCheck = false)

        // then
        actualYaml shouldBe """
            # This file was generated using Kotlin DSL (.github/workflows/some_workflow.main.kts).
            # If you want to modify the workflow, please change the Kotlin file and regenerate this YAML file.
            # Generated with https://github.com/krzema12/github-workflows-kt

            name: Test workflow
            on:
              push: {}
            concurrency:
              group: workflow_staging_environment
              cancel-in-progress: false
            jobs:
              test_job:
                runs-on: ubuntu-latest
                concurrency:
                  group: job_staging_environment
                  cancel-in-progress: false
                steps:
                - id: step-0
                  uses: EndBug/add-and-commit@v9
                - id: step-1
                  name: Some step consuming other step's output
                  uses: actions/checkout@v3
                  with:
                    repository: ${'$'}{{ step-0 }}
                    ref: ${'$'}{{ steps.step-0.outputs.commit_sha }}
                    token: ${'$'}{{ steps.step-0.outputs.my-unsafe-output }}

        """.trimIndent()
    }

    test("toYaml() - with concurrency, cancel in progress") {
        // when
        val actualYaml = workflow(
            name = "Test workflow",
            on = listOf(Push()),
            sourceFile = gitRootDir.resolve(".github/workflows/some_workflow.main.kts"),
            concurrency = Concurrency("workflow_staging_environment", cancelInProgress = true),
        ) {
            job(
                id = "test_job",
                runsOn = RunnerType.UbuntuLatest,
                concurrency = Concurrency("job_staging_environment", cancelInProgress = true),
            ) {
                val addAndCommit = uses(AddAndCommitV9())

                uses(
                    name = "Some step consuming other step's output",
                    action = CheckoutV3(
                        repository = expr(addAndCommit.id),
                        ref = expr(addAndCommit.outputs.commitSha),
                        token = expr(addAndCommit.outputs["my-unsafe-output"]),
                    ),
                )
            }
        }.toYaml(addConsistencyCheck = false)

        // then
        actualYaml shouldBe """
            # This file was generated using Kotlin DSL (.github/workflows/some_workflow.main.kts).
            # If you want to modify the workflow, please change the Kotlin file and regenerate this YAML file.
            # Generated with https://github.com/krzema12/github-workflows-kt

            name: Test workflow
            on:
              push: {}
            concurrency:
              group: workflow_staging_environment
              cancel-in-progress: true
            jobs:
              test_job:
                runs-on: ubuntu-latest
                concurrency:
                  group: job_staging_environment
                  cancel-in-progress: true
                steps:
                - id: step-0
                  uses: EndBug/add-and-commit@v9
                - id: step-1
                  name: Some step consuming other step's output
                  uses: actions/checkout@v3
                  with:
                    repository: ${'$'}{{ step-0 }}
                    ref: ${'$'}{{ steps.step-0.outputs.commit_sha }}
                    token: ${'$'}{{ steps.step-0.outputs.my-unsafe-output }}

        """.trimIndent()
    }

    test("writeToFile() - type-safe expressions") {
        testRanWithGitHub("type-safe expressions") {
            val GREETING by Contexts.env
            val FIRST_NAME by Contexts.env
            val SECRET by Contexts.env
            val TOKEN by Contexts.env
            val SUPER_SECRET by Contexts.secrets

            job(
                id = "job1",
                runsOn = RunnerType.UbuntuLatest,
                env = linkedMapOf(
                    GREETING to "World",
                ),
            ) {
                uses(CheckoutV3())
                run(
                    name = "Default environment variable",
                    command = "action=${Contexts.env.GITHUB_ACTION} repo=${Contexts.env.GITHUB_REPOSITORY}",
                    condition = expr { always() },
                )
                run(
                    name = "Custom environment variable",
                    env = linkedMapOf(
                        FIRST_NAME to "Patrick",
                    ),
                    command = "echo $GREETING $FIRST_NAME",
                )
                run(
                    name = "Encrypted secret",
                    env = linkedMapOf(
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
                    command = "echo " + expr { github.sha } + " event " + expr { github.eventRelease.release.url },
                )
            }
        }
    }

    test("toYaml() - YAML consistency job condition") {
        // when
        val actualYaml =
            workflow(
                name = "Test workflow",
                on = listOf(Push()),
                yamlConsistencyJobCondition = "\${{ always() }}",
                sourceFile = gitRootDir.resolve(".github/workflows/some_workflow.main.kts"),
            ) {
                job(
                    id = "test_job",
                    name = "Test Job",
                    runsOn = RunnerType.UbuntuLatest,
                ) {
                    uses(CheckoutV3())
                    run("echo 'hello!'")
                }
            }.toYaml()

        // then
        actualYaml shouldBe """
            # This file was generated using Kotlin DSL (.github/workflows/some_workflow.main.kts).
            # If you want to modify the workflow, please change the Kotlin file and regenerate this YAML file.
            # Generated with https://github.com/krzema12/github-workflows-kt

            name: Test workflow
            on:
              push: {}
            jobs:
              check_yaml_consistency:
                runs-on: ubuntu-latest
                if: ${'$'}{{ always() }}
                steps:
                - id: step-0
                  name: Check out
                  uses: actions/checkout@v3
                - id: step-1
                  name: Consistency check
                  run: diff -u '.github/workflows/some_workflow.yaml' <('.github/workflows/some_workflow.main.kts')
              test_job:
                name: Test Job
                runs-on: ubuntu-latest
                needs:
                - check_yaml_consistency
                steps:
                - id: step-0
                  uses: actions/checkout@v3
                - id: step-1
                  run: echo 'hello!'

        """.trimIndent()
    }

    test("writeToFile() - job outputs mapping") {
        testRanWithGitHub("job outputs mapping") {
            val setOutputJob = job(
                id = "set_output",
                runsOn = RunnerType.UbuntuLatest,
                outputs = object : JobOutputs() {
                    var scriptKey by output()
                    var scriptKey2 by output()
                    var scriptResult by output()
                },
            ) {
                val scriptStep = uses(
                    GithubScriptV6(
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
                id = "use_output",
                runsOn = RunnerType.UbuntuLatest,
                needs = listOf(setOutputJob),
            ) {
                run(
                    name = "use output of script",
                    command = """
                        echo ${expr { setOutputJob.outputs.scriptKey }}
                        echo ${expr { setOutputJob.outputs.scriptKey2 }}
                        echo ${expr { setOutputJob.outputs.scriptResult }}
                    """.trimIndent(),
                )
            }
        }
    }
},)

private fun testRanWithGitHub(
    name: String,
    workflow: WorkflowBuilder.() -> Unit,
) {
    // given
    val fileName = "Integration tests - $name"
    val trivialWorkflow = workflow(
        name = "Integration tests - $name",
        on = listOf(Push()),
        sourceFile = Path.of("../.github/workflows/$fileName.main.kts"),
    ) {
        workflow()
    }
    val targetPath = Path.of("../.github/workflows/$fileName.yaml")
    val expectedYaml = targetPath.toFile().readText()

    // when
    trivialWorkflow.writeToFile(addConsistencyCheck = false)

    // then
    val actualYaml = targetPath.toFile().readText()
    actualYaml shouldBe expectedYaml
}
