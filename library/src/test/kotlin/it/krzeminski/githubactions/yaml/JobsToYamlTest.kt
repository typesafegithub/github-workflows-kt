package it.krzeminski.githubactions.yaml

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe
import it.krzeminski.githubactions.domain.CommandStep
import it.krzeminski.githubactions.domain.Job
import it.krzeminski.githubactions.domain.RunnerType.UbuntuLatest
import it.krzeminski.githubactions.domain.RunnerType.Windows2022

class JobsToYamlTest : DescribeSpec({
    it("renders multiple jobs") {
        // given
        val jobs = listOf(
            Job(
                name = "Job 1",
                runsOn = UbuntuLatest,
                steps = listOf(
                    CommandStep(
                        name = "Some command 1",
                        command = "echo 'test 1!'",
                    ),
                ),
            ),
            Job(
                name = "Job 2",
                runsOn = UbuntuLatest,
                steps = listOf(
                    CommandStep(
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
                         |    - name: Some command 1
                         |      run: echo 'test 1!'
                         |"Job-2":
                         |  runs-on: "ubuntu-latest"
                         |  steps:
                         |    - name: Some command 2
                         |      run: echo 'test 2!'""".trimMargin()
    }

    it("renders with required arguments") {
        // given
        val jobs = listOf(
            Job(
                name = "Job 1",
                runsOn = UbuntuLatest,
                steps = listOf(
                    CommandStep(
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
                         |    - name: Some command
                         |      run: echo 'test!'""".trimMargin()
    }

    it("renders with custom runner") {
        // given
        val jobs = listOf(
            Job(
                name = "Job 1",
                runsOn = Windows2022,
                steps = listOf(
                    CommandStep(
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
                         |    - name: Some command
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
                name = "Job 1",
                runsOn = UbuntuLatest,
                needs = listOf(anotherJob1, anotherJob2),
                steps = listOf(
                    CommandStep(
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
                         |    - name: Some command
                         |      run: echo 'test!'""".trimMargin()
    }

    it("renders with environment variables") {
        // given
        val jobs = listOf(
            Job(
                name = "Job 1",
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
                         |    - name: Some command 1
                         |      run: echo 'test 1!'""".trimMargin()
    }

    it("renders with condition") {
        // given
        val jobs = listOf(
            Job(
                name = "Job 1",
                runsOn = UbuntuLatest,
                condition = "\${{ always() }}",
                steps = listOf(
                    CommandStep(
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
                         |    - name: Some command 1
                         |      run: echo 'test 1!'""".trimMargin()
    }

    it("renders with strategy matrix") {
        // given
        val jobs = listOf(
            Job(
                name = "Job 1",
                runsOn = UbuntuLatest,
                strategyMatrix = mapOf(
                    "strategyParam1" to listOf("foo", "bar"),
                    "strategyParam2" to listOf("baz", "goo"),
                ),
                steps = listOf(
                    CommandStep(
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
                         |    - name: Some command
                         |      run: echo 'test!'""".trimMargin()
    }

    it("should escape the job name") {
        mapOf(
            "  job   1  " to "job-1",
            "jo#b€$1" to "jo-b-1",
            "JOB job 1" to "JOB-job-1",
            "job_1 42" to "job_1-42",
            "-job" to "job",
        ).forAll { (input, expected) ->
            Job(input, UbuntuLatest, emptyList()).escapedName shouldBe expected
        }
    }
})
