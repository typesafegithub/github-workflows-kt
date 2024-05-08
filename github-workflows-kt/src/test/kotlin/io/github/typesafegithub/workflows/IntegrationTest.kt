package io.github.typesafegithub.workflows

import io.github.typesafegithub.workflows.actions.actions.CheckoutV4
import io.github.typesafegithub.workflows.actions.awsactions.ConfigureAwsCredentialsV4
import io.github.typesafegithub.workflows.actions.endbug.AddAndCommitV9
import io.github.typesafegithub.workflows.annotations.ExperimentalKotlinLogicStep
import io.github.typesafegithub.workflows.domain.Concurrency
import io.github.typesafegithub.workflows.domain.RunnerType
import io.github.typesafegithub.workflows.domain.triggers.Push
import io.github.typesafegithub.workflows.dsl.expressions.expr
import io.github.typesafegithub.workflows.dsl.workflow
import io.github.typesafegithub.workflows.yaml.Preamble.Just
import io.github.typesafegithub.workflows.yaml.Preamble.WithOriginalAfter
import io.github.typesafegithub.workflows.yaml.Preamble.WithOriginalBefore
import io.github.typesafegithub.workflows.yaml.toYaml
import io.github.typesafegithub.workflows.yaml.writeToFile
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.engine.spec.tempdir
import io.kotest.matchers.shouldBe

@OptIn(ExperimentalKotlinLogicStep::class)
@Suppress("LargeClass")
class IntegrationTest : FunSpec({

    val gitRootDir =
        tempdir().also {
            it.resolve(".git").mkdirs()
        }.toPath()
    val workflow =
        workflow(
            name = "Test workflow",
            on = listOf(Push()),
            sourceFile = gitRootDir.resolve(".github/workflows/some_workflow.main.kts"),
        ) {
            job(
                id = "test_job",
                name = "Test Job",
                runsOn = RunnerType.UbuntuLatest,
            ) {
                uses(action = CheckoutV4())
                run(command = "echo 'hello!'")
            }
        }

    test("writeToFile() - 'hello world' workflow") {
        // given
        val sourceTempFile = gitRootDir.resolve(".github/workflows/some_workflow.main.kts").toFile()
        val targetTempFile = gitRootDir.resolve(".github/workflows/some_workflow.yaml").toFile()
        val workflowWithTempTargetFile =
            workflow(
                name = "Test workflow",
                on = listOf(Push()),
                sourceFile = sourceTempFile.toPath(),
                yamlConsistencyJobEnv = mapOf("GITHUB_TOKEN" to expr("secrets.GITHUB_TOKEN")),
            ) {
                job(
                    id = "test_job",
                    runsOn = RunnerType.UbuntuLatest,
                ) {
                    uses(
                        name = "Check out",
                        action = CheckoutV4(),
                    )

                    run(
                        name = "Hello world!",
                        command = "echo 'hello!'",
                    )
                }
            }

        // when
        workflowWithTempTargetFile.writeToFile()

        // then
        targetTempFile.readText() shouldBe
            """
            # This file was generated using Kotlin DSL (.github/workflows/some_workflow.main.kts).
            # If you want to modify the workflow, please change the Kotlin file and regenerate this YAML file.
            # Generated with https://github.com/typesafegithub/github-workflows-kt

            name: 'Test workflow'
            on:
              push: {}
            jobs:
              check_yaml_consistency:
                name: 'Check YAML consistency'
                runs-on: 'ubuntu-latest'
                env:
                  GITHUB_TOKEN: '${'$'}{{ secrets.GITHUB_TOKEN }}'
                steps:
                - id: 'step-0'
                  name: 'Check out'
                  uses: 'actions/checkout@v4'
                - id: 'step-1'
                  name: 'Execute script'
                  run: 'rm ''.github/workflows/some_workflow.yaml'' && ''.github/workflows/some_workflow.main.kts'''
                - id: 'step-2'
                  name: 'Consistency check'
                  run: 'git diff --exit-code ''.github/workflows/some_workflow.yaml'''
              test_job:
                runs-on: 'ubuntu-latest'
                needs:
                - 'check_yaml_consistency'
                steps:
                - id: 'step-0'
                  name: 'Check out'
                  uses: 'actions/checkout@v4'
                - id: 'step-1'
                  name: 'Hello world!'
                  run: 'echo ''hello!'''

            """.trimIndent()
    }

    test("writeToFile() - environment variables and continueOnError") {
        // when
        val sourceTempFile = gitRootDir.resolve(".github/workflows/some_workflow.main.kts").toFile()
        val targetTempFile = gitRootDir.resolve(".github/workflows/some_workflow.yaml").toFile()
        val testWorkflow =
            workflow(
                name = "Test workflow",
                on = listOf(Push()),
                env =
                    mapOf(
                        "SIMPLE_VAR" to "simple-value-workflow",
                        "MULTILINE_VAR" to
                            """
                            hey,
                            hi,
                            hello! workflow
                            """.trimIndent(),
                    ),
                sourceFile = sourceTempFile.toPath(),
                _customArguments =
                    mapOf(
                        "name" to "Overridden name!",
                        "foo-bar" to "baz",
                    ),
            ) {
                job(
                    id = "test_job",
                    runsOn = RunnerType.UbuntuLatest,
                    condition = "\${{ always() }}",
                    env =
                        mapOf(
                            "SIMPLE_VAR" to "simple-value-job",
                            "MULTILINE_VAR" to
                                """
                                hey,
                                hi,
                                hello! job
                                """.trimIndent(),
                        ),
                    _customArguments =
                        mapOf(
                            "baz-goo" to 123,
                            "null-string" to "null",
                            "null-value" to null,
                            "empty-string" to "",
                        ),
                ) {
                    uses(
                        name = "Check out",
                        action = CheckoutV4(),
                        env =
                            mapOf(
                                "SIMPLE_VAR" to "simple-value-uses",
                                "MULTILINE_VAR" to
                                    """
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
                        env =
                            mapOf(
                                "SIMPLE_VAR" to "simple-value-run",
                                "MULTILINE_VAR" to
                                    """
                                    hey,
                                    hi,
                                    hello! run
                                    """.trimIndent(),
                            ),
                        continueOnError = true,
                    )
                }
            }

        // when
        testWorkflow.writeToFile(addConsistencyCheck = false)

        // then
        targetTempFile.readText() shouldBe
            """
            # This file was generated using Kotlin DSL (.github/workflows/some_workflow.main.kts).
            # If you want to modify the workflow, please change the Kotlin file and regenerate this YAML file.
            # Generated with https://github.com/typesafegithub/github-workflows-kt

            name: 'Overridden name!'
            on:
              push: {}
            env:
              SIMPLE_VAR: 'simple-value-workflow'
              MULTILINE_VAR: |-
                hey,
                hi,
                hello! workflow
            foo-bar: 'baz'
            jobs:
              test_job:
                runs-on: 'ubuntu-latest'
                env:
                  SIMPLE_VAR: 'simple-value-job'
                  MULTILINE_VAR: |-
                    hey,
                    hi,
                    hello! job
                if: '${'$'}{{ always() }}'
                baz-goo: 123
                null-string: 'null'
                null-value: null
                empty-string: ''
                steps:
                - id: 'step-0'
                  name: 'Check out'
                  continue-on-error: true
                  uses: 'actions/checkout@v4'
                  env:
                    SIMPLE_VAR: 'simple-value-uses'
                    MULTILINE_VAR: |-
                      hey,
                      hi,
                      hello! uses
                - id: 'step-1'
                  name: 'Hello world!'
                  env:
                    SIMPLE_VAR: 'simple-value-run'
                    MULTILINE_VAR: |-
                      hey,
                      hi,
                      hello! run
                  continue-on-error: true
                  run: 'echo ''hello!'''

            """.trimIndent()
    }

    test("toYaml() - with concurrency, default behavior") {
        // when
        val actualYaml =
            workflow(
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
                    val addAndCommit = uses(action = AddAndCommitV9())

                    uses(
                        name = "Some step consuming other step's output",
                        action =
                            CheckoutV4(
                                repository = expr(addAndCommit.id),
                                ref = expr(addAndCommit.outputs.commitSha),
                                token = expr(addAndCommit.outputs["my-unsafe-output"]),
                            ),
                    )
                }
            }.toYaml(addConsistencyCheck = false)

        // then
        actualYaml shouldBe
            """
            # This file was generated using Kotlin DSL (.github/workflows/some_workflow.main.kts).
            # If you want to modify the workflow, please change the Kotlin file and regenerate this YAML file.
            # Generated with https://github.com/typesafegithub/github-workflows-kt

            name: 'Test workflow'
            on:
              push: {}
            concurrency:
              group: 'workflow_staging_environment'
              cancel-in-progress: false
            jobs:
              test_job:
                runs-on: 'ubuntu-latest'
                concurrency:
                  group: 'job_staging_environment'
                  cancel-in-progress: false
                steps:
                - id: 'step-0'
                  uses: 'EndBug/add-and-commit@v9'
                - id: 'step-1'
                  name: 'Some step consuming other step''s output'
                  uses: 'actions/checkout@v4'
                  with:
                    repository: '${'$'}{{ step-0 }}'
                    ref: '${'$'}{{ steps.step-0.outputs.commit_sha }}'
                    token: '${'$'}{{ steps.step-0.outputs.my-unsafe-output }}'

            """.trimIndent()
    }

    @Suppress("MaxLineLength")
    test("toYaml() - long strings with GitHub expressions in action arguments") {
        // when
        val actualYaml =
            workflow(
                name = "Test workflow",
                on = listOf(Push()),
                sourceFile = gitRootDir.resolve(".github/workflows/some_workflow.main.kts"),
            ) {
                job(id = "deploy-dev", runsOn = RunnerType.UbuntuLatest) {
                    uses(
                        action =
                            ConfigureAwsCredentialsV4(
                                roleToAssume = "arn:aws:iam::${"1234567890".repeat(2)}:role/github-actions-role/${"1234567890".repeat(3)}",
                                awsRegion = "us-west-1",
                            ),
                    )
                    uses(
                        action =
                            ConfigureAwsCredentialsV4(
                                roleToAssume = "arn:aws:iam::${"1234567890".repeat(0)}:role/github-actions-role/${expr { github.token }}",
                                awsRegion = "us-west-1",
                            ),
                    )
                    uses(
                        action =
                            ConfigureAwsCredentialsV4(
                                roleToAssume = "arn:aws:iam::${"1234567890".repeat(1)}:role/github-actions-role/${expr { github.token }}",
                                awsRegion = "us-west-1",
                            ),
                    )
                    uses(
                        action =
                            ConfigureAwsCredentialsV4(
                                roleToAssume = "arn:aws:iam::${"1234567890".repeat(2)}:role/github-actions-role/${expr { github.token }}",
                                awsRegion = "us-west-1",
                            ),
                    )
                }
            }.toYaml(addConsistencyCheck = false)

        // then
        actualYaml shouldBe
            """
            # This file was generated using Kotlin DSL (.github/workflows/some_workflow.main.kts).
            # If you want to modify the workflow, please change the Kotlin file and regenerate this YAML file.
            # Generated with https://github.com/typesafegithub/github-workflows-kt

            name: 'Test workflow'
            on:
              push: {}
            jobs:
              deploy-dev:
                runs-on: 'ubuntu-latest'
                steps:
                - id: 'step-0'
                  uses: 'aws-actions/configure-aws-credentials@v4'
                  with:
                    aws-region: 'us-west-1'
                    role-to-assume: 'arn:aws:iam::12345678901234567890:role/github-actions-role/123456789012345678901234567890'
                - id: 'step-1'
                  uses: 'aws-actions/configure-aws-credentials@v4'
                  with:
                    aws-region: 'us-west-1'
                    role-to-assume: 'arn:aws:iam:::role/github-actions-role/${'$'}{{ github.token }}'
                - id: 'step-2'
                  uses: 'aws-actions/configure-aws-credentials@v4'
                  with:
                    aws-region: 'us-west-1'
                    role-to-assume: 'arn:aws:iam::1234567890:role/github-actions-role/${'$'}{{ github.token }}'
                - id: 'step-3'
                  uses: 'aws-actions/configure-aws-credentials@v4'
                  with:
                    aws-region: 'us-west-1'
                    role-to-assume: 'arn:aws:iam::12345678901234567890:role/github-actions-role/${'$'}{{ github.token }}'

            """.trimIndent()
    }

    test("toYaml() - with concurrency, cancel in progress") {
        // when
        val actualYaml =
            workflow(
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
                    val addAndCommit = uses(action = AddAndCommitV9())

                    uses(
                        name = "Some step consuming other step's output",
                        action =
                            CheckoutV4(
                                repository = expr(addAndCommit.id),
                                ref = expr(addAndCommit.outputs.commitSha),
                                token = expr(addAndCommit.outputs["my-unsafe-output"]),
                            ),
                    )
                }
            }.toYaml(addConsistencyCheck = false)

        // then
        actualYaml shouldBe
            """
            # This file was generated using Kotlin DSL (.github/workflows/some_workflow.main.kts).
            # If you want to modify the workflow, please change the Kotlin file and regenerate this YAML file.
            # Generated with https://github.com/typesafegithub/github-workflows-kt

            name: 'Test workflow'
            on:
              push: {}
            concurrency:
              group: 'workflow_staging_environment'
              cancel-in-progress: true
            jobs:
              test_job:
                runs-on: 'ubuntu-latest'
                concurrency:
                  group: 'job_staging_environment'
                  cancel-in-progress: true
                steps:
                - id: 'step-0'
                  uses: 'EndBug/add-and-commit@v9'
                - id: 'step-1'
                  name: 'Some step consuming other step''s output'
                  uses: 'actions/checkout@v4'
                  with:
                    repository: '${'$'}{{ step-0 }}'
                    ref: '${'$'}{{ steps.step-0.outputs.commit_sha }}'
                    token: '${'$'}{{ steps.step-0.outputs.my-unsafe-output }}'

            """.trimIndent()
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
                    uses(action = CheckoutV4())
                    run(command = "echo 'hello!'")
                }
            }.toYaml()

        // then
        actualYaml shouldBe
            """
            # This file was generated using Kotlin DSL (.github/workflows/some_workflow.main.kts).
            # If you want to modify the workflow, please change the Kotlin file and regenerate this YAML file.
            # Generated with https://github.com/typesafegithub/github-workflows-kt

            name: 'Test workflow'
            on:
              push: {}
            jobs:
              check_yaml_consistency:
                name: 'Check YAML consistency'
                runs-on: 'ubuntu-latest'
                if: '${'$'}{{ always() }}'
                steps:
                - id: 'step-0'
                  name: 'Check out'
                  uses: 'actions/checkout@v4'
                - id: 'step-1'
                  name: 'Consistency check'
                  run: 'diff -u ''.github/workflows/some_workflow.yaml'' <(''.github/workflows/some_workflow.main.kts'')'
              test_job:
                name: 'Test Job'
                runs-on: 'ubuntu-latest'
                needs:
                - 'check_yaml_consistency'
                steps:
                - id: 'step-0'
                  uses: 'actions/checkout@v4'
                - id: 'step-1'
                  run: 'echo ''hello!'''

            """.trimIndent()
    }

    test("writeToFile() - works without `sourceFile`") {
        // given
        val targetTempFile = gitRootDir.resolve(".github/workflows/some_workflow.yaml").toFile()
        val workflowWithTempTargetFile =
            workflow(
                name = "Test workflow",
                on = listOf(Push()),
                targetFileName = "some_workflow.yaml",
            ) {
                job(
                    id = "test_job",
                    runsOn = RunnerType.UbuntuLatest,
                ) {
                    uses(
                        name = "Check out",
                        action = CheckoutV4(),
                    )

                    run(
                        name = "Hello world!",
                        command = "echo 'hello!'",
                    )
                }
            }

        // when
        workflowWithTempTargetFile.writeToFile(gitRootDir = gitRootDir)

        // then
        targetTempFile.readText() shouldBe
            """
            # This file was generated using a Kotlin DSL.
            # If you want to modify the workflow, please change the Kotlin source and regenerate this YAML file.
            # Generated with https://github.com/typesafegithub/github-workflows-kt

            name: 'Test workflow'
            on:
              push: {}
            jobs:
              test_job:
                runs-on: 'ubuntu-latest'
                steps:
                - id: 'step-0'
                  name: 'Check out'
                  uses: 'actions/checkout@v4'
                - id: 'step-1'
                  name: 'Hello world!'
                  run: 'echo ''hello!'''

            """.trimIndent()
    }

    val workflowWithoutSource =
        workflow(
            name = "test",
            on = listOf(Push()),
        ) {
            job(id = "test", runsOn = RunnerType.UbuntuLatest) {
                run(command = "echo 'Hello!'")
            }
        }

    test("toYaml() - should succeed without sourceFile") {
        val yaml = workflowWithoutSource.toYaml()

        yaml shouldBe
            """
            # This file was generated using a Kotlin DSL.
            # If you want to modify the workflow, please change the Kotlin source and regenerate this YAML file.
            # Generated with https://github.com/typesafegithub/github-workflows-kt

            name: 'test'
            on:
              push: {}
            jobs:
              test:
                runs-on: 'ubuntu-latest'
                steps:
                - id: 'step-0'
                  run: 'echo ''Hello!'''

            """.trimIndent()
    }

    test("toYaml() - should fail to addConsistencyCheck when sourceFile is absent") {
        shouldThrow<IllegalStateException> {
            workflowWithoutSource.toYaml(addConsistencyCheck = true)
        }
    }

    test("toYaml() - custom preamble") {
        val yaml =
            workflowWithoutSource.toYaml(
                preamble =
                    Just(
                        """
                        Test preamble
                        with a second line
                        """.trimIndent(),
                    ),
            )

        yaml shouldBe
            """
            # Test preamble
            # with a second line

            name: 'test'
            on:
              push: {}
            jobs:
              test:
                runs-on: 'ubuntu-latest'
                steps:
                - id: 'step-0'
                  run: 'echo ''Hello!'''

            """.trimIndent()
    }

    test("toYaml() - custom preamble with empty line") {
        val yaml =
            workflowWithoutSource.toYaml(
                preamble =
                    Just(
                        """
                        Test preamble

                        with an empty line
                        """.trimIndent(),
                    ),
            )

        yaml shouldBe
            """
            # Test preamble
            #
            # with an empty line

            name: 'test'
            on:
              push: {}
            jobs:
              test:
                runs-on: 'ubuntu-latest'
                steps:
                - id: 'step-0'
                  run: 'echo ''Hello!'''

            """.trimIndent()
    }

    test("toYaml() - custom preamble with original after") {
        val yaml =
            workflow.toYaml(
                addConsistencyCheck = false,
                preamble =
                    WithOriginalAfter(
                        """
                        Test preamble
                        with original after
                        """.trimIndent(),
                    ),
            )

        yaml shouldBe
            """
            # Test preamble
            # with original after

            # This file was generated using Kotlin DSL (.github/workflows/some_workflow.main.kts).
            # If you want to modify the workflow, please change the Kotlin file and regenerate this YAML file.
            # Generated with https://github.com/typesafegithub/github-workflows-kt

            name: 'Test workflow'
            on:
              push: {}
            jobs:
              test_job:
                name: 'Test Job'
                runs-on: 'ubuntu-latest'
                steps:
                - id: 'step-0'
                  uses: 'actions/checkout@v4'
                - id: 'step-1'
                  run: 'echo ''hello!'''

            """.trimIndent()
    }

    test("toYaml() - custom preamble with original after without source") {
        val yaml =
            workflowWithoutSource.toYaml(
                preamble =
                    WithOriginalAfter(
                        """
                        Test preamble
                        with original after
                        """.trimIndent(),
                    ),
            )

        yaml shouldBe
            """
            # Test preamble
            # with original after

            # This file was generated using a Kotlin DSL.
            # If you want to modify the workflow, please change the Kotlin source and regenerate this YAML file.
            # Generated with https://github.com/typesafegithub/github-workflows-kt

            name: 'test'
            on:
              push: {}
            jobs:
              test:
                runs-on: 'ubuntu-latest'
                steps:
                - id: 'step-0'
                  run: 'echo ''Hello!'''

            """.trimIndent()
    }

    test("toYaml() - custom preamble with original before") {
        val yaml =
            workflow.toYaml(
                addConsistencyCheck = false,
                preamble =
                    WithOriginalBefore(
                        """
                        Test preamble
                        with original before
                        """.trimIndent(),
                    ),
            )

        yaml shouldBe
            """
            # This file was generated using Kotlin DSL (.github/workflows/some_workflow.main.kts).
            # If you want to modify the workflow, please change the Kotlin file and regenerate this YAML file.
            # Generated with https://github.com/typesafegithub/github-workflows-kt

            # Test preamble
            # with original before

            name: 'Test workflow'
            on:
              push: {}
            jobs:
              test_job:
                name: 'Test Job'
                runs-on: 'ubuntu-latest'
                steps:
                - id: 'step-0'
                  uses: 'actions/checkout@v4'
                - id: 'step-1'
                  run: 'echo ''hello!'''

            """.trimIndent()
    }

    test("toYaml() - custom preamble with original before without source") {
        val yaml =
            workflowWithoutSource.toYaml(
                preamble =
                    WithOriginalBefore(
                        """
                        Test preamble
                        with original before
                        """.trimIndent(),
                    ),
            )

        yaml shouldBe
            """
            # This file was generated using a Kotlin DSL.
            # If you want to modify the workflow, please change the Kotlin source and regenerate this YAML file.
            # Generated with https://github.com/typesafegithub/github-workflows-kt

            # Test preamble
            # with original before

            name: 'test'
            on:
              push: {}
            jobs:
              test:
                runs-on: 'ubuntu-latest'
                steps:
                - id: 'step-0'
                  run: 'echo ''Hello!'''

            """.trimIndent()
    }

    test("toYaml() - no preamble") {
        val yaml = workflowWithoutSource.toYaml(preamble = Just(""))

        yaml shouldBe
            """
            name: 'test'
            on:
              push: {}
            jobs:
              test:
                runs-on: 'ubuntu-latest'
                steps:
                - id: 'step-0'
                  run: 'echo ''Hello!'''

            """.trimIndent()
    }

    test("writeToFile() - calling Kotlin logic step") {
        // Given
        val targetTempFile = gitRootDir.resolve(".github/workflows/some_workflow.yaml").toFile()
        var callCount = 0
        var repoName = ""

        val myWorkflow =
            workflow(
                name = "test",
                on = listOf(Push()),
                sourceFile = gitRootDir.resolve(".github/workflows/some_workflow.main.kts"),
            ) {
                job(id = "test", runsOn = RunnerType.UbuntuLatest) {
                    uses(action = CheckoutV4())
                    run(name = "Step with Kotlin code in lambda") {
                        callCount++
                        repoName = github.repository
                    }
                }
            }

        // When
        // Writing the YAML
        myWorkflow.writeToFile(
            preamble = Just(""),
            addConsistencyCheck = false,
            gitRootDir = gitRootDir,
        )
        // During runtime
        myWorkflow.writeToFile(
            preamble = Just(""),
            addConsistencyCheck = false,
            gitRootDir = gitRootDir,
            getenv = {
                when (it) {
                    "GHWKT_RUN_STEP" -> "test:step-1"
                    "GHWKT_GITHUB_CONTEXT_JSON" ->
                        """
                        {
                            "repository": "test-repository",
                            "sha": "d34db33f",
                            "event_name": "push",
                            "event": {
                                "after": "bce434"
                            }
                        }
                        """.trimIndent()
                    else -> null
                }
            },
        )

        // Then
        targetTempFile.readText() shouldBe
            """
            name: 'test'
            on:
              push: {}
            jobs:
              test:
                runs-on: 'ubuntu-latest'
                steps:
                - id: 'step-0'
                  uses: 'actions/checkout@v4'
                - id: 'step-1'
                  name: 'Step with Kotlin code in lambda'
                  env:
                    GHWKT_GITHUB_CONTEXT_JSON: '${'$'}{{ toJSON(github) }}'
                  run: 'GHWKT_RUN_STEP=''test:step-1'' ''.github/workflows/some_workflow.main.kts'''

            """.trimIndent()
        callCount shouldBe 1
        repoName shouldBe "test-repository"
    }

    test("toYaml() - calling Kotlin logic step") {
        // Given
        val myWorkflow =
            workflow(
                name = "test",
                on = listOf(Push()),
                sourceFile = gitRootDir.resolve(".github/workflows/some_workflow.main.kts"),
            ) {
                job(id = "test", runsOn = RunnerType.UbuntuLatest) {
                    uses(action = CheckoutV4())
                    run(name = "Step with Kotlin code in lambda") {
                    }
                }
            }

        // Then
        shouldThrow<IllegalArgumentException> {
            // When
            myWorkflow.toYaml(
                preamble = Just(""),
                addConsistencyCheck = false,
                gitRootDir = gitRootDir,
            )
        }.also {
            it.message shouldBe "toYaml() currently doesn't support steps with Kotlin-based 'run' blocks!"
        }
    }

    test("writeToFile() - calling Kotlin logic step without prior checkout") {
        // Then
        shouldThrow<IllegalArgumentException> {
            // When
            workflow(
                name = "test",
                on = listOf(Push()),
                sourceFile = gitRootDir.resolve(".github/workflows/some_workflow.main.kts"),
            ) {
                job(id = "test", runsOn = RunnerType.UbuntuLatest) {
                    run(name = "Step with Kotlin code in lambda") {
                    }
                    uses(action = CheckoutV4())
                }
            }
        }.also {
            it.message shouldBe "Please check out the code prior to using Kotlin-based 'run' block!"
        }
    }

    test("writeToFile() - calling Kotlin logic step without setting sourceFile") {
        // Then
        shouldThrow<IllegalArgumentException> {
            // When
            workflow(
                name = "test",
                on = listOf(Push()),
            ) {
                job(id = "test", runsOn = RunnerType.UbuntuLatest) {
                    uses(action = CheckoutV4())
                    run(name = "Step with Kotlin code in lambda") {
                    }
                }
            }
        }.also {
            it.message shouldBe "sourceFile needs to be set when using Kotlin-based 'run' block!"
        }
    }
})
