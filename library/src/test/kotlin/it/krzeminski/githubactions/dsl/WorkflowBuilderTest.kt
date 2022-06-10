package it.krzeminski.githubactions.dsl

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import it.krzeminski.githubactions.domain.RunnerType
import it.krzeminski.githubactions.domain.RunnerType.UbuntuLatest
import it.krzeminski.githubactions.domain.triggers.Push
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
            val workflow = workflow(
                name = "Test workflow",
                on = listOf(Push()),
                sourceFile = Paths.get(".github/workflows/some_workflow.main.kts"),
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
                    runsOn = RunnerType.UbuntuLatest,
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
    }
})
