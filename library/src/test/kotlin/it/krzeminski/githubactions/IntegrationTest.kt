package it.krzeminski.githubactions

import com.charleskorn.kaml.MalformedYamlException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.engine.spec.tempdir
import io.kotest.matchers.shouldBe
import it.krzeminski.githubactions.actions.actions.CheckoutV3
import it.krzeminski.githubactions.actions.actions.SetupNodeV3
import it.krzeminski.githubactions.actions.endbug.AddAndCommitV9
import it.krzeminski.githubactions.domain.Concurrency
import it.krzeminski.githubactions.domain.RunnerType
import it.krzeminski.githubactions.domain.triggers.Push
import it.krzeminski.githubactions.dsl.expr
import it.krzeminski.githubactions.dsl.workflow
import it.krzeminski.githubactions.expr.Env
import it.krzeminski.githubactions.expr.Expr
import it.krzeminski.githubactions.expr.Secrets
import it.krzeminski.githubactions.yaml.toYaml
import it.krzeminski.githubactions.yaml.writeToFile
import java.nio.file.Path
import java.nio.file.Paths

@Suppress("LargeClass")
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
            # Generated with https://github.com/krzema12/github-actions-kotlin-dsl

            name: Test workflow

            on:
              push:

            jobs:
              "check_yaml_consistency":
                runs-on: "ubuntu-latest"
                steps:
                  - id: step-0
                    name: Check out
                    uses: actions/checkout@v3
                  - id: step-1
                    name: Consistency check
                    run: diff -u '.github/workflows/some_workflow.yaml' <('.github/workflows/some_workflow.main.kts')
              "test_job":
                name: Test Job
                runs-on: "ubuntu-latest"
                needs:
                  - "check_yaml_consistency"
                steps:
                  - id: step-0
                    uses: actions/checkout@v3
                  - id: step-1
                    run: echo 'hello!'

        """.trimIndent()
    }

    @Suppress("MaxLineLength")
    test("toYaml() - workflow with one job depending on another") {
        // given
        val workflowWithDependency = workflow(
            name = "Test workflow",
            on = listOf(Push()),
            sourceFile = gitRootDir.resolve(".github/workflows/some_workflow.main.kts"),
            targetFileName = "some_workflow_with_dependency.yaml"
        ) {
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

        // when
        val actualYaml = workflowWithDependency.toYaml()

        // then
        actualYaml shouldBe """
            # This file was generated using Kotlin DSL (.github/workflows/some_workflow.main.kts).
            # If you want to modify the workflow, please change the Kotlin file and regenerate this YAML file.
            # Generated with https://github.com/krzema12/github-actions-kotlin-dsl

            name: Test workflow

            on:
              push:

            jobs:
              "check_yaml_consistency":
                runs-on: "ubuntu-latest"
                steps:
                  - id: step-0
                    name: Check out
                    uses: actions/checkout@v3
                  - id: step-1
                    name: Consistency check
                    run: diff -u '.github/workflows/some_workflow_with_dependency.yaml' <('.github/workflows/some_workflow.main.kts')
              "test_job_1":
                runs-on: "ubuntu-latest"
                needs:
                  - "check_yaml_consistency"
                steps:
                  - id: step-0
                    name: Hello world!
                    run: echo 'hello!'
              "test_job_2":
                runs-on: "ubuntu-latest"
                needs:
                  - "test_job_1"
                  - "check_yaml_consistency"
                steps:
                  - id: step-0
                    name: Hello world, again!
                    run: echo 'hello again!'

        """.trimIndent()
    }

    test("toYaml() - 'hello world' workflow without consistency check") {
        // when
        val actualYaml = workflow.toYaml(addConsistencyCheck = false)

        // then
        actualYaml shouldBe """
            # This file was generated using Kotlin DSL (.github/workflows/some_workflow.main.kts).
            # If you want to modify the workflow, please change the Kotlin file and regenerate this YAML file.
            # Generated with https://github.com/krzema12/github-actions-kotlin-dsl

            name: Test workflow

            on:
              push:

            jobs:
              "test_job":
                name: Test Job
                runs-on: "ubuntu-latest"
                steps:
                  - id: step-0
                    uses: actions/checkout@v3
                  - id: step-1
                    run: echo 'hello!'

        """.trimIndent()
    }

    test("toYaml() - multiline command with pipes") {
        // given
        val workflowWithMultilineCommand = workflow(
            name = "Test workflow",
            on = listOf(Push()),
            sourceFile = gitRootDir.resolve(".github/workflows/some_workflow.main.kts"),
        ) {
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

        // when
        val actualYaml = workflowWithMultilineCommand.toYaml(addConsistencyCheck = false)

        // then
        actualYaml shouldBe """
            # This file was generated using Kotlin DSL (.github/workflows/some_workflow.main.kts).
            # If you want to modify the workflow, please change the Kotlin file and regenerate this YAML file.
            # Generated with https://github.com/krzema12/github-actions-kotlin-dsl

            name: Test workflow

            on:
              push:

            jobs:
              "test_job":
                runs-on: "ubuntu-latest"
                steps:
                  - id: step-0
                    name: Hello world!
                    run: |
                      less test.txt \
                      | grep -P "foobar" \
                      | sort \
                      > result.txt

        """.trimIndent()
    }

    test("writeToFile() - 'hello world' workflow") {
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
        workflowWithTempTargetFile.writeToFile(addConsistencyCheck = false, gitRootDir = gitRootDir)

        // then
        targetTempFile.readText() shouldBe """
            # This file was generated using Kotlin DSL (.github/workflows/some_workflow.main.kts).
            # If you want to modify the workflow, please change the Kotlin file and regenerate this YAML file.
            # Generated with https://github.com/krzema12/github-actions-kotlin-dsl

            name: Test workflow

            on:
              push:

            jobs:
              "test_job":
                runs-on: "ubuntu-latest"
                steps:
                  - id: step-0
                    name: Check out
                    uses: actions/checkout@v3
                  - id: step-1
                    name: Hello world!
                    run: echo 'hello!'

        """.trimIndent()
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
            # Generated with https://github.com/krzema12/github-actions-kotlin-dsl

            name: Test workflow

            on:
              push:

            jobs:
              "check_yaml_consistency":
                runs-on: "ubuntu-latest"
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
              "test_job":
                runs-on: "ubuntu-latest"
                needs:
                  - "check_yaml_consistency"
                steps:
                  - id: step-0
                    name: Check out
                    uses: actions/checkout@v3
                  - id: step-1
                    name: Hello world!
                    run: echo 'hello!'

        """.trimIndent()
    }

    test("toYaml() - job condition") {
        // when
        val actualYaml = workflow(
            name = "Test workflow",
            on = listOf(Push()),
            sourceFile = gitRootDir.resolve(".github/workflows/some_workflow.main.kts"),
        ) {
            job(
                id = "test_job",
                runsOn = RunnerType.UbuntuLatest,
                condition = "\${{ always() }}"
            ) {
                uses(
                    name = "Check out",
                    action = CheckoutV3(),
                )
            }
        }.toYaml(addConsistencyCheck = false)

        // then
        actualYaml shouldBe """
            # This file was generated using Kotlin DSL (.github/workflows/some_workflow.main.kts).
            # If you want to modify the workflow, please change the Kotlin file and regenerate this YAML file.
            # Generated with https://github.com/krzema12/github-actions-kotlin-dsl

            name: Test workflow

            on:
              push:

            jobs:
              "test_job":
                runs-on: "ubuntu-latest"
                if: ${'$'}{{ always() }}
                steps:
                  - id: step-0
                    name: Check out
                    uses: actions/checkout@v3

        """.trimIndent()
    }

    test("toYaml() - environment variables") {
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
                """.trimIndent()
            ),
            sourceFile = gitRootDir.resolve(".github/workflows/some_workflow.main.kts"),
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
                    """.trimIndent()
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
                        """.trimIndent()
                    ),
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
                        """.trimIndent()
                    ),
                )
            }
        }.toYaml(addConsistencyCheck = false)

        // then
        actualYaml shouldBe """
            # This file was generated using Kotlin DSL (.github/workflows/some_workflow.main.kts).
            # If you want to modify the workflow, please change the Kotlin file and regenerate this YAML file.
            # Generated with https://github.com/krzema12/github-actions-kotlin-dsl

            name: Test workflow

            on:
              push:

            env:
              SIMPLE_VAR: simple-value-workflow
              MULTILINE_VAR: |
                hey,
                hi,
                hello! workflow

            jobs:
              "test_job":
                runs-on: "ubuntu-latest"
                env:
                  SIMPLE_VAR: simple-value-job
                  MULTILINE_VAR: |
                    hey,
                    hi,
                    hello! job
                if: ${'$'}{{ always() }}
                steps:
                  - id: step-0
                    name: Check out
                    uses: actions/checkout@v3
                    env:
                      SIMPLE_VAR: simple-value-uses
                      MULTILINE_VAR: |
                        hey,
                        hi,
                        hello! uses
                  - id: step-1
                    name: Hello world!
                    env:
                      SIMPLE_VAR: simple-value-run
                      MULTILINE_VAR: |
                        hey,
                        hi,
                        hello! run
                    run: echo 'hello!'

        """.trimIndent()
    }

    test("toYaml() - step with outputs") {
        // when
        val actualYaml = workflow(
            name = "Test workflow",
            on = listOf(Push()),
            sourceFile = gitRootDir.resolve(".github/workflows/some_workflow.main.kts"),
        ) {
            job(
                id = "test_job",
                runsOn = RunnerType.UbuntuLatest,
            ) {
                val addAndCommit = uses(AddAndCommitV9())

                uses(
                    name = "Some step consuming other step's output",
                    action = CheckoutV3(
                        repository = expr(addAndCommit.id),
                        ref = expr(addAndCommit.outputs.commitSha),
                        token = expr(addAndCommit.outputs["my-unsafe-output"]),
                    )
                )
            }
        }.toYaml(addConsistencyCheck = false)

        // then
        actualYaml shouldBe """
            # This file was generated using Kotlin DSL (.github/workflows/some_workflow.main.kts).
            # If you want to modify the workflow, please change the Kotlin file and regenerate this YAML file.
            # Generated with https://github.com/krzema12/github-actions-kotlin-dsl

            name: Test workflow

            on:
              push:

            jobs:
              "test_job":
                runs-on: "ubuntu-latest"
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
                    )
                )
            }
        }.toYaml(addConsistencyCheck = false)

        // then
        actualYaml shouldBe """
            # This file was generated using Kotlin DSL (.github/workflows/some_workflow.main.kts).
            # If you want to modify the workflow, please change the Kotlin file and regenerate this YAML file.
            # Generated with https://github.com/krzema12/github-actions-kotlin-dsl

            name: Test workflow

            on:
              push:

            concurrency:
              group: workflow_staging_environment
              cancel-in-progress: false

            jobs:
              "test_job":
                runs-on: "ubuntu-latest"
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
                    )
                )
            }
        }.toYaml(addConsistencyCheck = false)

        // then
        actualYaml shouldBe """
            # This file was generated using Kotlin DSL (.github/workflows/some_workflow.main.kts).
            # If you want to modify the workflow, please change the Kotlin file and regenerate this YAML file.
            # Generated with https://github.com/krzema12/github-actions-kotlin-dsl

            name: Test workflow

            on:
              push:

            concurrency:
              group: workflow_staging_environment
              cancel-in-progress: true

            jobs:
              "test_job":
                runs-on: "ubuntu-latest"
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

    test("Malformed YAML") {

        val invalidWorkflow = workflow(
            name = "Test workflow",
            on = listOf(Push()),
            sourceFile = Paths.get("../.github/workflows/invalid_workflow.main.kts"),
        ) {
            job("test_job", runsOn = RunnerType.UbuntuLatest) {
                run(name = "property: something", command = "echo hello")
            }
        }
        shouldThrow<MalformedYamlException> {
            invalidWorkflow.toYaml()
        }.message shouldBe """
            |mapping values are not allowed here (is the indentation level of this line or a line nearby incorrect?)
            | at line 26, column 23:
            |            name: property: something
            |                          ^
        """.trimMargin()
    }

    test("Executing workflow with type-safe expressions") {
        @Suppress("VariableNaming")
        val actualYaml = workflow(
            name = "expr-typesafe",
            on = listOf(Push()),
            sourceFile = Path.of("ExprIntegrationTest.kt"),
        ) {
            val NODE by Expr.matrix.map
            val GREETING by Expr.env.map
            val FIRST_NAME by Expr.env.map
            val SECRET by Expr.env.map
            val TOKEN by Expr.env.map
            val SUPER_SECRET by Expr.secrets.map

            job(
                id = "job1",
                runsOn = RunnerType.UbuntuLatest,
                strategyMatrix = mapOf(
                    "OS" to listOf("ubuntu-latest", "windows-latest"),
                    NODE to listOf("14", "16"),
                ),
                env = linkedMapOf(
                    GREETING to "World",
                )
            ) {
                uses(CheckoutV3())
                run(
                    name = "Default environment variable",
                    command = "action=${Env.GITHUB_ACTION} repo=${Env.GITHUB_REPOSITORY}",
                    condition = expr { always() }
                )
                run(
                    name = "Custom environment variable",
                    env = linkedMapOf(
                        FIRST_NAME to "Patrick",
                    ),
                    command = "echo " + expr { GREETING } + " " + expr { FIRST_NAME }
                )
                run(
                    name = "Encrypted secret",
                    env = linkedMapOf(
                        SECRET to expr { SUPER_SECRET },
                        TOKEN to expr { Secrets.GITHUB_TOKEN }
                    ),
                    command = "echo secret=$SECRET token=$TOKEN"
                )
                uses(
                    name = "MatrixContext node",
                    SetupNodeV3(nodeVersion = expr { NODE })
                )
                run(
                    name = "RunnerContext create temp directory",
                    command = "mkdir " + expr { runner.temp } + "/build_logs"
                )
                run(
                    name = "GitHubContext echo sha",
                    command = "echo " + expr { github.sha }
                )
                run(
                    name = "StrategyContext job-index",
                    command = "npm test > test-job-" + expr { strategy.`job-index` } + ".txt"
                )
            }
        }.toYaml(addConsistencyCheck = false)

        val d = '$'.toString() // Escape dollar signs

        actualYaml shouldBe """
            # This file was generated using Kotlin DSL (library/ExprIntegrationTest.kt).
            # If you want to modify the workflow, please change the Kotlin file and regenerate this YAML file.
            # Generated with https://github.com/krzema12/github-actions-kotlin-dsl

            name: expr-typesafe

            on:
              push:

            jobs:
              "job1":
                runs-on: "ubuntu-latest"
                env:
                  GREETING: World
                strategy:
                  matrix:
                    OS:
                      - ubuntu-latest
                      - windows-latest
                    NODE:
                      - 14
                      - 16
                steps:
                  - id: step-0
                    uses: actions/checkout@v3
                  - id: step-1
                    name: Default environment variable
                    run: action=${d}GITHUB_ACTION repo=${d}GITHUB_REPOSITORY
                    if: $d{{ always() }}
                  - id: step-2
                    name: Custom environment variable
                    env:
                      FIRST_NAME: Patrick
                    run: echo $d{{ ${d}GREETING }} $d{{ ${d}FIRST_NAME }}
                  - id: step-3
                    name: Encrypted secret
                    env:
                      SECRET: $d{{ secrets.SUPER_SECRET }}
                      TOKEN: $d{{ secrets.GITHUB_TOKEN }}
                    run: echo secret=${d}SECRET token=${d}TOKEN
                  - id: step-4
                    name: MatrixContext node
                    uses: actions/setup-node@v3
                    with:
                      node-version: $d{{ matrix.NODE }}
                  - id: step-5
                    name: RunnerContext create temp directory
                    run: mkdir $d{{ runner.temp }}/build_logs
                  - id: step-6
                    name: GitHubContext echo sha
                    run: echo $d{{ github.sha }}
                  - id: step-7
                    name: StrategyContext job-index
                    run: npm test > test-job-$d{{ strategy.job-index }}.txt

        """.trimIndent()
    }
})
