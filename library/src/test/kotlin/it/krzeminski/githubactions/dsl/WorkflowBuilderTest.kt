package it.krzeminski.githubactions.dsl

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import it.krzeminski.githubactions.domain.RunnerType.UbuntuLatest
import it.krzeminski.githubactions.domain.triggers.Push
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
    }
},)
