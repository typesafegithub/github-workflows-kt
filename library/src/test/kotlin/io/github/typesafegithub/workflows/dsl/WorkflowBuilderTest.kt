package io.github.typesafegithub.workflows.dsl

import io.github.typesafegithub.workflows.domain.Permission
import io.github.typesafegithub.workflows.domain.RunnerType.UbuntuLatest
import io.github.typesafegithub.workflows.domain.triggers.Push
import io.github.typesafegithub.workflows.yaml.Preamble
import io.github.typesafegithub.workflows.yaml.toYaml
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.nio.file.Paths

class WorkflowBuilderTest : FunSpec(
    {
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
        }

        context("properties") {
            test("permissions") {
                val yaml = workflow(
                    name = "test",
                    permissions = listOf(
                        Permission.actions.read,
                        Permission.checks.read,
                        Permission.contents.write,
                    ),
                    on = listOf(Push()),
                ) {
                    job(id = "job", runsOn = UbuntuLatest) { run(command = "ls") }
                }.toYaml(addConsistencyCheck = false, preamble = Preamble.Just(""))

                yaml.trim() shouldBe """
                    name: test
                    on:
                      push: {}
                    permissions:
                      actions: read
                      checks: read
                      contents: write
                    jobs:
                      job:
                        runs-on: ubuntu-latest
                        steps:
                        - id: step-0
                          run: ls
                """.trimIndent()
            }
        }
    },
)
