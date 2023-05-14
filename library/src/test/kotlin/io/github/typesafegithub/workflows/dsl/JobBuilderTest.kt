package io.github.typesafegithub.workflows.dsl

import io.github.typesafegithub.workflows.actions.actions.CheckoutV3
import io.github.typesafegithub.workflows.actions.actions.SetupJavaV3
import io.github.typesafegithub.workflows.actions.actions.SetupJavaV3.Distribution.Adopt
import io.github.typesafegithub.workflows.domain.RunnerType
import io.github.typesafegithub.workflows.domain.Workflow
import io.github.typesafegithub.workflows.domain.triggers.Push
import io.github.typesafegithub.workflows.yaml.Preamble
import io.github.typesafegithub.workflows.yaml.toYaml
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.nio.file.Paths

class JobBuilderTest : FunSpec(
    {
        context("job builder") {
            test("with custom step arguments") {
                // Given
                val workflow = workflow(
                    name = "Test workflow",
                    on = listOf(Push()),
                    sourceFile = Paths.get(".github/workflows/some_workflow.main.kts"),
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
                            action = CheckoutV3(),
                            _customArguments = mapOf("foo2" to true),
                        )
                        uses(
                            action = CheckoutV3(),
                            _customArguments = mapOf("foo3" to true),
                        )
                        uses(
                            name = "Set up Java",
                            action = SetupJavaV3(distribution = Adopt, javaVersion = "11"),
                            _customArguments = mapOf("foo4" to true),
                        )
                        uses(
                            action = SetupJavaV3(distribution = Adopt, javaVersion = "11"),
                            _customArguments = mapOf("foo5" to true),
                        )
                    }
                }

                // When
                val job = workflow.jobs.first()

                // Then
                job.steps.forEachIndexed { index, step ->
                    step._customArguments shouldBe mapOf("foo$index" to true)
                }
            }
        }

        context("conditions") {
            test("if and condition are both set") {
                shouldThrow<Error> {
                    workflow(
                        name = "test",
                        on = listOf(Push()),
                    ) {
                        job("test", runsOn = RunnerType.UbuntuLatest, condition = "a", `if` = "b") {
                            run(command = "ls")
                        }
                    }
                }
            }

            test("use if") {
                val yaml = workflow(
                    name = "test",
                    on = listOf(Push()),
                ) {
                    job("test", runsOn = RunnerType.UbuntuLatest, `if` = "b") {
                        run(command = "ls")
                    }
                }.toYaml(addConsistencyCheck = false, preamble = Preamble.Just(""))
                yaml.trim() shouldBe """
                name: 'test'
                on:
                  push: {}
                jobs:
                  test:
                    runs-on: 'ubuntu-latest'
                    if: 'b'
                    steps:
                    - id: 'step-0'
                      run: 'ls'
                """.trimIndent()
            }

            test("use condition") {
                val yaml = workflow(
                    name = "test",
                    on = listOf(Push()),
                ) {
                    job("test", runsOn = RunnerType.UbuntuLatest, condition = "a") {
                        run(command = "ls")
                    }
                }.toTestYaml()
                yaml.trim() shouldBe """
                name: 'test'
                on:
                  push: {}
                jobs:
                  test:
                    runs-on: 'ubuntu-latest'
                    if: 'a'
                    steps:
                    - id: 'step-0'
                      run: 'ls'
                """.trimIndent()
            }
        }
    },
)

private fun Workflow.toTestYaml() = toYaml(addConsistencyCheck = false, preamble = Preamble.Just(""))
