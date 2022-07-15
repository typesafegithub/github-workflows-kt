package it.krzeminski.githubactions.dsl

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.engine.spec.tempdir
import io.kotest.matchers.shouldBe
import it.krzeminski.githubactions.actions.actions.CheckoutV3
import it.krzeminski.githubactions.actions.endbug.AddAndCommitV9
import it.krzeminski.githubactions.domain.Concurrency
import it.krzeminski.githubactions.domain.Defaults
import it.krzeminski.githubactions.domain.Run
import it.krzeminski.githubactions.domain.RunnerType.UbuntuLatest
import it.krzeminski.githubactions.domain.triggers.Push
import it.krzeminski.githubactions.dsl.expressions.expr
import it.krzeminski.githubactions.yaml.toYaml
import java.nio.file.Paths

class WorkflowBuilderTest : FunSpec({
    context("validation errors") {
        test("no jobs defined") {
            val exception = shouldThrow<IllegalArgumentException> {
                workflow(
                    name = "Some workflow",
                    on = listOf(Push()),
                    sourceFile = Paths.get(".github/workflows/some_workflow.main.kts"),
                ) {
                    // No jobs.
                }
            }
            exception.message shouldBe "There are no jobs defined!"
        }

        test("no steps defined") {
            val exception = shouldThrow<IllegalArgumentException> {
                workflow(
                    name = "Some workflow",
                    on = listOf(Push()),
                    sourceFile = Paths.get(".github/workflows/some_workflow.main.kts"),
                ) {
                    job(
                        id = "Some-job",
                        runsOn = UbuntuLatest,
                    ) {
                        // No steps.
                    }
                }
            }
            exception.message shouldBe "There are no steps defined!"
        }

        test("no triggers defined") {
            val exception = shouldThrow<IllegalArgumentException> {
                workflow(
                    name = "Some workflow",
                    on = emptyList(),
                    sourceFile = Paths.get(".github/workflows/some_workflow.main.kts"),
                ) {
                    job(
                        id = "Some job",
                        runsOn = UbuntuLatest,
                    ) {
                        run(
                            name = "Some command",
                            command = "echo 'hello!'",
                        )
                    }
                }
            }
            exception.message shouldBe "There are no triggers defined!"
        }

        test("duplicated job names") {
            val exception = shouldThrow<IllegalArgumentException> {
                workflow(
                    name = "Some workflow",
                    on = listOf(Push()),
                    sourceFile = Paths.get(".github/workflows/some_workflow.main.kts"),
                ) {
                    job(
                        id = "Some-job-1",
                        runsOn = UbuntuLatest,
                    ) {
                        run(
                            name = "Some command",
                            command = "echo 'hello!'",
                        )
                    }

                    job(
                        id = "Some-job-1",
                        runsOn = UbuntuLatest,
                    ) {
                        run(
                            name = "Some command",
                            command = "echo 'hello!'",
                        )
                    }

                    job(
                        id = "Some-job-2",
                        runsOn = UbuntuLatest,
                    ) {
                        run(
                            name = "Some command",
                            command = "echo 'hello!'",
                        )
                    }

                    job(
                        id = "Some-job-3",
                        runsOn = UbuntuLatest,
                    ) {
                        run(
                            name = "Some command",
                            command = "echo 'hello!'",
                        )
                    }

                    job(
                        id = "Some-job-3",
                        runsOn = UbuntuLatest,
                    ) {
                        run(
                            name = "Some command",
                            command = "echo 'hello!'",
                        )
                    }
                }
            }
            exception.message shouldBe "Duplicated job names: [Some-job-1, Some-job-3]"
        }

        test("workflow with custom arguments") {
            val gitRootDir = tempdir().also {
                it.resolve(".git").mkdirs()
            }.toPath()
            val workflow = workflow(
                name = "Test workflow",
                on = listOf(Push()),
                sourceFile = gitRootDir.resolve(".github/workflows/some_workflow.main.kts"),
                _customArguments = mapOf(
                    "dry-run" to BooleanCustomValue(true),
                    "written-by" to ListCustomValue("Alice", "Bob"),
                    "concurrency" to ObjectCustomValue(
                        mapOf(
                            "group" to expr("github.ref"),
                            "cancel-in-progress" to "true",
                        )
                    )
                ),
            ) {
                job(
                    id = "test_job",
                    runsOn = UbuntuLatest,
                ) {
                    run(
                        name = "Hello world!",
                        command = "echo 'hello!'",
                    )
                }
            }
            workflow.toYaml(addConsistencyCheck = false) shouldBe """
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
                        run: echo 'hello!'
                dry-run: true
                written-by:
                  - 'Alice'
                  - 'Bob'
                concurrency:
                  group: ${'$'}{{ github.ref }}
                  cancel-in-progress: true

            """.trimIndent()
        }

        test("defaults defined with no parameters") {
            val exception = shouldThrow<IllegalArgumentException> {
                workflow(
                    name = "Test Workflow",
                    on = listOf(Push()),
                    sourceFile = Paths.get(".github/workflows/some_workflow.main.kts"),
                    defaults = Defaults(run = Run())
                ) {
                    job(
                        id = "test_job",
                        runsOn = UbuntuLatest,
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
                }
            }
            exception.message shouldBe "Run should at least have one of shell or working-directory defined!"
        }
    }
})
