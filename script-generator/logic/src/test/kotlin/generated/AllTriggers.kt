package generated

import io.github.typesafegithub.workflows.actions.actions.CheckoutV3
import io.github.typesafegithub.workflows.domain.RunnerType
import io.github.typesafegithub.workflows.domain.Workflow
import io.github.typesafegithub.workflows.domain.triggers.BranchProtectionRule
import io.github.typesafegithub.workflows.domain.triggers.CheckRun
import io.github.typesafegithub.workflows.domain.triggers.CheckSuite
import io.github.typesafegithub.workflows.domain.triggers.Create
import io.github.typesafegithub.workflows.domain.triggers.Cron
import io.github.typesafegithub.workflows.domain.triggers.Delete
import io.github.typesafegithub.workflows.domain.triggers.Deployment
import io.github.typesafegithub.workflows.domain.triggers.DeploymentStatus
import io.github.typesafegithub.workflows.domain.triggers.Discussion
import io.github.typesafegithub.workflows.domain.triggers.DiscussionComment
import io.github.typesafegithub.workflows.domain.triggers.Fork
import io.github.typesafegithub.workflows.domain.triggers.Gollum
import io.github.typesafegithub.workflows.domain.triggers.IssueComment
import io.github.typesafegithub.workflows.domain.triggers.Issues
import io.github.typesafegithub.workflows.domain.triggers.Label
import io.github.typesafegithub.workflows.domain.triggers.Milestone
import io.github.typesafegithub.workflows.domain.triggers.PageBuild
import io.github.typesafegithub.workflows.domain.triggers.Project
import io.github.typesafegithub.workflows.domain.triggers.ProjectCard
import io.github.typesafegithub.workflows.domain.triggers.ProjectColumn
import io.github.typesafegithub.workflows.domain.triggers.PublicWorkflow
import io.github.typesafegithub.workflows.domain.triggers.PullRequest
import io.github.typesafegithub.workflows.domain.triggers.PullRequestReview
import io.github.typesafegithub.workflows.domain.triggers.PullRequestReviewComment
import io.github.typesafegithub.workflows.domain.triggers.PullRequestTarget
import io.github.typesafegithub.workflows.domain.triggers.Push
import io.github.typesafegithub.workflows.domain.triggers.RegistryPackage
import io.github.typesafegithub.workflows.domain.triggers.Release
import io.github.typesafegithub.workflows.domain.triggers.RepositoryDispatch
import io.github.typesafegithub.workflows.domain.triggers.Schedule
import io.github.typesafegithub.workflows.domain.triggers.Status
import io.github.typesafegithub.workflows.domain.triggers.Watch
import io.github.typesafegithub.workflows.domain.triggers.WorkflowCall
import io.github.typesafegithub.workflows.domain.triggers.WorkflowDispatch
import io.github.typesafegithub.workflows.domain.triggers.WorkflowRun
import io.github.typesafegithub.workflows.dsl.expressions.expr
import io.github.typesafegithub.workflows.dsl.workflow
import io.github.typesafegithub.workflows.yaml.toYaml
import io.github.typesafegithub.workflows.yaml.writeToFile
import java.nio.`file`.Paths
import kotlin.collections.listOf
import kotlin.collections.mapOf

public val workflowAllTriggers: Workflow = workflow(
      name = "all-triggers",
      on = listOf(
        PullRequest(),
        Push(),
        PullRequestTarget(),
        Schedule(listOf(
          Cron("* * * * *"),
        )),
        WorkflowDispatch(),
        BranchProtectionRule(
          _customArguments = mapOf(
            "types" to listOf("created", "deleted")
          ),
        ),
        CheckRun(
          _customArguments = mapOf(
            "types" to listOf("completed", "rerequested")
          ),
        ),
        CheckSuite(),
        Create(),
        Delete(),
        Deployment(),
        DeploymentStatus(),
        Discussion(
          _customArguments = mapOf(
            "types" to listOf("created", "edited", "answered")
          ),
        ),
        DiscussionComment(),
        Fork(),
        Gollum(),
        IssueComment(
          _customArguments = mapOf(
            "types" to listOf("created", "edited", "deleted")
          ),
        ),
        Issues(
          _customArguments = mapOf(
            "types" to listOf("opened", "edited")
          ),
        ),
        Label(
          _customArguments = mapOf(
            "types" to listOf("crDiscussionCommenteated", "deleted", "edited")
          ),
        ),
        Milestone(
          _customArguments = mapOf(
            "types" to listOf("created", "closed")
          ),
        ),
        PageBuild(),
        PublicWorkflow(),
        Project(
          _customArguments = mapOf(
            "types" to listOf("created", "deleted")
          ),
        ),
        ProjectCard(
          _customArguments = mapOf(
            "types" to listOf("created", "moved")
          ),
        ),
        ProjectColumn(
          _customArguments = mapOf(
            "types" to listOf("moved")
          ),
        ),
        PullRequestReview(),
        PullRequestReviewComment(
          _customArguments = mapOf(
            "types" to listOf("created", "edited")
          ),
        ),
        RegistryPackage(
          _customArguments = mapOf(
            "types" to listOf("published", "updated")
          ),
        ),
        RepositoryDispatch(),
        Release(
          _customArguments = mapOf(
            "types" to listOf("published", "unpublished")
          ),
        ),
        Status(
          _customArguments = mapOf(
            "types" to listOf("started")
          ),
        ),
        Watch(),
        WorkflowCall(),
        WorkflowRun(
          _customArguments = mapOf(
            "types" to listOf("completed", "requested")
          ),
        ),
        ),
      sourceFile = Paths.get(".github/workflows/all-triggers.main.kts"),
    ) {
      job(
        id = "job-0",
        runsOn = RunnerType.UbuntuLatest,
      ) {
        uses(
          name = "Check out",
          action = CheckoutV3(),
        )
      }

    }
