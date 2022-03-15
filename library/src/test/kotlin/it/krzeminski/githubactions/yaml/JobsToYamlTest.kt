package it.krzeminski.githubactions.yaml

import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import it.krzeminski.githubactions.domain.CommandStep
import it.krzeminski.githubactions.domain.Job
import it.krzeminski.githubactions.domain.RunnerType
import it.krzeminski.githubactions.domain.RunnerType.UbuntuLatest
import it.krzeminski.githubactions.domain.RunnerType.Windows2022
import it.krzeminski.githubactions.dsl.BooleanCustomValue
import it.krzeminski.githubactions.dsl.ListCustomValue

class JobsToYamlTest : DescribeSpec({
    it("renders multiple jobs") {
        // given
        val jobs = listOf(
            Job(
                name = "Job-1",
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
                name = "Job-2",
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
                name = "Job-1",
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
                name = "Job-1",
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
            name = "Another job 1",
            runsOn = UbuntuLatest,
            steps = listOf(),
        )
        val anotherJob2 = Job(
            name = "Another job 2",
            runsOn = UbuntuLatest,
            steps = listOf(),
        )
        val jobs = listOf(
            Job(
                name = "Job-1",
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
                         |    - "Another job 1"
                         |    - "Another job 2"
                         |  steps:
                         |    - id: someId
                         |      name: Some command
                         |      run: echo 'test!'""".trimMargin()
    }

    it("renders with environment variables") {
        // given
        val jobs = listOf(
            Job(
                name = "Job-1",
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
                name = "Job-1",
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
                name = "Job-1",
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

    it("should reject invalid job names") {
        listOf(
            "job   1",
            "job1  ",
            "  job1",
            "",
            "-job",
            "4job",
            "job()"
        ).forAll { jobName ->
            shouldThrowAny {
                val job = Job(jobName, UbuntuLatest, emptyList())
                listOf(job).jobsToYaml()
            }.shouldHaveMessage(
                """
                Invalid field Job(name="$jobName") does not match regex: [a-zA-Z_][a-zA-Z0-9_-]*
                See: https://docs.github.com/en/actions/using-jobs/using-jobs-in-a-workflow#setting-an-id-for-a-job
                """.trimIndent()
            )
        }
    }

    it("should not reject valid job names") {
        listOf(
            "job-42",
            "_42",
            "_job",
            "a",
            "JOB_JOB",
            "_--4",
        ).forAll { jobName ->
            val job = Job(jobName, UbuntuLatest, emptyList())
            listOf(job).jobsToYaml()
        }
    }

    it("should accept custom arguments") {
        // given
        val jobs = listOf(
            Job(
                name = "Job-1",
                runsOn = RunnerType.UbuntuLatest,
                _customArguments = mapOf(
                    "distribute-job" to BooleanCustomValue(true),
                    "servers" to ListCustomValue("server-1", "server-2")
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
                         |    - 'server-1'
                         |    - 'server-2'
                         """.trimMargin()
    }
})
