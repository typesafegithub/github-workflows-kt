package io.github.typesafegithub.workflows.domain.triggers

import io.github.typesafegithub.workflows.yaml.triggersToYaml
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class OtherTriggersTest :
    FunSpec({

        test("Creating all triggers without any argument") {
            val triggers: List<Trigger> =
                listOf(
                    BranchProtectionRule(),
                    CheckRun(),
                    CheckSuite(),
                    Create(),
                    Delete(),
                    Deployment(),
                    DeploymentStatus(),
                    Discussion(),
                    DiscussionComment(),
                    Fork(),
                    Gollum(),
                    IssueComment(),
                    Issues(),
                    Label(),
                    MergeGroup(),
                    Milestone(),
                    PageBuild(),
                    Project(),
                    ProjectCard(),
                    ProjectColumn(),
                    PublicWorkflow(),
                    PullRequest(),
                    PullRequestReview(),
                    PullRequestReviewComment(),
                    PullRequestTarget(),
                    Push(),
                    RegistryPackage(),
                    Release(),
                    RepositoryDispatch(),
                    Schedule(emptyList()),
                    Status(),
                    Watch(),
                    WorkflowCall(),
                    WorkflowDispatch(),
                    WorkflowRun(),
                )

            triggers.triggersToYaml() shouldBe
                mapOf(
                    "branch_protection_rule" to emptyMap<Any, Any>(),
                    "check_run" to emptyMap<Any, Any>(),
                    "check_suite" to emptyMap<Any, Any>(),
                    "create" to emptyMap<Any, Any>(),
                    "delete" to emptyMap<Any, Any>(),
                    "deployment" to emptyMap<Any, Any>(),
                    "deployment_status" to emptyMap<Any, Any>(),
                    "discussion" to emptyMap<Any, Any>(),
                    "discussion_comment" to emptyMap<Any, Any>(),
                    "fork" to emptyMap<Any, Any>(),
                    "gollum" to emptyMap<Any, Any>(),
                    "issue_comment" to emptyMap<Any, Any>(),
                    "issues" to emptyMap<Any, Any>(),
                    "label" to emptyMap<Any, Any>(),
                    "merge_group" to emptyMap<Any, Any>(),
                    "milestone" to emptyMap<Any, Any>(),
                    "page_build" to emptyMap<Any, Any>(),
                    "project" to emptyMap<Any, Any>(),
                    "project_card" to emptyMap<Any, Any>(),
                    "project_column" to emptyMap<Any, Any>(),
                    "public" to emptyMap<Any, Any>(),
                    "pull_request" to emptyMap<Any, Any>(),
                    "pull_request_review" to emptyMap<Any, Any>(),
                    "pull_request_review_comment" to emptyMap<Any, Any>(),
                    "pull_request_target" to emptyMap<Any, Any>(),
                    "push" to emptyMap<Any, Any>(),
                    "registry_package" to emptyMap<Any, Any>(),
                    "release" to emptyMap<Any, Any>(),
                    "repository_dispatch" to emptyMap<Any, Any>(),
                    "schedule" to emptyList<String>(),
                    "status" to emptyMap<Any, Any>(),
                    "watch" to emptyMap<Any, Any>(),
                    "workflow_call" to emptyMap<Any, Any>(),
                    "workflow_dispatch" to emptyMap<Any, Any>(),
                    "workflow_run" to emptyMap<Any, Any>(),
                )
        }

        test("Creating all triggers with free arguments") {
            val triggers: List<Trigger> =
                listOf(
                    BranchProtectionRule(
                        types = listOf(BranchProtectionRule.EventType.Created, BranchProtectionRule.EventType.Deleted),
                    ),
                    CheckRun(
                        types = listOf(CheckRun.EventType.Completed, CheckRun.EventType.Rerequested),
                    ),
                    CheckSuite(),
                    Create(),
                    Delete(),
                    Deployment(),
                    DeploymentStatus(),
                    Discussion(
                        types =
                            listOf(
                                Discussion.EventType.Created,
                                Discussion.EventType.Edited,
                                Discussion.EventType.Answered,
                            ),
                    ),
                    DiscussionComment(),
                    Fork(),
                    Gollum(),
                    IssueComment(
                        types =
                            listOf(
                                IssueComment.EventType.Created,
                                IssueComment.EventType.Edited,
                                IssueComment.EventType.Deleted,
                            ),
                    ),
                    Issues(
                        types = listOf(Issues.EventType.Opened, Issues.EventType.Edited),
                    ),
                    Label(
                        types = listOf(Label.EventType.Created, Label.EventType.Deleted, Label.EventType.Edited),
                    ),
                    MergeGroup(),
                    Milestone(
                        types = listOf(Milestone.EventType.Created, Milestone.EventType.Closed),
                    ),
                    PageBuild(),
                    Project(
                        types = listOf(Project.EventType.Created, Project.EventType.Deleted),
                    ),
                    ProjectCard(
                        types = listOf(ProjectCard.EventType.Created, ProjectCard.EventType.Moved),
                    ),
                    ProjectColumn(
                        types = listOf(ProjectColumn.EventType.Moved),
                    ),
                    PublicWorkflow(),
                    PullRequest(),
                    PullRequestReview(),
                    PullRequestReviewComment(
                        types =
                            listOf(
                                PullRequestReviewComment.EventType.Created,
                                PullRequestReviewComment.EventType.Edited,
                            ),
                    ),
                    PullRequestTarget(),
                    Push(),
                    RegistryPackage(
                        types = listOf(RegistryPackage.EventType.Published, RegistryPackage.EventType.Updated),
                    ),
                    Release(types = listOf("published", "unpublished")),
                    RepositoryDispatch(types = listOf("foo", "bar")),
                    Schedule(emptyList()),
                    Status(
                        _customArguments = mapOf("types" to listOf("started")),
                    ),
                    Watch(),
                    WorkflowCall(),
                    WorkflowDispatch(),
                    WorkflowRun(
                        types = listOf(WorkflowRun.EventType.Completed, WorkflowRun.EventType.Requested),
                    ),
                )

            triggers.triggersToYaml() shouldBe
                mapOf(
                    "branch_protection_rule" to mapOf("types" to listOf("created", "deleted")),
                    "check_run" to mapOf("types" to listOf("completed", "rerequested")),
                    "check_suite" to emptyMap<Any, Any>(),
                    "create" to emptyMap<Any, Any>(),
                    "delete" to emptyMap<Any, Any>(),
                    "deployment" to emptyMap<Any, Any>(),
                    "deployment_status" to emptyMap<Any, Any>(),
                    "discussion" to mapOf("types" to listOf("created", "edited", "answered")),
                    "discussion_comment" to emptyMap<Any, Any>(),
                    "fork" to emptyMap<Any, Any>(),
                    "gollum" to emptyMap<Any, Any>(),
                    "issue_comment" to mapOf("types" to listOf("created", "edited", "deleted")),
                    "issues" to mapOf("types" to listOf("opened", "edited")),
                    "label" to mapOf("types" to listOf("created", "deleted", "edited")),
                    "merge_group" to emptyMap<Any, Any>(),
                    "milestone" to mapOf("types" to listOf("created", "closed")),
                    "page_build" to emptyMap<Any, Any>(),
                    "project" to mapOf("types" to listOf("created", "deleted")),
                    "project_card" to mapOf("types" to listOf("created", "moved")),
                    "project_column" to mapOf("types" to listOf("moved")),
                    "public" to emptyMap<Any, Any>(),
                    "pull_request" to emptyMap<Any, Any>(),
                    "pull_request_review" to emptyMap<Any, Any>(),
                    "pull_request_review_comment" to mapOf("types" to listOf("created", "edited")),
                    "pull_request_target" to emptyMap<Any, Any>(),
                    "push" to emptyMap<Any, Any>(),
                    "registry_package" to mapOf("types" to listOf("published", "updated")),
                    "release" to mapOf("types" to listOf("published", "unpublished")),
                    "repository_dispatch" to mapOf("types" to listOf("foo", "bar")),
                    "schedule" to emptyList<String>(),
                    "status" to mapOf("types" to listOf("started")),
                    "watch" to emptyMap<Any, Any>(),
                    "workflow_call" to emptyMap<Any, Any>(),
                    "workflow_dispatch" to emptyMap<Any, Any>(),
                    "workflow_run" to mapOf("types" to listOf("completed", "requested")),
                )
        }
    })
