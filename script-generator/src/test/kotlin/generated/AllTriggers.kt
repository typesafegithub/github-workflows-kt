package generated

import it.krzeminski.githubactions.actions.actions.CheckoutV3
import it.krzeminski.githubactions.domain.RunnerType
import it.krzeminski.githubactions.domain.Workflow
import it.krzeminski.githubactions.domain.triggers.BranchProtectionRule
import it.krzeminski.githubactions.domain.triggers.CheckRun
import it.krzeminski.githubactions.domain.triggers.CheckSuite
import it.krzeminski.githubactions.domain.triggers.Create
import it.krzeminski.githubactions.domain.triggers.Cron
import it.krzeminski.githubactions.domain.triggers.Delete
import it.krzeminski.githubactions.domain.triggers.Deployment
import it.krzeminski.githubactions.domain.triggers.DeploymentStatus
import it.krzeminski.githubactions.domain.triggers.Discussion
import it.krzeminski.githubactions.domain.triggers.DiscussionComment
import it.krzeminski.githubactions.domain.triggers.Fork
import it.krzeminski.githubactions.domain.triggers.Gollum
import it.krzeminski.githubactions.domain.triggers.IssueComment
import it.krzeminski.githubactions.domain.triggers.Issues
import it.krzeminski.githubactions.domain.triggers.Label
import it.krzeminski.githubactions.domain.triggers.Milestone
import it.krzeminski.githubactions.domain.triggers.PageBuild
import it.krzeminski.githubactions.domain.triggers.Project
import it.krzeminski.githubactions.domain.triggers.ProjectCard
import it.krzeminski.githubactions.domain.triggers.ProjectColumn
import it.krzeminski.githubactions.domain.triggers.PublicWorkflow
import it.krzeminski.githubactions.domain.triggers.PullRequest
import it.krzeminski.githubactions.domain.triggers.PullRequestReview
import it.krzeminski.githubactions.domain.triggers.PullRequestReviewComment
import it.krzeminski.githubactions.domain.triggers.PullRequestTarget
import it.krzeminski.githubactions.domain.triggers.Push
import it.krzeminski.githubactions.domain.triggers.RegistryPackage
import it.krzeminski.githubactions.domain.triggers.Release
import it.krzeminski.githubactions.domain.triggers.RepositoryDispatch
import it.krzeminski.githubactions.domain.triggers.Schedule
import it.krzeminski.githubactions.domain.triggers.Status
import it.krzeminski.githubactions.domain.triggers.Watch
import it.krzeminski.githubactions.domain.triggers.WorkflowCall
import it.krzeminski.githubactions.domain.triggers.WorkflowDispatch
import it.krzeminski.githubactions.domain.triggers.WorkflowRun
import it.krzeminski.githubactions.dsl.workflow
import java.nio.`file`.Paths
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
