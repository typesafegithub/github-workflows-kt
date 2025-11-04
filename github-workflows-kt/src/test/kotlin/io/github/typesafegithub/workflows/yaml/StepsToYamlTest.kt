@file:Suppress("DEPRECATION") // Use deprecated action versions, to not have to update them.

package io.github.typesafegithub.workflows.yaml

import io.github.typesafegithub.workflows.actions.actions.Checkout
import io.github.typesafegithub.workflows.actions.actions.Checkout.FetchDepth
import io.github.typesafegithub.workflows.actions.actions.UploadArtifact
import io.github.typesafegithub.workflows.domain.ActionStep
import io.github.typesafegithub.workflows.domain.CommandStep
import io.github.typesafegithub.workflows.domain.Shell
import io.github.typesafegithub.workflows.domain.actions.Action
import io.github.typesafegithub.workflows.domain.actions.CustomAction
import io.github.typesafegithub.workflows.domain.actions.RegularAction
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class StepsToYamlTest :
    DescribeSpec({
        it("renders multiple steps") {
            // given
            val steps =
                listOf(
                    CommandStep(
                        id = "someId",
                        name = "Some command",
                        command = "echo 'test!'",
                    ),
                    ActionStep(
                        id = "someId",
                        name = "Some external action",
                        action = Checkout(),
                    ),
                )

            // when
            val yaml = steps.stepsToYaml()

            // then
            yaml shouldBe
                listOf(
                    mapOf(
                        "id" to "someId",
                        "name" to "Some command",
                        "run" to "echo 'test!'",
                    ),
                    mapOf(
                        "id" to "someId",
                        "name" to "Some external action",
                        "uses" to "actions/checkout@v4",
                    ),
                )
        }

        describe("command step") {
            it("renders with required parameters") {
                // given
                val steps =
                    listOf(
                        CommandStep(
                            id = "someId",
                            command = "echo 'test!'",
                        ),
                    )

                // when
                val yaml = steps.stepsToYaml()

                // then
                yaml shouldBe
                    listOf(
                        mapOf(
                            "id" to "someId",
                            "run" to "echo 'test!'",
                        ),
                    )
            }

            it("renders with all parameters") {
                // given
                val steps =
                    listOf(
                        CommandStep(
                            id = "someId",
                            name = "Some command",
                            env =
                                mapOf(
                                    "FOO" to "bar",
                                    "BAZ" to
                                        """
                                        goo,
                                        zoo
                                        """.trimIndent(),
                                ),
                            continueOnError = true,
                            timeoutMinutes = 123,
                            shell = Shell.Bash,
                            workingDirectory = "/home/me",
                            command = "echo 'test!'",
                            condition = "\${{ matrix.foo == 'bar' }}",
                            _customArguments =
                                mapOf(
                                    "foo" to true,
                                    "null-string" to "null",
                                    "null-value" to null,
                                    "empty-string" to "",
                                ),
                        ),
                    )

                // when
                val yaml = steps.stepsToYaml()

                // then
                yaml shouldBe
                    listOf(
                        mapOf(
                            "id" to "someId",
                            "name" to "Some command",
                            "env" to
                                mapOf(
                                    "FOO" to "bar",
                                    "BAZ" to
                                        """
                                        goo,
                                        zoo
                                        """.trimIndent(),
                                ),
                            "continue-on-error" to true,
                            "timeout-minutes" to 123,
                            "shell" to "bash",
                            "working-directory" to "/home/me",
                            "run" to "echo 'test!'",
                            "if" to "\${{ matrix.foo == 'bar' }}",
                            "foo" to true,
                            "null-string" to "null",
                            "null-value" to null,
                            "empty-string" to "",
                        ),
                    )
            }

            it("renders multiline command") {
                // given
                val steps =
                    listOf(
                        CommandStep(
                            id = "someId",
                            name = "Some command",
                            command =
                                """
                                echo 'first line'
                                echo 'second line'

                                echo 'third line'
                                """.trimIndent(),
                        ),
                    )

                // when
                val yaml = steps.stepsToYaml()

                // then
                yaml shouldBe
                    listOf(
                        mapOf(
                            "id" to "someId",
                            "name" to "Some command",
                            "run" to
                                """
                                echo 'first line'
                                echo 'second line'

                                echo 'third line'
                                """.trimIndent(),
                        ),
                    )
            }

            it("renders with custom shell") {
                // given
                val steps =
                    listOf(
                        CommandStep(
                            id = "someId",
                            command = "echo 'with custom shell!'",
                            shell = Shell.Custom("myCoolShell {0}"),
                        ),
                    )

                // when
                val yaml = steps.stepsToYaml()

                // then
                yaml shouldBe
                    listOf(
                        mapOf(
                            "id" to "someId",
                            "shell" to "myCoolShell {0}",
                            "run" to "echo 'with custom shell!'",
                        ),
                    )
            }

            it("renders with custom argument overriding built-in argument") {
                // given
                val steps =
                    listOf(
                        CommandStep(
                            id = "someId",
                            name = "Will be overridden",
                            command = "echo 'test!'",
                            _customArguments =
                                mapOf(
                                    "name" to "Overridden!",
                                ),
                        ),
                    )

                // when
                val yaml = steps.stepsToYaml()

                // then
                yaml shouldBe
                    listOf(
                        mapOf(
                            "id" to "someId",
                            "name" to "Overridden!",
                            "run" to "echo 'test!'",
                        ),
                    )
            }
        }

        describe("external action step") {
            it("renders with required parameters and no action inputs") {
                // given
                val steps =
                    listOf(
                        ActionStep(
                            id = "someId",
                            action = Checkout(),
                        ),
                    )

                // when
                val yaml = steps.stepsToYaml()

                // then
                yaml shouldBe
                    listOf(
                        mapOf(
                            "id" to "someId",
                            "uses" to "actions/checkout@v4",
                        ),
                    )
            }

            it("renders with all parameters") {
                // given
                val steps =
                    listOf(
                        ActionStep(
                            id = "someId",
                            name = "Some external action",
                            continueOnError = true,
                            timeoutMinutes = 123,
                            action = Checkout(fetchDepth = FetchDepth.Infinite),
                            env =
                                mapOf(
                                    "FOO" to "bar",
                                    "BAZ" to
                                        """
                                        goo,
                                        zoo
                                        """.trimIndent(),
                                ),
                            condition = "\${{ matrix.foo == 'bar' }}",
                            _customArguments =
                                mapOf(
                                    "foo" to true,
                                    "null-string" to "null",
                                    "null-value" to null,
                                    "empty-string" to "",
                                ),
                        ),
                    )

                // when
                val yaml = steps.stepsToYaml()

                // then
                yaml shouldBe
                    listOf(
                        mapOf(
                            "id" to "someId",
                            "name" to "Some external action",
                            "continue-on-error" to true,
                            "timeout-minutes" to 123,
                            "uses" to "actions/checkout@v4",
                            "with" to
                                mapOf(
                                    "fetch-depth" to "0",
                                ),
                            "env" to
                                mapOf(
                                    "FOO" to "bar",
                                    "BAZ" to
                                        """
                                        goo,
                                        zoo
                                        """.trimIndent(),
                                ),
                            "if" to "\${{ matrix.foo == 'bar' }}",
                            "foo" to true,
                            "null-string" to "null",
                            "null-value" to null,
                            "empty-string" to "",
                        ),
                    )
            }

            it("renders custom action") {
                // given
                val steps =
                    listOf(
                        ActionStep(
                            id = "latex",
                            name = "Latex",
                            action =
                                CustomAction(
                                    actionOwner = "xu-cheng",
                                    actionName = "latex-action",
                                    actionVersion = "v2",
                                    inputs =
                                        mapOf(
                                            "root_file" to "report.tex",
                                            "compiler" to "latexmk",
                                        ),
                                ),
                        ),
                    )

                // when
                val yaml = steps.stepsToYaml()

                // then
                yaml shouldBe
                    listOf(
                        mapOf(
                            "id" to "latex",
                            "name" to "Latex",
                            "uses" to "xu-cheng/latex-action@v2",
                            "with" to
                                mapOf(
                                    "root_file" to "report.tex",
                                    "compiler" to "latexmk",
                                ),
                        ),
                    )
            }

            it("renders with action with custom arguments") {
                // given
                val steps =
                    listOf(
                        ActionStep(
                            id = "someId",
                            action =
                                UploadArtifact(
                                    name = "artifact",
                                    path = listOf("path1", "path2"),
                                    _customInputs =
                                        mapOf(
                                            "path" to "override-path-value",
                                            "answer" to "42",
                                        ),
                                ),
                        ),
                    )

                // when
                val yaml = steps.stepsToYaml()

                // then
                yaml shouldBe
                    listOf(
                        mapOf(
                            "id" to "someId",
                            "uses" to "actions/upload-artifact@v3",
                            "with" to
                                mapOf(
                                    "name" to "artifact",
                                    "path" to "override-path-value",
                                    "answer" to "42",
                                ),
                        ),
                    )
            }

            it("renders with action's custom version") {
                // given
                val steps =
                    listOf(
                        ActionStep(
                            id = "someId",
                            action =
                                UploadArtifact(
                                    name = "artifact",
                                    path = listOf("path1", "path2"),
                                    _customVersion = "v2.3.4",
                                ),
                        ),
                    )

                // when
                val yaml = steps.stepsToYaml()

                // then
                yaml shouldBe
                    listOf(
                        mapOf(
                            "id" to "someId",
                            "uses" to "actions/upload-artifact@v2.3.4",
                            "with" to
                                mapOf(
                                    "name" to "artifact",
                                    "path" to
                                        """
                                        path1
                                        path2
                                        """.trimIndent(),
                                ),
                        ),
                    )
            }

            it("renders with custom argument overriding built-in argument") {
                // given
                val steps =
                    listOf(
                        ActionStep(
                            id = "someId",
                            name = "Will be overridden",
                            action = Checkout(),
                            _customArguments =
                                mapOf(
                                    "name" to "Overridden!",
                                ),
                        ),
                    )

                // when
                val yaml = steps.stepsToYaml()

                // then
                yaml shouldBe
                    listOf(
                        mapOf(
                            "id" to "someId",
                            "name" to "Overridden!",
                            "uses" to "actions/checkout@v4",
                        ),
                    )
            }

            it("renders with comment") {
                // given
                val steps =
                    listOf(
                        ActionStep(
                            id = "someId",
                            action =
                                object : RegularAction<Action.Outputs>(
                                    actionOwner = "some-owner",
                                    actionName = "some-name",
                                    actionVersion = "some-commit-hash",
                                    intendedVersion = "some-version",
                                ) {
                                    override fun toYamlArguments(): LinkedHashMap<String, String> =
                                        linkedMapOf("foo" to "bar")

                                    override fun buildOutputObject(stepId: String): Outputs = Outputs(stepId)
                                },
                        ),
                    )

                // when
                val yaml = steps.stepsToYaml()

                // then
                yaml shouldBe
                    listOf(
                        mapOf(
                            "id" to "someId",
                            "uses" to StringWithComment("some-owner/some-name@some-commit-hash", "some-version"),
                            "with" to mapOf("foo" to "bar"),
                        ),
                    )
            }
        }
    })
