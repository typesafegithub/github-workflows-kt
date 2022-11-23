package it.krzeminski.githubactions.yaml

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import it.krzeminski.githubactions.domain.triggers.Cron
import it.krzeminski.githubactions.domain.triggers.PullRequest
import it.krzeminski.githubactions.domain.triggers.PullRequestTarget
import it.krzeminski.githubactions.domain.triggers.Push
import it.krzeminski.githubactions.domain.triggers.Schedule
import it.krzeminski.githubactions.domain.triggers.Trigger
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
        yaml shouldBe mapOf(
            "workflow_dispatch" to emptyMap<Any, Any>(),
            "push" to emptyMap(),
        )
    }

    describe("workflow dispatch") {
        it("renders without parameters") {
            // given
            val triggers = listOf(WorkflowDispatch())

            // when
            val yaml = triggers.triggersToYaml()

            // then
            yaml shouldBe mapOf(
                "workflow_dispatch" to emptyMap<Any, Any>(),
            )
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
                    ),
                ),
            )

            // when
            val yaml = triggers.triggersToYaml()

            // then
            yaml shouldBe mapOf(
                "workflow_dispatch" to mapOf(
                    "inputs" to mapOf(
                        "logLevel" to mapOf(
                            "description" to "Log level",
                            "type" to "choice",
                            "required" to true,
                            "default" to "warning",
                            "options" to listOf("info", "warning", "debug"),
                        ),
                        "tags" to mapOf(
                            "description" to "Test scenario tags",
                            "type" to "boolean",
                            "required" to false,
                        ),
                        "environment" to mapOf(
                            "description" to "Environment to run tests against",
                            "type" to "environment",
                            "required" to true,
                        ),
                        "greeting" to mapOf(
                            "description" to "Hello {greeting}",
                            "type" to "string",
                            "required" to true,
                        ),
                    ),
                ),
            )
        }
    }

    describe("push") {
        it("renders without parameters") {
            // given
            val triggers = listOf(Push())

            // when
            val yaml = triggers.triggersToYaml()

            // then
            yaml shouldBe mapOf(
                "push" to emptyMap<Any, Any>(),
            )
        }

        it("renders with all parameters valid to put together - first part") {
            // given
            val triggers = listOf(
                Push(
                    tags = listOf("tag1", "tag2"),
                    branches = listOf("branch1", "branch2"),
                    paths = listOf("path1", "path2"),
                ),
            )

            // when
            val yaml = triggers.triggersToYaml()

            // then
            yaml shouldBe mapOf(
                "push" to mapOf(
                    "branches" to listOf("branch1", "branch2"),
                    "tags" to listOf("tag1", "tag2"),
                    "paths" to listOf("path1", "path2"),
                ),
            )
        }

        it("renders with all parameters valid to put together - second part") {
            // given
            val triggers = listOf(
                Push(
                    branchesIgnore = listOf("branchIgnore1", "branchIgnore2"),
                    tagsIgnore = listOf("tagIgnore1", "tagIgnore2"),
                    pathsIgnore = listOf("pathIgnore1", "pathIgnore2"),
                ),
            )

            // when
            val yaml = triggers.triggersToYaml()

            // then
            yaml shouldBe mapOf(
                "push" to mapOf(
                    "branches-ignore" to listOf("branchIgnore1", "branchIgnore2"),
                    "tags-ignore" to listOf("tagIgnore1", "tagIgnore2"),
                    "paths-ignore" to listOf("pathIgnore1", "pathIgnore2"),
                ),
            )
        }
    }

    describe("pull request") {
        it("renders without parameters") {
            // given
            val triggers = listOf(PullRequest())

            // when
            val yaml = triggers.triggersToYaml()

            // then
            yaml shouldBe mapOf(
                "pull_request" to emptyMap<Any, Any>(),
            )
        }

        it("renders with all parameters valid to put together - first part") {
            // given
            val triggers = listOf(
                PullRequest(
                    branches = listOf("branch1", "branch2"),
                    paths = listOf("path1", "path2"),
                    types = listOf(PullRequest.Type.AutoMergeDisabled, PullRequest.Type.Opened),
                ),
            )

            // when
            val yaml = triggers.triggersToYaml()

            // then
            yaml shouldBe mapOf(
                "pull_request" to mapOf(
                    "types" to listOf("auto_merge_disabled", "opened"),
                    "branches" to listOf("branch1", "branch2"),
                    "paths" to listOf("path1", "path2"),
                ),
            )
        }

        it("renders with all parameters valid to put together - second part") {
            // given
            val triggers = listOf(
                PullRequest(
                    branchesIgnore = listOf("branchIgnore1", "branchIgnore2"),
                    pathsIgnore = listOf("pathIgnore1", "pathIgnore2"),
                ),
            )

            // when
            val yaml = triggers.triggersToYaml()

            // then
            yaml shouldBe mapOf(
                "pull_request" to mapOf(
                    "branches-ignore" to listOf("branchIgnore1", "branchIgnore2"),
                    "paths-ignore" to listOf("pathIgnore1", "pathIgnore2"),
                ),
            )
        }
    }

    describe("pull request target") {
        it("renders without parameters") {
            // given
            val triggers = listOf(PullRequestTarget())

            // when
            val yaml = triggers.triggersToYaml()

            // then
            yaml shouldBe mapOf(
                "pull_request_target" to emptyMap<Any, Any>(),
            )
        }

        it("renders with all parameters valid to put together - first part") {
            // given
            val triggers = listOf(
                PullRequestTarget(
                    types = listOf(PullRequestTarget.Type.Assigned, PullRequestTarget.Type.Closed),
                    branches = listOf("branch1", "branch2"),
                    paths = listOf("path1", "path2"),
                ),
            )

            // when
            val yaml = triggers.triggersToYaml()

            // then
            yaml shouldBe mapOf(
                "pull_request_target" to mapOf(
                    "types" to listOf("assigned", "closed"),
                    "branches" to listOf("branch1", "branch2"),
                    "paths" to listOf("path1", "path2"),
                ),
            )
        }

        it("renders with all parameters valid to put together - second part") {
            // given
            val triggers = listOf(
                PullRequestTarget(
                    types = listOf(PullRequestTarget.Type.Assigned, PullRequestTarget.Type.Closed),
                    branchesIgnore = listOf("branchIgnore1", "branchIgnore2"),
                    pathsIgnore = listOf("pathIgnore1", "pathIgnore2"),
                ),
            )

            // when
            val yaml = triggers.triggersToYaml()

            // then
            yaml shouldBe mapOf(
                "pull_request_target" to mapOf(
                    "types" to listOf("assigned", "closed"),
                    "branches-ignore" to listOf("branchIgnore1", "branchIgnore2"),
                    "paths-ignore" to listOf("pathIgnore1", "pathIgnore2"),
                ),
            )
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
                ),
            )

            // when
            val yaml = triggers.triggersToYaml()

            // then
            yaml shouldBe mapOf(
                "schedule" to listOf(
                    mapOf("cron" to "30 5,17 * * *"),
                ),
            )
        }

        it("renders with multiple cron triggers") {
            // given
            val triggers = listOf(
                Schedule(
                    triggers = listOf(
                        Cron("30 5,17 * * *"),
                        Cron("0 0 * * *"),
                    ),
                ),
            )

            // when
            val yaml = triggers.triggersToYaml()

            // then
            yaml shouldBe mapOf(
                "schedule" to listOf(
                    mapOf("cron" to "30 5,17 * * *"),
                    mapOf("cron" to "0 0 * * *"),
                ),
            )
        }
    }

    describe("triggers with custom arguments") {
        val triggers: List<Trigger> = listOf(
            PullRequest(
                _customArguments = mapOf(
                    "types" to listOf("ignored"),
                    "lang" to "french",
                    "fast" to true,
                    "answer" to 42,
                    "list" to listOf(1, 2, 3),
                ),
            ),
            WorkflowDispatch(
                _customArguments = mapOf(
                    "lang" to "french",
                    "list" to listOf(1, 2, 3),
                ),
            ),
            Push(
                _customArguments = mapOf(
                    "branches" to listOf("main", "master"),
                    "tags" to listOf("tag1", "tag2"),
                ),
            ),
            Schedule(
                triggers = emptyList(),
                _customArguments = mapOf(
                    "cron" to "0 7 * * *",
                    "object" to
                        mapOf(
                            "some-property" to "good",
                            "other-property" to "better",
                        ),
                ),
            ),
            PullRequestTarget(
                _customArguments = mapOf(
                    "branches" to listOf("main", "master"),
                    "tags" to listOf("tag1", "tag2"),
                ),
            ),
        )

        triggers.triggersToYaml() shouldBe mapOf(
            "pull_request" to mapOf(
                "types" to listOf("ignored"),
                "lang" to "french",
                "fast" to true,
                "answer" to 42,
                "list" to listOf(1, 2, 3),
            ),
            "workflow_dispatch" to mapOf(
                "lang" to "french",
                "list" to listOf(1, 2, 3),
            ),
            "push" to mapOf(
                "branches" to listOf("main", "master"),
                "tags" to listOf("tag1", "tag2"),
            ),
            "schedule" to mapOf(
                "cron" to "0 7 * * *",
                "object" to
                    mapOf(
                        "some-property" to "good",
                        "other-property" to "better",
                    ),
            ),
            "pull_request_target" to mapOf(
                "branches" to listOf("main", "master"),
                "tags" to listOf("tag1", "tag2"),
            ),
        )
    }
},)
