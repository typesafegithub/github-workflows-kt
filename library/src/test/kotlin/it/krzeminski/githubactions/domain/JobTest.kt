package it.krzeminski.githubactions.domain

import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.core.spec.style.FunSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.throwable.shouldHaveMessage

class JobTest : FunSpec({
    test("should reject invalid job names") {
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
                Job(jobName, RunnerType.UbuntuLatest, emptyList())
            }.shouldHaveMessage(
                """
                Invalid field Job(name="$jobName") does not match regex: [a-zA-Z_][a-zA-Z0-9_-]*
                See: https://docs.github.com/en/actions/using-jobs/using-jobs-in-a-workflow#setting-an-id-for-a-job
                """.trimIndent()
            )
        }
    }

    test("should not reject valid job names") {
        listOf(
            "job-42",
            "_42",
            "_job",
            "a",
            "JOB_JOB",
            "_--4",
        ).forAll { jobName ->
            Job(jobName, RunnerType.UbuntuLatest, emptyList())
        }
    }
})
