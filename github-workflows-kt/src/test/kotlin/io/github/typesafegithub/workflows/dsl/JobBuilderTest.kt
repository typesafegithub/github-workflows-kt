package io.github.typesafegithub.workflows.dsl

import io.github.typesafegithub.workflows.actions.actions.Checkout
import io.github.typesafegithub.workflows.actions.actions.DeployPages
import io.github.typesafegithub.workflows.actions.actions.SetupJava
import io.github.typesafegithub.workflows.actions.actions.SetupJava.Distribution.Adopt
import io.github.typesafegithub.workflows.domain.ActionStep
import io.github.typesafegithub.workflows.domain.Environment
import io.github.typesafegithub.workflows.domain.RunnerType
import io.github.typesafegithub.workflows.domain.Workflow
import io.github.typesafegithub.workflows.domain.triggers.Push
import io.github.typesafegithub.workflows.dsl.expressions.expr
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

        context("step custom IDs") {
            test("set custom ID for a 'run' step") {
                // When
                var workflow: Workflow? = null
                workflow(
                    name = "test",
                    on = listOf(Push()),
                    sourceFile = sourceTempFile,
                    useWorkflow = { workflow = it },
                ) {
                    job(id = "test", runsOn = RunnerType.UbuntuLatest) {
                        run(command = "command with default ID")
                        run(id = "foobar", command = "command with custom ID")
                    }
                }

                // Then
                workflow!!.jobs[0].steps[0].id shouldBe "step-0"
                workflow.jobs[0].steps[1].id shouldBe "foobar"
            }

            test("set custom ID for a 'uses' step") {
                // When
                var workflow: Workflow? = null
                workflow(
                    name = "test",
                    on = listOf(Push()),
                    sourceFile = sourceTempFile,
                    useWorkflow = { workflow = it },
                ) {
                    job(id = "test", runsOn = RunnerType.UbuntuLatest) {
                        uses(
                            name = "step with default ID",
                            action = Checkout(),
                        )
                        uses(
                            name = "step with default ID",
                            id = "foobar",
                            action = Checkout(),
                        )
                    }
                }

                // Then
                workflow!!.jobs[0].steps[0].id shouldBe "step-0"
                workflow.jobs[0].steps[1].id shouldBe "foobar"
            }

            test("custom ID is equal to existing default step ID") {
                shouldThrow<IllegalArgumentException> {
                    workflow(
                        name = "test",
                        on = listOf(Push()),
                        sourceFile = sourceTempFile,
                    ) {
                        job(id = "test", runsOn = RunnerType.UbuntuLatest) {
                            run(command = "command with default ID")
                            run(id = "step-0", command = "command with custom ID")
                        }
                    }
                }.also {
                    it.message shouldBe "Duplicated step IDs for job 'test': [step-0]"
                }
            }

            test("two equal custom IDs set") {
                shouldThrow<IllegalArgumentException> {
                    workflow(
                        name = "test",
                        on = listOf(Push()),
                        sourceFile = sourceTempFile,
                    ) {
                        job(id = "test", runsOn = RunnerType.UbuntuLatest) {
                            run(id = "foobar", command = "command #1 with custom ID")
                            run(id = "foobar", command = "command #2 with custom ID")
                        }
                    }
                }.also {
                    it.message shouldBe "Duplicated step IDs for job 'test': [foobar]"
                }
            }
        }

        test("action output passed to environment") {
            // When
            var workflow: Workflow? = null
            workflow(
                name = "test",
                on = listOf(Push()),
                sourceFile = sourceTempFile,
                useWorkflow = { workflow = it },
            ) {
                val deploymentStep =
                    ActionStep(
                        id = "deployment",
                        name = "Deploy to GitHub Pages",
                        action = DeployPages(),
                    )
                job(
                    id = "deploy",
                    runsOn = RunnerType.UbuntuLatest,
                    environment =
                        Environment(
                            name = "github-pages",
                            url = expr(deploymentStep.outputs.pageUrl),
                        ),
                ) {
                    uses(actionStep = deploymentStep)
                }
            }

            // Then
            workflow!!.jobs[0].environment?.url shouldBe "\${{ steps.deployment.outputs.page_url }}"
        }
    })
