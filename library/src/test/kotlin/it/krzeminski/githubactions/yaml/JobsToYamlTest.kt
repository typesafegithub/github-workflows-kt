package it.krzeminski.githubactions.yaml

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import it.krzeminski.githubactions.domain.CommandStep
import it.krzeminski.githubactions.domain.Job
import it.krzeminski.githubactions.domain.RunnerType
import it.krzeminski.githubactions.domain.RunnerType.UbuntuLatest
import it.krzeminski.githubactions.domain.RunnerType.Windows2022
import it.krzeminski.githubactions.dsl.expressions.expr

class JobsToYamlTest : DescribeSpec({
    it("renders multiple jobs") {
        // given
        val jobs = listOf(
            Job(
                id = "Job-1",
                runsOn = UbuntuLatest,
                steps = listOf(
                    CommandStep(
                        id = "someId",
                        name = "Some command 1",
                        command = "echo 'test 1!'",
                    ),
                ),
            ),
            Job(
                id = "Job-2",
                runsOn = UbuntuLatest,
                steps = listOf(
                    CommandStep(
                        id = "someId",
                        name = "Some command 2",
                        command = "echo 'test 2!'",
                    ),
                ),
            ),
        )

        // when
        val yaml = jobs.jobsToYaml()

        // then
        yaml shouldBe """|"Job-1":
                         |  runs-on: "ubuntu-latest"
                         |  steps:
                         |    - id: someId
                         |      name: Some command 1
                         |      run: echo 'test 1!'
                         |"Job-2":
                         |  runs-on: "ubuntu-latest"
                         |  steps:
                         |    - id: someId
                         |      name: Some command 2
                         |      run: echo 'test 2!'""".trimMargin()
    }

    it("renders with required arguments") {
        // given
        val jobs = listOf(
            Job(
                id = "Job-1",
                runsOn = UbuntuLatest,
                steps = listOf(
                    CommandStep(
                        id = "someId",
                        name = "Some command",
                        command = "echo 'test!'",
                    ),
                ),
            ),
        )

        // when
        val yaml = jobs.jobsToYaml()

        // then
        yaml shouldBe """|"Job-1":
                         |  runs-on: "ubuntu-latest"
                         |  steps:
                         |    - id: someId
                         |      name: Some command
                         |      run: echo 'test!'""".trimMargin()
    }

    it("renders with custom runner") {
        // given
        val jobs = listOf(
            Job(
                id = "Job-1",
                runsOn = Windows2022,
                steps = listOf(
                    CommandStep(
                        id = "someId",
                        name = "Some command",
                        command = "echo 'test!'",
                    ),
                ),
            ),
        )

        // when
        val yaml = jobs.jobsToYaml()

        // then
        yaml shouldBe """|"Job-1":
                         |  runs-on: "windows-2022"
                         |  steps:
                         |    - id: someId
                         |      name: Some command
                         |      run: echo 'test!'""".trimMargin()
    }

    it("renders with dependencies on other jobs") {
        // given
        val anotherJob1 = Job(
            id = "Another-job-1",
            runsOn = UbuntuLatest,
            steps = listOf(),
        )
        val anotherJob2 = Job(
            id = "Another-job-2",
            runsOn = UbuntuLatest,
            steps = listOf(),
        )
        val jobs = listOf(
            Job(
                id = "Job-1",
                runsOn = UbuntuLatest,
                needs = listOf(anotherJob1, anotherJob2),
                steps = listOf(
                    CommandStep(
                        id = "someId",
                        name = "Some command",
                        command = "echo 'test!'",
                    ),
                ),
            ),
        )

        // when
        val yaml = jobs.jobsToYaml()

        // then
        yaml shouldBe """|"Job-1":
                         |  runs-on: "ubuntu-latest"
                         |  needs:
                         |    - "Another-job-1"
                         |    - "Another-job-2"
                         |  steps:
                         |    - id: someId
                         |      name: Some command
                         |      run: echo 'test!'""".trimMargin()
    }

    it("renders with environment variables") {
        // given
        val jobs = listOf(
            Job(
                id = "Job-1",
                runsOn = UbuntuLatest,
                env = linkedMapOf(
                    "FOO" to "bar",
                    "BAZ" to """
                        goo,
                        zoo
                    """.trimIndent()
                ),
                condition = "\${{ always() }}",
                steps = listOf(
                    CommandStep(
                        id = "someId",
                        name = "Some command 1",
                        command = "echo 'test 1!'",
                    ),
                ),
            ),
        )

        // when
        val yaml = jobs.jobsToYaml()

        // then
        yaml shouldBe """|"Job-1":
                         |  runs-on: "ubuntu-latest"
                         |  env:
                         |    FOO: bar
                         |    BAZ: |
                         |      goo,
                         |      zoo
                         |  if: ${'$'}{{ always() }}
                         |  steps:
                         |    - id: someId
                         |      name: Some command 1
                         |      run: echo 'test 1!'""".trimMargin()
    }

    it("renders with condition") {
        // given
        val jobs = listOf(
            Job(
                id = "Job-1",
                runsOn = UbuntuLatest,
                condition = "\${{ always() }}",
                steps = listOf(
                    CommandStep(
                        id = "someId",
                        name = "Some command 1",
                        command = "echo 'test 1!'",
                    ),
                ),
            ),
        )

        // when
        val yaml = jobs.jobsToYaml()

        // then
        yaml shouldBe """|"Job-1":
                         |  runs-on: "ubuntu-latest"
                         |  if: ${'$'}{{ always() }}
                         |  steps:
                         |    - id: someId
                         |      name: Some command 1
                         |      run: echo 'test 1!'""".trimMargin()
    }

    it("renders with strategy matrix") {
        // given
        val jobs = listOf(
            Job(
                id = "Job-1",
                runsOn = UbuntuLatest,
                strategyMatrix = mapOf(
                    "strategyParam1" to listOf("foo", "bar"),
                    "strategyParam2" to listOf("baz", "goo"),
                ),
                steps = listOf(
                    CommandStep(
                        id = "someId",
                        name = "Some command",
                        command = "echo 'test!'",
                    ),
                ),
            ),
        )

        // when
        val yaml = jobs.jobsToYaml()

        // then
        yaml shouldBe """|"Job-1":
                         |  runs-on: "ubuntu-latest"
                         |  strategy:
                         |    matrix:
                         |      strategyParam1:
                         |        - foo
                         |        - bar
                         |      strategyParam2:
                         |        - baz
                         |        - goo
                         |  steps:
                         |    - id: someId
                         |      name: Some command
                         |      run: echo 'test!'""".trimMargin()
    }

    it("should accept custom arguments") {
        // given
        val jobs = listOf(
            Job(
                id = "Job-1",
                runsOn = RunnerType.UbuntuLatest,
                _customArguments = mapOf(
                    "distribute-job" to true,
                    "servers" to listOf("server-1", "server-2")
                ),
                steps = listOf(
                    CommandStep(
                        id = "someId",
                        name = "Some command",
                        command = "echo 'test!'",
                    ),
                ),
            ),
        )

        // when
        val yaml = jobs.jobsToYaml()

        // then
        yaml shouldBe """|"Job-1":
                         |  runs-on: "ubuntu-latest"
                         |  steps:
                         |    - id: someId
                         |      name: Some command
                         |      run: echo 'test!'
                         |  distribute-job: true
                         |  servers:
                         |  - server-1
                         |  - server-2
                         """.trimMargin()
    }

    it("renders timeout-minutes") {
        // given
        val jobs = listOf(
            Job(
                id = "Job-1",
                runsOn = RunnerType.UbuntuLatest,
                timeoutMinutes = 30,
                steps = listOf(
                    CommandStep(
                        id = "someId",
                        name = "Some command",
                        command = "echo 'test!'",
                    ),
                ),
            ),
        )

        // when
        val yaml = jobs.jobsToYaml()

        // then
        yaml shouldBe """
             |"Job-1":
             |  runs-on: "ubuntu-latest"
             |  timeout-minutes: 30
             |  steps:
             |    - id: someId
             |      name: Some command
             |      run: echo 'test!'
             """.trimMargin()
    }

    it("renders a custom RunnerType - hardcoded or from an expression") {
        fun job(id: String, runnerType: RunnerType) = Job(
            id = id,
            runsOn = runnerType,
            steps = listOf(
                CommandStep(
                    id = "someId",
                    name = "Some command",
                    command = "echo 'test!'",
                ),
            ),
        )

        // given
        val jobs = listOf(
            job("Job-1", RunnerType.Custom("windows-3.0")),
            job("Job-2", RunnerType.Custom(expr("github.event.inputs.run-on"))),
        )

        // when
        val yaml = jobs.jobsToYaml()

        // then
        yaml shouldBe """
             |"Job-1":
             |  runs-on: "windows-3.0"
             |  steps:
             |    - id: someId
             |      name: Some command
             |      run: echo 'test!'
             |"Job-2":
             |  runs-on: "${'$'}{{ github.event.inputs.run-on }}"
             |  steps:
             |    - id: someId
             |      name: Some command
             |      run: echo 'test!'
             """.trimMargin()
    }
})
