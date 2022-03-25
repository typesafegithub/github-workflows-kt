package it.krzeminski.githubactions.domain

import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.core.spec.style.FunSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.throwable.shouldHaveMessage

class JobTest : FunSpec({
    test("should reject invalid job IDs") {
        listOf(
            "job   1",
            "job1  ",
            "  job1",
            "",
            "-job",
            "4job",
            "job()"
        ).forAll { jobId ->
            shouldThrowAny {
                Job(jobId, null, RunnerType.UbuntuLatest, emptyList())
            }.shouldHaveMessage(
                """
                Invalid field Job(id="$jobId") does not match regex: [a-zA-Z_][a-zA-Z0-9_-]*
                See: https://docs.github.com/en/actions/using-jobs/using-jobs-in-a-workflow#setting-an-id-for-a-job
                """.trimIndent()
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
            Job(jobName, null, RunnerType.UbuntuLatest, emptyList())
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
            )
        } shouldHaveMessage "timeout should be positive"
    }
})
