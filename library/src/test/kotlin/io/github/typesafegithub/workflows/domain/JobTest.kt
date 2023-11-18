package io.github.typesafegithub.workflows.domain

import io.github.typesafegithub.workflows.domain.AbstractResult.Status
import io.github.typesafegithub.workflows.domain.triggers.WorkflowDispatch
import io.github.typesafegithub.workflows.dsl.workflow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.core.spec.style.FunSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage

class JobTest : FunSpec({
    test("step.outcome step.conclusion job.result") {
        workflow(
            name = "some-workflow",
            on = listOf(WorkflowDispatch()),
        ) {
            val job = job(
                id = "some-id",
                runsOn = RunnerType.UbuntuLatest,
            ) {
                val step0 = run(command = "")
                step0.outcome.toString() shouldBe "steps.step-0.outcome"
                step0.outcome.eq(Status.Failure) shouldBe "steps.step-0.outcome == 'failure'"
                step0.outcome.eq(Status.Cancelled) shouldBe "steps.step-0.outcome == 'cancelled'"
                step0.outcome.eq(Status.Skipped) shouldBe "steps.step-0.outcome == 'skipped'"
                step0.outcome.eq(Status.Success) shouldBe "steps.step-0.outcome == 'success'"
                val step1 = run(command = "")
                step1.conclusion.toString() shouldBe "steps.step-1.conclusion"
                step1.conclusion.eq(Status.Failure) shouldBe "steps.step-1.conclusion == 'failure'"
                step1.conclusion.eq(Status.Cancelled) shouldBe "steps.step-1.conclusion == 'cancelled'"
                step1.conclusion.eq(Status.Skipped) shouldBe "steps.step-1.conclusion == 'skipped'"
                step1.conclusion.eq(Status.Success) shouldBe "steps.step-1.conclusion == 'success'"
            }
            job.result.toString() shouldBe "needs.some-id.result"
            job.result.eq(Status.Failure) shouldBe "needs.some-id.result == 'failure'"
            job.result.eq(Status.Cancelled) shouldBe "needs.some-id.result == 'cancelled'"
            job.result.eq(Status.Skipped) shouldBe "needs.some-id.result == 'skipped'"
            job.result.eq(Status.Success) shouldBe "needs.some-id.result == 'success'"
        }
    }
    context("outputs") {
        test("should include job outputs") {
            val job =
                Job(
                    id = "some-id",
                    name = "Some job",
                    runsOn = RunnerType.UbuntuLatest,
                    steps = listOf(),
                    outputs =
                        object : JobOutputs() {
                            var output1 by output()
                            var output2 by output()
                        },
                )
            job.outputs.output1 = "foo"
            job.outputs.output2 = "foo"

            job.outputs.output1 shouldBe "needs.some-id.outputs.output1"
            job.outputs.output2 shouldBe "needs.some-id.outputs.output2"

            job.result.toString() shouldBe "needs.some-id.result"
            job.result.eq(Status.Failure) shouldBe "needs.some-id.result == 'failure'"
            job.result.eq(Status.Cancelled) shouldBe "needs.some-id.result == 'cancelled'"
            job.result.eq(Status.Skipped) shouldBe "needs.some-id.result == 'skipped'"
            job.result.eq(Status.Success) shouldBe "needs.some-id.result == 'success'"
        }

        test("should throw if accessing uninitialized output") {
            val job =
                Job(
                    id = "some-id",
                    name = "Some job",
                    runsOn = RunnerType.UbuntuLatest,
                    steps = listOf(),
                    outputs =
                        object : JobOutputs() {
                            var output1 by output()
                        },
                )

            shouldThrow<IllegalStateException> {
                job.outputs.output1
            }
        }

        test("should throw if initializing output more second time") {
            val job =
                Job(
                    id = "some-id",
                    name = "Some job",
                    runsOn = RunnerType.UbuntuLatest,
                    steps = listOf(),
                    outputs =
                        object : JobOutputs() {
                            var output1 by output()
                        },
                )
            job.outputs.output1 = "foo"

            shouldThrow<IllegalStateException> {
                job.outputs.output1 = "bar"
                Unit
            }
        }
    }

    test("should reject invalid job IDs") {
        listOf(
            "job   1",
            "job1  ",
            "  job1",
            "",
            "-job",
            "4job",
            "job()",
        ).forAll { jobId ->
            shouldThrowAny {
                Job(jobId, null, RunnerType.UbuntuLatest, emptyList(), outputs = JobOutputs.EMPTY)
            }.shouldHaveMessage(
                """
                Invalid field Job(id="$jobId") does not match regex: [a-zA-Z_][a-zA-Z0-9_-]*
                See: https://docs.github.com/en/actions/using-jobs/using-jobs-in-a-workflow#setting-an-id-for-a-job
                """.trimIndent(),
            )
        }
    }

    test("should not reject valid job IDs") {
        listOf(
            "job-42",
            "_42",
            "_job",
            "a",
            "JOB_JOB",
            "_--4",
        ).forAll { jobName ->
            Job(jobName, null, RunnerType.UbuntuLatest, emptyList(), outputs = JobOutputs.EMPTY)
        }
    }

    test("should reject invalid timeout values") {
        shouldThrowAny {
            Job(
                id = "Job-1",
                runsOn = RunnerType.UbuntuLatest,
                timeoutMinutes = -1,
                steps =
                    listOf(
                        CommandStep(
                            id = "someId",
                            name = "Some command",
                            command = "echo 'test!'",
                        ),
                    ),
                outputs = JobOutputs.EMPTY,
            )
        } shouldHaveMessage "timeout should be positive"
    }
})
