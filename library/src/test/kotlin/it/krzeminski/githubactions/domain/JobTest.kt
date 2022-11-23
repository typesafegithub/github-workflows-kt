package it.krzeminski.githubactions.domain

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.core.spec.style.FunSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage

class JobTest : FunSpec({
    context("outputs") {
        test("should include job outputs") {
            val job = Job(
                id = "some-id",
                name = "Some job",
                runsOn = RunnerType.UbuntuLatest,
                steps = listOf(),
                outputs = object : JobOutputs() {
                    var output1 by output()
                    var output2 by output()
                },
            )
            job.outputs.output1 = "foo"
            job.outputs.output2 = "foo"

            job.outputs.output1 shouldBe "needs.some-id.outputs.output1"
            job.outputs.output2 shouldBe "needs.some-id.outputs.output2"
        }

        test("should throw if accessing uninitialized output") {
            val job = Job(
                id = "some-id",
                name = "Some job",
                runsOn = RunnerType.UbuntuLatest,
                steps = listOf(),
                outputs = object : JobOutputs() {
                    var output1 by output()
                },
            )

            shouldThrow<IllegalStateException> {
                job.outputs.output1
            }
        }

        test("should throw if initializing output more second time") {
            val job = Job(
                id = "some-id",
                name = "Some job",
                runsOn = RunnerType.UbuntuLatest,
                steps = listOf(),
                outputs = object : JobOutputs() {
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
                steps = listOf(
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
},)
