package io.github.typesafegithub.workflows

import io.github.typesafegithub.workflows.actions.actions.Checkout
import io.github.typesafegithub.workflows.actions.awsactions.ConfigureAwsCredentials
import io.github.typesafegithub.workflows.actions.endbug.AddAndCommit
import io.github.typesafegithub.workflows.annotations.ExperimentalKotlinLogicStep
import io.github.typesafegithub.workflows.domain.Concurrency
import io.github.typesafegithub.workflows.domain.RunnerType
import io.github.typesafegithub.workflows.domain.actions.Action.Outputs
import io.github.typesafegithub.workflows.domain.actions.RegularAction
import io.github.typesafegithub.workflows.domain.triggers.Push
import io.github.typesafegithub.workflows.dsl.expressions.expr
import io.github.typesafegithub.workflows.dsl.workflow
import io.github.typesafegithub.workflows.yaml.CheckoutActionVersionSource
import io.github.typesafegithub.workflows.yaml.ConsistencyCheckJobConfig.Disabled
import io.github.typesafegithub.workflows.yaml.DEFAULT_CONSISTENCY_CHECK_JOB_CONFIG
import io.github.typesafegithub.workflows.yaml.Preamble.Just
import io.github.typesafegithub.workflows.yaml.Preamble.WithOriginalAfter
import io.github.typesafegithub.workflows.yaml.Preamble.WithOriginalBefore
import io.github.typesafegithub.workflows.yaml.generateYaml
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.engine.spec.tempdir
import io.kotest.matchers.shouldBe

@OptIn(ExperimentalKotlinLogicStep::class)
@Suppress("LargeClass")
class IntegrationTest :
    FunSpec({

        val gitRootDir =
            tempdir()
                .also {
                    it.resolve(".git").mkdirs()
                }.toPath()
        val sourceTempFile = gitRootDir.resolve(".github/workflows/some_workflow.main.kts").toFile()
        val targetTempFile = gitRootDir.resolve(".github/workflows/some_workflow.yaml").toFile()

        afterTest {
            targetTempFile.delete()
        }

        test("'hello world' workflow") {
            // when
            workflow(
                name = "Test workflow",
                on = listOf(Push()),
                sourceFile = sourceTempFile,
                consistencyCheckJobConfig =
                    DEFAULT_CONSISTENCY_CHECK_JOB_CONFIG.copy(
                        env = mapOf("GITHUB_TOKEN" to expr("secrets.GITHUB_TOKEN")),
                    ),
            ) {
                job(
                    id = "test_job",
                    runsOn = RunnerType.UbuntuLatest,
                ) {
                    uses(
                        name = "Check out",
                        action = Checkout(),
                    )

                    run(
                        name = "Hello world!",
                        command = "echo 'hello!'",
                    )
                }
            }

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

        test("environment variables and continueOnError") {
            // when
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
                sourceFile = sourceTempFile,
                consistencyCheckJobConfig = Disabled,
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
                        action = Checkout(),
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

        test("fallback for bindings server") {
            // when
            workflow(
                name = "Test workflow",
                on = listOf(Push()),
                sourceFile = sourceTempFile,
                consistencyCheckJobConfig =
                    DEFAULT_CONSISTENCY_CHECK_JOB_CONFIG.copy(
                        useLocalBindingsServerAsFallback = true,
                    ),
            ) {
                job(
                    id = "test_job",
                    runsOn = RunnerType.UbuntuLatest,
                ) {
                    run(
                        name = "Hello world!",
                        command = "echo 'hello!'",
                    )
                }
            }

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
                    steps:
                    - id: 'step-0'
                      name: 'Check out'
                      uses: 'actions/checkout@v4'
                    - id: 'step-1'
                      name: 'Execute script'
                      continue-on-error: true
                      run: 'rm ''.github/workflows/some_workflow.yaml'' && ''.github/workflows/some_workflow.main.kts'''
                    - id: 'step-2'
                      name: '[Fallback] Start the local server'
                      run: 'docker run -p 8080:8080 krzema12/github-workflows-kt-jit-binding-server &'
                      if: '${'$'}{{ steps.step-1.outcome != ''success'' }}'
                    - id: 'step-3'
                      name: '[Fallback] Wait for the server'
                      run: 'curl --head -X GET --retry 60 --retry-all-errors --retry-delay 1 http://localhost:8080/status'
                      if: '${'$'}{{ steps.step-1.outcome != ''success'' }}'
                    - id: 'step-4'
                      name: '[Fallback] Replace server URL in script'
                      run: 'sed -i -e ''s/https:\/\/bindings.krzeminski.it/http:\/\/localhost:8080/g'' .github/workflows/some_workflow.main.kts'
                      if: '${'$'}{{ steps.step-1.outcome != ''success'' }}'
                    - id: 'step-5'
                      name: '[Fallback] Execute script again'
                      run: 'rm -f ''.github/workflows/some_workflow.yaml'' && ''.github/workflows/some_workflow.main.kts'''
                      if: '${'$'}{{ steps.step-1.outcome != ''success'' }}'
                    - id: 'step-6'
                      name: 'Consistency check'
                      run: 'git diff --exit-code ''.github/workflows/some_workflow.yaml'''
                  test_job:
                    runs-on: 'ubuntu-latest'
                    needs:
                    - 'check_yaml_consistency'
                    steps:
                    - id: 'step-0'
                      name: 'Hello world!'
                      run: 'echo ''hello!'''

                """.trimIndent()
        }

        test("actions/checkout's version given explicitly") {
            // when
            workflow(
                name = "Test workflow",
                on = listOf(Push()),
                sourceFile = sourceTempFile,
                consistencyCheckJobConfig =
                    DEFAULT_CONSISTENCY_CHECK_JOB_CONFIG.copy(
                        checkoutActionVersion = CheckoutActionVersionSource.Given("v123"),
                    ),
            ) {
                job(
                    id = "test_job",
                    runsOn = RunnerType.UbuntuLatest,
                ) {
                    run(
                        name = "Hello world!",
                        command = "echo 'hello!'",
                    )
                }
            }

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
                    steps:
                    - id: 'step-0'
                      name: 'Check out'
                      uses: 'actions/checkout@v123'
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
                      name: 'Hello world!'
                      run: 'echo ''hello!'''

                """.trimIndent()
        }

        test("actions/checkout's version inferred from classpath") {
            // when
            workflow(
                name = "Test workflow",
                on = listOf(Push()),
                sourceFile = sourceTempFile,
                consistencyCheckJobConfig =
                    DEFAULT_CONSISTENCY_CHECK_JOB_CONFIG.copy(
                        checkoutActionVersion = CheckoutActionVersionSource.InferredFromClasspath
                    ),
            ) {
                job(
                    id = "test_job",
                    runsOn = RunnerType.UbuntuLatest,
                ) {
                    run(
                        name = "Hello world!",
                        command = "echo 'hello!'",
                    )
                }
            }

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
                      name: 'Hello world!'
                      run: 'echo ''hello!'''

                """.trimIndent()
        }

        test("with concurrency, default behavior") {
            // when
            workflow(
                name = "Test workflow",
                on = listOf(Push()),
                sourceFile = sourceTempFile,
                consistencyCheckJobConfig = Disabled,
                concurrency = Concurrency("workflow_staging_environment"),
            ) {
                job(
                    id = "test_job",
                    runsOn = RunnerType.UbuntuLatest,
                    concurrency = Concurrency("job_staging_environment"),
                ) {
                    val addAndCommit = uses(action = AddAndCommit())

                    uses(
                        name = "Some step consuming other step's output",
                        action =
                            Checkout(
                                repository = expr(addAndCommit.id),
                                ref = expr(addAndCommit.outputs.commitSha),
                                token = expr(addAndCommit.outputs["my-unsafe-output"]),
                            ),
                    )
                }
            }

            // then
            targetTempFile.readText() shouldBe
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
        test("long strings with GitHub expressions in action arguments") {
            // when
            workflow(
                name = "Test workflow",
                on = listOf(Push()),
                sourceFile = sourceTempFile,
                consistencyCheckJobConfig = Disabled,
            ) {
                job(id = "deploy-dev", runsOn = RunnerType.UbuntuLatest) {
                    uses(
                        action =
                            ConfigureAwsCredentials(
                                roleToAssume =
                                    "arn:aws:iam::" +
                                        "1234567890".repeat(2) +
                                        ":role/github-actions-role/" +
                                        "1234567890".repeat(3),
                                awsRegion = "us-west-1",
                            ),
                    )
                    uses(
                        action =
                            ConfigureAwsCredentials(
                                roleToAssume =
                                    "arn:aws:iam::" +
                                        "1234567890".repeat(0) +
                                        ":role/github-actions-role/" +
                                        expr { github.token },
                                awsRegion = "us-west-1",
                            ),
                    )
                    uses(
                        action =
                            ConfigureAwsCredentials(
                                roleToAssume =
                                    "arn:aws:iam::" +
                                        "1234567890".repeat(1) +
                                        ":role/github-actions-role/" +
                                        expr { github.token },
                                awsRegion = "us-west-1",
                            ),
                    )
                    uses(
                        action =
                            ConfigureAwsCredentials(
                                roleToAssume =
                                    "arn:aws:iam::" +
                                        "1234567890".repeat(2) +
                                        ":role/github-actions-role/" +
                                        expr { github.token },
                                awsRegion = "us-west-1",
                            ),
                    )
                }
            }

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

        test("with concurrency, cancel in progress") {
            // when
            workflow(
                name = "Test workflow",
                on = listOf(Push()),
                sourceFile = sourceTempFile,
                consistencyCheckJobConfig = Disabled,
                concurrency = Concurrency("workflow_staging_environment", cancelInProgress = true),
            ) {
                job(
                    id = "test_job",
                    runsOn = RunnerType.UbuntuLatest,
                    concurrency = Concurrency("job_staging_environment", cancelInProgress = true),
                ) {
                    val addAndCommit = uses(action = AddAndCommit())

                    uses(
                        name = "Some step consuming other step's output",
                        action =
                            Checkout(
                                repository = expr(addAndCommit.id),
                                ref = expr(addAndCommit.outputs.commitSha),
                                token = expr(addAndCommit.outputs["my-unsafe-output"]),
                            ),
                    )
                }
            }

            // then
            targetTempFile.readText() shouldBe
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

        test("YAML consistency job condition") {
            // given
            workflow(
                name = "Test workflow",
                on = listOf(Push()),
                consistencyCheckJobConfig =
                    DEFAULT_CONSISTENCY_CHECK_JOB_CONFIG.copy(
                        condition = "\${{ always() }}",
                    ),
                sourceFile = sourceTempFile,
            ) {
                job(
                    id = "test_job",
                    name = "Test Job",
                    runsOn = RunnerType.UbuntuLatest,
                ) {
                    uses(action = Checkout())
                    run(command = "echo 'hello!'")
                }
            }

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
                    if: '${'$'}{{ always() }}'
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

        test("works without `sourceFile`") {
            // when
            workflow(
                name = "Test workflow",
                on = listOf(Push()),
                targetFileName = "some_workflow.yaml",
                gitRootDir = gitRootDir,
            ) {
                job(
                    id = "test_job",
                    runsOn = RunnerType.UbuntuLatest,
                ) {
                    uses(
                        name = "Check out",
                        action = Checkout(),
                    )

                    run(
                        name = "Hello world!",
                        command = "echo 'hello!'",
                    )
                }
            }

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

        test("custom preamble") {
            workflow(
                name = "test",
                on = listOf(Push()),
                sourceFile = sourceTempFile,
                preamble =
                    Just(
                        """
                        Test preamble
                        with a second line
                        """.trimIndent(),
                    ),
                consistencyCheckJobConfig = Disabled,
            ) {
                job(id = "test", runsOn = RunnerType.UbuntuLatest) {
                    run(command = "echo 'Hello!'")
                }
            }

            targetTempFile.readText() shouldBe
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

        test("custom preamble with empty line") {
            workflow(
                name = "test",
                on = listOf(Push()),
                sourceFile = sourceTempFile,
                preamble =
                    Just(
                        """
                        Test preamble

                        with an empty line
                        """.trimIndent(),
                    ),
                consistencyCheckJobConfig = Disabled,
            ) {
                job(id = "test", runsOn = RunnerType.UbuntuLatest) {
                    run(command = "echo 'Hello!'")
                }
            }

            targetTempFile.readText() shouldBe
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

        test("custom preamble with original after") {
            workflow(
                name = "Test workflow",
                on = listOf(Push()),
                sourceFile = sourceTempFile,
                preamble =
                    WithOriginalAfter(
                        """
                        Test preamble

                        with original after
                        """.trimIndent(),
                    ),
                consistencyCheckJobConfig = Disabled,
            ) {
                job(id = "test_job", runsOn = RunnerType.UbuntuLatest) {
                    run(command = "echo 'Hello!'")
                }
            }

            targetTempFile.readText() shouldBe
                """
                # Test preamble
                #
                # with original after

                # This file was generated using Kotlin DSL (.github/workflows/some_workflow.main.kts).
                # If you want to modify the workflow, please change the Kotlin file and regenerate this YAML file.
                # Generated with https://github.com/typesafegithub/github-workflows-kt

                name: 'Test workflow'
                on:
                  push: {}
                jobs:
                  test_job:
                    runs-on: 'ubuntu-latest'
                    steps:
                    - id: 'step-0'
                      run: 'echo ''Hello!'''

                """.trimIndent()
        }

        test("custom preamble with original before") {
            workflow(
                name = "Test workflow",
                on = listOf(Push()),
                sourceFile = sourceTempFile,
                preamble =
                    WithOriginalBefore(
                        """
                        Test preamble

                        with original before
                        """.trimIndent(),
                    ),
                consistencyCheckJobConfig = Disabled,
            ) {
                job(id = "test_job", runsOn = RunnerType.UbuntuLatest) {
                    run(command = "echo 'Hello!'")
                }
            }

            targetTempFile.readText() shouldBe
                """
                # This file was generated using Kotlin DSL (.github/workflows/some_workflow.main.kts).
                # If you want to modify the workflow, please change the Kotlin file and regenerate this YAML file.
                # Generated with https://github.com/typesafegithub/github-workflows-kt

                # Test preamble
                #
                # with original before

                name: 'Test workflow'
                on:
                  push: {}
                jobs:
                  test_job:
                    runs-on: 'ubuntu-latest'
                    steps:
                    - id: 'step-0'
                      run: 'echo ''Hello!'''

                """.trimIndent()
        }

        test("no preamble") {
            workflow(
                name = "test",
                on = listOf(Push()),
                sourceFile = sourceTempFile,
                preamble = Just(""),
                consistencyCheckJobConfig = Disabled,
            ) {
                job(id = "test", runsOn = RunnerType.UbuntuLatest) {
                    run(command = "echo 'Hello!'")
                }
            }

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
                      run: 'echo ''Hello!'''

                """.trimIndent()
        }

        test("calling Kotlin logic step without prior checkout") {
            // Then
            shouldThrow<IllegalArgumentException> {
                // When
                workflow(
                    name = "test",
                    on = listOf(Push()),
                    sourceFile = sourceTempFile,
                ) {
                    job(id = "test", runsOn = RunnerType.UbuntuLatest) {
                        run(name = "Step with Kotlin code in lambda") {
                        }
                        uses(action = Checkout())
                    }
                }
            }.also {
                it.message shouldBe "Please check out the code prior to using Kotlin-based 'run' block!"
            }
        }

        test("calling Kotlin logic step without setting sourceFile") {
            // Then
            shouldThrow<IllegalArgumentException> {
                // When
                workflow(
                    name = "test",
                    on = listOf(Push()),
                ) {
                    job(id = "test", runsOn = RunnerType.UbuntuLatest) {
                        uses(action = Checkout())
                        run(name = "Step with Kotlin code in lambda") {
                        }
                    }
                }
            }.also {
                it.message shouldBe "sourceFile needs to be set when using Kotlin-based 'run' block!"
            }
        }

        test("return workflow as string and do not write to file") {
            // when
            var workflowYaml: String? = null
            workflow(
                name = "Test workflow",
                on = listOf(Push()),
                sourceFile = sourceTempFile,
                targetFileName = null,
                consistencyCheckJobConfig = Disabled,
                useWorkflow = { workflowYaml = it.generateYaml() },
            ) {
                job(
                    id = "test_job",
                    runsOn = RunnerType.UbuntuLatest,
                ) {
                    uses(
                        name = "Check out",
                        action = Checkout(),
                    )
                }
            }

            // then
            workflowYaml shouldBe
                """
                # This file was generated using Kotlin DSL (.github/workflows/some_workflow.main.kts).
                # If you want to modify the workflow, please change the Kotlin file and regenerate this YAML file.
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

                """.trimIndent()
            targetTempFile.exists() shouldBe false
        }

        test("action version with comment") {
            // when
            workflow(
                name = "Test workflow",
                on = listOf(Push()),
                sourceFile = sourceTempFile,
                consistencyCheckJobConfig = Disabled,
            ) {
                job(
                    id = "test_job",
                    runsOn = RunnerType.UbuntuLatest,
                ) {
                    uses(
                        name = "Check out",
                        action =
                            object : RegularAction<Outputs>(
                                actionOwner = "actions",
                                actionName = "checkout",
                                actionVersion = "08c6903cd8c0fde910a37f88322edcfb5dd907a8",
                                intendedVersion = "v5.0.0",
                            ) {
                                override fun toYamlArguments(): LinkedHashMap<String, String> =
                                    linkedMapOf("path" to "my-repo")

                                override fun buildOutputObject(stepId: String): Outputs = Outputs(stepId)
                            },
                    )
                }
            }

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
                  test_job:
                    runs-on: 'ubuntu-latest'
                    steps:
                    - id: 'step-0'
                      name: 'Check out'
                      uses: 'actions/checkout@08c6903cd8c0fde910a37f88322edcfb5dd907a8' # v5.0.0
                      with:
                        path: 'my-repo'

                """.trimIndent()
        }
    })
