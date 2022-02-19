package it.krzeminski.githubactions.yaml

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import it.krzeminski.githubactions.domain.triggers.Cron
import it.krzeminski.githubactions.domain.triggers.PullRequest
import it.krzeminski.githubactions.domain.triggers.PullRequestTarget
import it.krzeminski.githubactions.domain.triggers.Push
import it.krzeminski.githubactions.domain.triggers.Schedule
import it.krzeminski.githubactions.domain.triggers.WorkflowDispatch

class TriggersToYamlTest : DescribeSpec({
    it("renders multiple triggers") {
        // given
        val triggers = listOf(
            WorkflowDispatch(),
            Push(),
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
            val triggers = listOf(WorkflowDispatch())

            // when
            val yaml = triggers.triggersToYaml()

            // then
            yaml shouldBe """
                |workflow_dispatch:
            """.trimMargin()
        }

        it("renders with all parameters") {

            // given
            val triggers = listOf(
                WorkflowDispatch(
                    inputs = mapOf(
                        "logLevel" to WorkflowDispatch.Input(
                            description = "Log level",
                            type = WorkflowDispatch.Type.Choice,
                            required = true,
                            default = "warning",
                            options = listOf("info", "warning", "debug"),
                        ),
                        "tags" to WorkflowDispatch.Input(
                            description = "Test scenario tags",
                            type = WorkflowDispatch.Type.Boolean,
                            required = false,
                        ),
                        "environment" to WorkflowDispatch.Input(
                            description = "Environment to run tests against",
                            type = WorkflowDispatch.Type.Environment,
                            required = true,
                        ),
                        "greeting" to WorkflowDispatch.Input(
                            description = "Hello {greeting}",
                            type = WorkflowDispatch.Type.String,
                            required = true,
                        ),
                    )
                )
            )

            // when
            val yaml = triggers.triggersToYaml()

            // then
            yaml shouldBe """
              |workflow_dispatch:
              |  inputs:
              |    logLevel:
              |      description: 'Log level'
              |      type: choice
              |      required: true
              |      default: 'warning'
              |      options:
              |        - 'info'
              |        - 'warning'
              |        - 'debug'
              |    tags:
              |      description: 'Test scenario tags'
              |      type: boolean
              |      required: false
              |    environment:
              |      description: 'Environment to run tests against'
              |      type: environment
              |      required: true
              |    greeting:
              |      description: 'Hello {greeting}'
              |      type: string
              |      required: true
            """.trimMargin()
        }
    }

    describe("push") {
        it("renders without parameters") {
            // given
            val triggers = listOf(Push())

            // when
            val yaml = triggers.triggersToYaml()

            // then
            yaml shouldBe """
                |push:
            """.trimMargin()
        }

        it("renders with all parameters") {
            // given
            val triggers = listOf(
                Push(
                    branches = listOf("branch1", "branch2"),
                    tags = listOf("tag1", "tag2"),
                    branchesIgnore = listOf("branchIgnore1", "branchIgnore2"),
                    tagsIgnore = listOf("tagIgnore1", "tagIgnore2"),
                    paths = listOf("path1", "path2"),
                    pathsIgnore = listOf("pathIgnore1", "pathIgnore2"),
                ),
            )

            // when
            val yaml = triggers.triggersToYaml()

            // then
            yaml shouldBe """
                |push:
                |  branches:
                |    - 'branch1'
                |    - 'branch2'
                |  tags:
                |    - 'tag1'
                |    - 'tag2'
                |  branches-ignore:
                |    - 'branchIgnore1'
                |    - 'branchIgnore2'
                |  tags-ignore:
                |    - 'tagIgnore1'
                |    - 'tagIgnore2'
                |  paths:
                |    - 'path1'
                |    - 'path2'
                |  paths-ignore:
                |    - 'pathIgnore1'
                |    - 'pathIgnore2'
            """.trimMargin()
        }
    }

    describe("pull request") {
        it("renders without parameters") {
            // given
            val triggers = listOf(PullRequest())

            // when
            val yaml = triggers.triggersToYaml()

            // then
            yaml shouldBe """
                |pull_request:
            """.trimMargin()
        }

        it("renders with all parameters valid to put together - first part") {
            // given
            val triggers = listOf(
                PullRequest(
                    branches = listOf("branch1", "branch2"),
                    paths = listOf("path1", "path2"),
                )
            )

            // when
            val yaml = triggers.triggersToYaml()

            // then
            yaml shouldBe """
                |pull_request:
                |  branches:
                |    - 'branch1'
                |    - 'branch2'
                |  paths:
                |    - 'path1'
                |    - 'path2'
            """.trimMargin()
        }

        it("renders with all parameters valid to put together - second part") {
            // given
            val triggers = listOf(
                PullRequest(
                    branchesIgnore = listOf("branchIgnore1", "branchIgnore2"),
                    pathsIgnore = listOf("pathIgnore1", "pathIgnore2"),
                )
            )

            // when
            val yaml = triggers.triggersToYaml()

            // then
            yaml shouldBe """
                |pull_request:
                |  branches-ignore:
                |    - 'branchIgnore1'
                |    - 'branchIgnore2'
                |  paths-ignore:
                |    - 'pathIgnore1'
                |    - 'pathIgnore2'
            """.trimMargin()
        }
    }

    describe("pull request target") {
        it("renders without parameters") {
            // given
            val triggers = listOf(PullRequestTarget())

            // when
            val yaml = triggers.triggersToYaml()

            // then
            yaml shouldBe """
                |pull_request_target:
            """.trimMargin()
        }

        it("renders with all parameters") {
            // given
            val triggers = listOf(
                PullRequestTarget(
                    types = listOf(PullRequestTarget.Type.Assigned, PullRequestTarget.Type.Closed),
                    branches = listOf("branch1", "branch2"),
                    branchesIgnore = listOf("branchIgnore1", "branchIgnore2"),
                    paths = listOf("path1", "path2"),
                    pathsIgnore = listOf("pathIgnore1", "pathIgnore2"),
                )
            )

            // when
            val yaml = triggers.triggersToYaml()

            // then
            yaml shouldBe """
                |pull_request_target:
                |  types:
                |    - 'assigned'
                |    - 'closed'
                |  branches:
                |    - 'branch1'
                |    - 'branch2'
                |  branches-ignore:
                |    - 'branchIgnore1'
                |    - 'branchIgnore2'
                |  paths:
                |    - 'path1'
                |    - 'path2'
                |  paths-ignore:
                |    - 'pathIgnore1'
                |    - 'pathIgnore2'
            """.trimMargin()
        }
    }

    describe("schedule") {
        it("renders with single cron trigger") {
            // given
            val triggers = listOf(
                Schedule(
                    triggers = listOf(
                        Cron("30 5,17 * * *"),
                    ),
                )
            )

            // when
            val yaml = triggers.triggersToYaml()

            // then
            yaml shouldBe """
                |schedule:
                | - cron: '30 5,17 * * *'
            """.trimMargin()
        }

        it("renders with multiple cron triggers") {
            // given
            val triggers = listOf(
                Schedule(
                    triggers = listOf(
                        Cron("30 5,17 * * *"),
                        Cron("0 0 * * *"),
                    ),
                )
            )

            // when
            val yaml = triggers.triggersToYaml()

            // then
            yaml shouldBe """
                |schedule:
                | - cron: '30 5,17 * * *'
                | - cron: '0 0 * * *'
            """.trimMargin()
        }
    }
})
