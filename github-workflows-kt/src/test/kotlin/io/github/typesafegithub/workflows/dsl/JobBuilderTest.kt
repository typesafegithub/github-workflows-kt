package io.github.typesafegithub.workflows.dsl

import io.github.typesafegithub.workflows.actions.actions.Checkout
import io.github.typesafegithub.workflows.actions.actions.SetupJava
import io.github.typesafegithub.workflows.actions.actions.SetupJava.Distribution.Adopt
import io.github.typesafegithub.workflows.domain.RunnerType
import io.github.typesafegithub.workflows.domain.Workflow
import io.github.typesafegithub.workflows.domain.triggers.Push
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.engine.spec.tempdir
import io.kotest.matchers.shouldBe
import java.nio.file.Paths

class JobBuilderTest :
    FunSpec({
        val gitRootDir =
            tempdir()
                .also {
                    it.resolve(".git").mkdirs()
                }.toPath()
        val sourceTempFile = gitRootDir.resolve(".github/workflows/some_workflow.main.kts").toFile()

        context("job builder") {
            test("with custom step arguments") {
                // Given
                var workflow: Workflow? = null
                workflow(
                    name = "Test workflow",
                    on = listOf(Push()),
                    sourceFile = Paths.get(".github/workflows/some_workflow.main.kts").toFile(),
                    useWorkflow = { workflow = it },
                ) {
                    job(
                        id = "test_job",
                        runsOn = RunnerType.UbuntuLatest,
                    ) {
                        run(
                            name = "Hello world!",
                            command = "echo 'hello!'",
                            _customArguments = mapOf("foo0" to true),
                        )
                        run(
                            command = "echo 'hello!'",
                            _customArguments = mapOf("foo1" to true),
                        )
                        uses(
                            name = "Check out",
                            action = Checkout(),
                            _customArguments = mapOf("foo2" to true),
                        )
                        uses(
                            action = Checkout(),
                            _customArguments = mapOf("foo3" to true),
                        )
                        uses(
                            name = "Set up Java",
                            action = SetupJava(distribution = Adopt, javaVersion = "11"),
                            _customArguments = mapOf("foo4" to true),
                        )
                        uses(
                            action = SetupJava(distribution = Adopt, javaVersion = "11"),
                            _customArguments = mapOf("foo5" to true),
                        )
                    }
                }

                // When
                val job = workflow!!.jobs.first()

                // Then
                job.steps.forEachIndexed { index, step ->
                    step._customArguments shouldBe mapOf("foo$index" to true)
                }
            }
        }

        context("conditions") {
            test("'if' and 'condition' are both set") {
                shouldThrow<IllegalArgumentException> {
                    workflow(
                        name = "test",
                        on = listOf(Push()),
                        sourceFile = sourceTempFile,
                    ) {
                        job(id = "test", runsOn = RunnerType.UbuntuLatest, condition = "a", `if` = "b") {
                            run(command = "ls")
                        }
                    }
                }
            }

            test("use 'if'") {
                // When
                var workflow: Workflow? = null
                workflow(
                    name = "test",
                    on = listOf(Push()),
                    sourceFile = sourceTempFile,
                    useWorkflow = { workflow = it },
                ) {
                    job(id = "test", runsOn = RunnerType.UbuntuLatest, `if` = "b") {
                        run(command = "ls")
                    }
                }

                // Then
                workflow!!.jobs[0].condition shouldBe "b"
            }

            test("use 'condition'") {
                // When
                var workflow: Workflow? = null
                workflow(
                    name = "test",
                    on = listOf(Push()),
                    sourceFile = sourceTempFile,
                    useWorkflow = { workflow = it },
                ) {
                    job(id = "test", runsOn = RunnerType.UbuntuLatest, condition = "b") {
                        run(command = "ls")
                    }
                }

                // Then
                workflow!!.jobs[0].condition shouldBe "b"
            }
        }
    })
