package generated

import it.krzeminski.githubactions.actions.actions.CheckoutV2
import it.krzeminski.githubactions.domain.RunnerType.UbuntuLatest
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
import it.krzeminski.githubactions.dsl.ListCustomValue
import it.krzeminski.githubactions.dsl.expr
import it.krzeminski.githubactions.dsl.workflow
import it.krzeminski.githubactions.yaml.toYaml
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
            "types" to ListCustomValue("created", "deleted")
          ),
        ),
        CheckRun(
          _customArguments = mapOf(
            "types" to ListCustomValue("completed", "rerequested")
          ),
        ),
        CheckSuite(),
        Create(),
        Delete(),
        Deployment(),
        DeploymentStatus(),
        Discussion(
          _customArguments = mapOf(
            "types" to ListCustomValue("created", "edited", "answered")
          ),
        ),
        DiscussionComment(),
        Fork(),
        Gollum(),
        IssueComment(
          _customArguments = mapOf(
            "types" to ListCustomValue("created", "edited", "deleted")
          ),
        ),
        Issues(
          _customArguments = mapOf(
            "types" to ListCustomValue("opened", "edited")
          ),
        ),
        Label(
          _customArguments = mapOf(
            "types" to ListCustomValue("crDiscussionCommenteated", "deleted", "edited")
          ),
        ),
        Milestone(
          _customArguments = mapOf(
            "types" to ListCustomValue("created", "closed")
          ),
        ),
        PageBuild(),
        PublicWorkflow(),
        Project(
          _customArguments = mapOf(
            "types" to ListCustomValue("created", "deleted")
          ),
        ),
        ProjectCard(
          _customArguments = mapOf(
            "types" to ListCustomValue("created", "moved")
          ),
        ),
        ProjectColumn(
          _customArguments = mapOf(
            "types" to ListCustomValue("moved")
          ),
        ),
        PullRequestReview(),
        PullRequestReviewComment(
          _customArguments = mapOf(
            "types" to ListCustomValue("created", "edited")
          ),
        ),
        RegistryPackage(
          _customArguments = mapOf(
            "types" to ListCustomValue("published", "updated")
          ),
        ),
        RepositoryDispatch(),
        Release(
          _customArguments = mapOf(
            "types" to ListCustomValue("published", "unpublished")
          ),
        ),
        Status(
          _customArguments = mapOf(
            "types" to ListCustomValue("started")
          ),
        ),
        Watch(),
        WorkflowCall(),
        WorkflowRun(
          _customArguments = mapOf(
            "types" to ListCustomValue("completed", "requested")
          ),
        ),
        ),
      sourceFile = Paths.get("all-triggers.main.kts"),
      targetFile = Paths.get("yaml-output/all-triggers.yml"),
    ) {
      job("job-0", UbuntuLatest) {
        uses(
          name = "Check out",
          action = CheckoutV2(),
        )
      }

    }
