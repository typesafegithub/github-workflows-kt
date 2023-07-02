package io.github.typesafegithub.workflows.yaml

import io.github.typesafegithub.workflows.domain.CommandStep
import io.github.typesafegithub.workflows.domain.Concurrency
import io.github.typesafegithub.workflows.domain.Container
import io.github.typesafegithub.workflows.domain.Job
import io.github.typesafegithub.workflows.domain.JobOutputs
import io.github.typesafegithub.workflows.domain.Mode
import io.github.typesafegithub.workflows.domain.Permission
import io.github.typesafegithub.workflows.domain.RunnerType
import io.github.typesafegithub.workflows.domain.RunnerType.UbuntuLatest
import io.github.typesafegithub.workflows.dsl.expressions.expr
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

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
                permissions = mapOf(
                    Permission.Actions to Mode.Read,
                    Permission.Checks to Mode.Write,
                    Permission.Contents to Mode.None,
                ),
                container = Container(image = "some-image"),
                services = mapOf(
                    "some-service" to Container(image = "some-service-image"),
                ),
                _customArguments = mapOf(
                    "distribute-job" to true,
                    "servers" to listOf("server-1", "server-2"),
                    "null-string" to "null",
                    "null-value" to null,
                    "empty-string" to "",
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
                "permissions" to mapOf(
                    "actions" to "read",
                    "checks" to "write",
                    "contents" to "none",
                ),
                "container" to mapOf(
                    "image" to "some-image",
                ),
                "services" to mapOf(
                    "some-service" to mapOf(
                        "image" to "some-service-image",
                    ),
                ),
                "distribute-job" to true,
                "servers" to listOf("server-1", "server-2"),
                "null-string" to "null",
                "null-value" to null,
                "empty-string" to "",
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

    context("RunnerType") {
        it("renders properly") {
            RunnerType.selfHosted()
                .toYaml() shouldBe listOf("self-hosted")
            RunnerType.selfHosted("self-hosted")
                .toYaml() shouldBe listOf("self-hosted")
            RunnerType.selfHosted("test-label", "test-arch", "test-label")
                .toYaml() shouldBe listOf("self-hosted", "test-label", "test-arch")

            RunnerType.Group("test-group")
                .toYaml() shouldBe mapOf("group" to "test-group")
            RunnerType.Group("test-group", "test-label", "test-label-2", "test-label")
                .toYaml() shouldBe mapOf(
                "group" to "test-group",
                "labels" to listOf("test-label", "test-label-2"),
            )

            RunnerType.Labelled("test-label", "test-label-2", "test-label")
                .toYaml() shouldBe listOf("test-label", "test-label-2")
        }
    }
})
