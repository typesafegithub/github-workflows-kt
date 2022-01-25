package it.krzeminski.githubactions.yaml

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import it.krzeminski.githubactions.domain.Trigger.PullRequest
import it.krzeminski.githubactions.domain.Trigger.Push
import it.krzeminski.githubactions.domain.Trigger.WorkflowDispatch

class TriggersToYamlTest : DescribeSpec({
    it("renders multiple triggers") {
        // given
        val triggers = listOf(
            WorkflowDispatch,
            Push,
        )

        // when
        val yaml = triggers.triggersToYaml()

        // then
        yaml shouldBe """
            |workflow_dispatch:
            |push:
        """.trimMargin()
    }

    describe("workflow dispatch") {
        it("renders without parameters") {
            // given
            val triggers = listOf(WorkflowDispatch)

            // when
            val yaml = triggers.triggersToYaml()

            // then
            yaml shouldBe """
                |workflow_dispatch:
            """.trimMargin()
        }
    }

    describe("push") {
        it("renders without parameters") {
            // given
            val triggers = listOf(Push)

            // when
            val yaml = triggers.triggersToYaml()

            // then
            yaml shouldBe """
                |push:
            """.trimMargin()
        }
    }

    describe("pull request") {
        it("renders without parameters") {
            // given
            val triggers = listOf(PullRequest)

            // when
            val yaml = triggers.triggersToYaml()

            // then
            yaml shouldBe """
                |pull_request:
            """.trimMargin()
        }
    }
})
