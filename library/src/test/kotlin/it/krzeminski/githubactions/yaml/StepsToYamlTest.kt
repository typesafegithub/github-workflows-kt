package it.krzeminski.githubactions.yaml

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import it.krzeminski.githubactions.actions.Action
import it.krzeminski.githubactions.actions.actions.CheckoutV3
import it.krzeminski.githubactions.actions.actions.CheckoutV3.FetchDepth
import it.krzeminski.githubactions.actions.actions.UploadArtifactV2
import it.krzeminski.githubactions.domain.CommandStep
import it.krzeminski.githubactions.domain.ExternalActionStep

class StepsToYamlTest : DescribeSpec({
    it("renders multiple steps") {
        // given
        val steps = listOf(
            CommandStep(
                id = "someId",
                name = "Some command",
                command = "echo 'test!'",
            ),
            ExternalActionStep(
                id = "someId",
                name = "Some external action",
                action = CheckoutV3(),
            ),
        )

        // when
        val yaml = steps.stepsToYaml()

        // then
        yaml shouldBe """|- id: someId
                         |  name: Some command
                         |  run: echo 'test!'
                         |- id: someId
                         |  name: Some external action
                         |  uses: actions/checkout@v3""".trimMargin()
    }

    describe("command step") {
        it("renders with required parameters") {
            // given
            val steps = listOf(
                CommandStep(
                    id = "someId",
                    name = "Some command",
                    command = "echo 'test!'",
                ),
            )

            // when
            val yaml = steps.stepsToYaml()

            // then
            yaml shouldBe """|- id: someId
                             |  name: Some command
                             |  run: echo 'test!'""".trimMargin()
        }

        it("renders with environment variables") {
            // given
            val steps = listOf(
                CommandStep(
                    id = "someId",
                    name = "Some command",
                    command = "echo 'test!'",
                    env = linkedMapOf(
                        "FOO" to "bar",
                        "BAZ" to """
                            goo,
                            zoo
                        """.trimIndent()
                    ),
                ),
            )

            // when
            val yaml = steps.stepsToYaml()

            // then
            yaml shouldBe """|- id: someId
                             |  name: Some command
                             |  env:
                             |    FOO: bar
                             |    BAZ: |
                             |      goo,
                             |      zoo
                             |  run: echo 'test!'""".trimMargin()
        }

        it("renders with condition") {
            // given
            val steps = listOf(
                CommandStep(
                    id = "someId",
                    name = "Some command",
                    command = "echo 'test!'",
                    condition = "\${{ matrix.foo == 'bar' }}"
                ),
            )

            // when
            val yaml = steps.stepsToYaml()

            // then
            yaml shouldBe """|- id: someId
                             |  name: Some command
                             |  run: echo 'test!'
                             |  if: ${'$'}{{ matrix.foo == 'bar' }}""".trimMargin()
        }

        it("renders multiline command") {
            // given
            val steps = listOf(
                CommandStep(
                    id = "someId",
                    name = "Some command",
                    command = """
                        echo 'first line'
                        echo 'second line'

                        echo 'third line'
                    """.trimIndent(),
                ),
            )

            // when
            val yaml = steps.stepsToYaml()

            // then
            yaml shouldBe """|- id: someId
                             |  name: Some command
                             |  run: |
                             |    echo 'first line'
                             |    echo 'second line'
                             |    
                             |    echo 'third line'""".trimMargin()
        }
    }

    describe("external action step") {
        it("renders with no parameters") {
            // given
            val steps = listOf(
                ExternalActionStep(
                    id = "someId",
                    name = "Some external action",
                    action = CheckoutV3(),
                ),
            )

            // when
            val yaml = steps.stepsToYaml()

            // then
            yaml shouldBe """|- id: someId
                             |  name: Some external action
                             |  uses: actions/checkout@v3""".trimMargin()
        }

        it("renders with some parameters") {
            // given
            val steps = listOf(
                ExternalActionStep(
                    id = "someId",
                    name = "Some external action",
                    action = CheckoutV3(fetchDepth = FetchDepth.Infinite),
                ),
            )

            // when
            val yaml = steps.stepsToYaml()

            // then
            yaml shouldBe """|- id: someId
                             |  name: Some external action
                             |  uses: actions/checkout@v3
                             |  with:
                             |    fetch-depth: 0""".trimMargin()
        }

        it("renders with environment variables") {
            // given
            val steps = listOf(
                ExternalActionStep(
                    id = "someId",
                    name = "Some external action",
                    action = CheckoutV3(),
                    env = linkedMapOf(
                        "FOO" to "bar",
                        "BAZ" to """
                            goo,
                            zoo
                        """.trimIndent()
                    ),
                ),
            )

            // when
            val yaml = steps.stepsToYaml()

            // then
            yaml shouldBe """|- id: someId
                             |  name: Some external action
                             |  uses: actions/checkout@v3
                             |  env:
                             |    FOO: bar
                             |    BAZ: |
                             |      goo,
                             |      zoo""".trimMargin()
        }

        it("renders with condition") {
            // given
            val steps = listOf(
                ExternalActionStep(
                    id = "someId",
                    name = "Some external action",
                    action = CheckoutV3(),
                    condition = "\${{ matrix.foo == 'bar' }}"
                ),
            )

            // when
            val yaml = steps.stepsToYaml()

            // then
            yaml shouldBe """|- id: someId
                             |  name: Some external action
                             |  uses: actions/checkout@v3
                             |  if: ${'$'}{{ matrix.foo == 'bar' }}""".trimMargin()
        }

        it("renders with some parameters and condition") {
            // given
            val steps = listOf(
                ExternalActionStep(
                    id = "someId",
                    name = "Some external action",
                    action = CheckoutV3(fetchDepth = FetchDepth.Infinite),
                    condition = "\${{ matrix.foo == 'bar' }}"
                ),
            )

            // when
            val yaml = steps.stepsToYaml()

            // then
            yaml shouldBe """|- id: someId
                             |  name: Some external action
                             |  uses: actions/checkout@v3
                             |  with:
                             |    fetch-depth: 0
                             |  if: ${'$'}{{ matrix.foo == 'bar' }}""".trimMargin()
        }

        it("renders with action parameter that renders to more than one line") {
            // given
            val steps = listOf(
                ExternalActionStep(
                    id = "someId",
                    name = "Action with multiline parameter",
                    action = UploadArtifactV2(
                        name = "artifact",
                        path = listOf("path1", "path2"),
                    ),
                ),
            )

            // when
            val yaml = steps.stepsToYaml()

            // then
            yaml shouldBe """|- id: someId
                             |  name: Action with multiline parameter
                             |  uses: actions/upload-artifact@v2
                             |  with:
                             |    name: artifact
                             |    path: |
                             |      path1
                             |      path2""".trimMargin()
        }

        it("renders correctly with values starting with special YAML characters") {
            // given
            val steps = listOf(
                ExternalActionStep(
                    id = "someId",
                    name = "Action with values starting with special YAML characters",
                    action = object : Action("foo", "bar", "v2") {
                        override fun toYamlArguments() = linkedMapOf(
                            "param1" to "foo-bar",
                            "param2" to "*/some/dir",
                            "param3" to "**/another/dir",
                            "param4" to "[test]",
                            "param5" to "!another-reserved-character",
                        )
                    }
                ),
            )

            // when
            val yaml = steps.stepsToYaml()

            // then
            yaml shouldBe """|- id: someId
                             |  name: Action with values starting with special YAML characters
                             |  uses: foo/bar@v2
                             |  with:
                             |    param1: foo-bar
                             |    param2: '*/some/dir'
                             |    param3: '**/another/dir'
                             |    param4: '[test]'
                             |    param5: '!another-reserved-character'""".trimMargin()
        }
    }
})
