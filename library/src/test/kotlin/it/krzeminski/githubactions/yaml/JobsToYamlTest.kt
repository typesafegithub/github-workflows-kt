package it.krzeminski.githubactions.yaml

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import it.krzeminski.githubactions.domain.CommandStep
import it.krzeminski.githubactions.domain.Concurrency
import it.krzeminski.githubactions.domain.Job
import it.krzeminski.githubactions.domain.JobOutputs
import it.krzeminski.githubactions.domain.RunnerType
import it.krzeminski.githubactions.domain.RunnerType.UbuntuLatest
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
                outputs = JobOutputs.EMPTY,
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
                outputs = JobOutputs.EMPTY,
            ),
        )

        // when
        val yaml = jobs.jobsToYaml()

        // then
        yaml shouldBe mapOf(
            "Job-1" to mapOf(
                "runs-on" to "ubuntu-latest",
                "steps" to listOf(
                    mapOf(
                        "id" to "someId",
                        "name" to "Some command 1",
                        "run" to "echo 'test 1!'",
                    ),
                ),
            ),
            "Job-2" to mapOf(
                "runs-on" to "ubuntu-latest",
                "steps" to listOf(
                    mapOf(
                        "id" to "someId",
                        "name" to "Some command 2",
                        "run" to "echo 'test 2!'",
                    ),
                ),
            ),
        )
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
                outputs = JobOutputs.EMPTY,
            ),
        )

        // when
        val yaml = jobs.jobsToYaml()

        // then
        yaml shouldBe mapOf(
            "Job-1" to mapOf(
                "runs-on" to "ubuntu-latest",
                "steps" to listOf(
                    mapOf(
                        "id" to "someId",
                        "name" to "Some command",
                        "run" to "echo 'test!'",
                    ),
                ),
            ),
        )
    }

    it("renders with all arguments") {
        // given
        val jobs = listOf(
            Job(
                id = "Job-1",
                name = "Some name",
                runsOn = UbuntuLatest,
                outputs = JobOutputs.EMPTY,
                env = linkedMapOf(
                    "FOO" to "bar",
                    "BAZ" to """
                        goo,
                        zoo
                    """.trimIndent(),
                ),
                strategyMatrix = mapOf(
                    "strategyParam1" to listOf("foo", "bar"),
                    "strategyParam2" to listOf("baz", "goo"),
                ),
                concurrency = Concurrency(
                    group = "group-name",
                    cancelInProgress = true,
                ),
                timeoutMinutes = 30,
                condition = "\${{ always() }}",
                steps = listOf(
                    CommandStep(
                        id = "someId",
                        name = "Some command",
                        command = "echo 'test!'",
                    ),
                ),
                _customArguments = mapOf(
                    "distribute-job" to true,
                    "servers" to listOf("server-1", "server-2"),
                ),
            ),
        )

        // when
        val yaml = jobs.jobsToYaml()

        // then
        yaml shouldBe mapOf(
            "Job-1" to mapOf(
                "name" to "Some name",
                "runs-on" to "ubuntu-latest",
                "env" to mapOf(
                    "FOO" to "bar",
                    "BAZ" to """
                        goo,
                        zoo
                    """.trimIndent(),
                ),
                "strategy" to mapOf(
                    "matrix" to mapOf(
                        "strategyParam1" to listOf("foo", "bar"),
                        "strategyParam2" to listOf("baz", "goo"),
                    ),
                ),
                "concurrency" to mapOf(
                    "group" to "group-name",
                    "cancel-in-progress" to true,
                ),
                "timeout-minutes" to 30,
                "if" to "\${{ always() }}",
                "steps" to listOf(
                    mapOf(
                        "id" to "someId",
                        "name" to "Some command",
                        "run" to "echo 'test!'",
                    ),
                ),
                "distribute-job" to true,
                "servers" to listOf("server-1", "server-2"),
            ),
        )
    }

    it("renders with dependencies on other jobs") {
        // given
        val anotherJob1 = Job(
            id = "Another-job-1",
            runsOn = UbuntuLatest,
            steps = listOf(),
            outputs = JobOutputs.EMPTY,
        )
        val anotherJob2 = Job(
            id = "Another-job-2",
            runsOn = UbuntuLatest,
            steps = listOf(),
            outputs = JobOutputs.EMPTY,
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
                outputs = JobOutputs.EMPTY,
            ),
        )

        // when
        val yaml = jobs.jobsToYaml()

        // then
        yaml shouldBe mapOf(
            "Job-1" to mapOf(
                "runs-on" to "ubuntu-latest",
                "needs" to listOf("Another-job-1", "Another-job-2"),
                "steps" to listOf(
                    mapOf(
                        "id" to "someId",
                        "name" to "Some command",
                        "run" to "echo 'test!'",
                    ),
                ),
            ),
        )
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
            outputs = JobOutputs.EMPTY,
        )

        // given
        val jobs = listOf(
            job("Job-1", RunnerType.Custom("windows-3.0")),
            job("Job-2", RunnerType.Custom(expr("github.event.inputs.run-on"))),
        )

        // when
        val yaml = jobs.jobsToYaml()

        // then
        yaml shouldBe mapOf(
            "Job-1" to mapOf(
                "runs-on" to "windows-3.0",
                "steps" to listOf(
                    mapOf(
                        "id" to "someId",
                        "name" to "Some command",
                        "run" to "echo 'test!'",
                    ),
                ),
            ),
            "Job-2" to mapOf(
                "runs-on" to "\${{ github.event.inputs.run-on }}",
                "steps" to listOf(
                    mapOf(
                        "id" to "someId",
                        "name" to "Some command",
                        "run" to "echo 'test!'",
                    ),
                ),
            ),
        )
    }

    it("renders with custom argument overriding built-in argument") {
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
                outputs = JobOutputs.EMPTY,
                _customArguments = mapOf(
                    "runs-on" to "overridden!",
                ),
            ),
        )

        // when
        val yaml = jobs.jobsToYaml()

        // then
        yaml shouldBe mapOf(
            "Job-1" to mapOf(
                "runs-on" to "overridden!",
                "steps" to listOf(
                    mapOf(
                        "id" to "someId",
                        "name" to "Some command",
                        "run" to "echo 'test!'",
                    ),
                ),
            ),
        )
    }
},)
