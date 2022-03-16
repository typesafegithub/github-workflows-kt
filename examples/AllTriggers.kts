#!/usr/bin/env kotlin

@file:DependsOn("it.krzeminski:github-actions-kotlin-dsl:0.9.0")

import it.krzeminski.githubactions.actions.actions.CheckoutV3
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
import it.krzeminski.githubactions.domain.triggers.PullRequest
import it.krzeminski.githubactions.domain.triggers.PullRequestReviewComment
import it.krzeminski.githubactions.domain.triggers.PullRequestTarget
import it.krzeminski.githubactions.domain.triggers.Push
import it.krzeminski.githubactions.domain.triggers.RegistryPackage
import it.krzeminski.githubactions.domain.triggers.Release
import it.krzeminski.githubactions.domain.triggers.Schedule
import it.krzeminski.githubactions.domain.triggers.Status
import it.krzeminski.githubactions.domain.triggers.Watch
import it.krzeminski.githubactions.domain.triggers.WorkflowCall
import it.krzeminski.githubactions.domain.triggers.WorkflowDispatch
import it.krzeminski.githubactions.domain.triggers.WorkflowRun
import it.krzeminski.githubactions.dsl.expr
import it.krzeminski.githubactions.dsl.workflow
import it.krzeminski.githubactions.yaml.toYaml
import java.nio.`file`.Paths

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
        BranchProtectionRule().types("created", "deleted"),
        CheckRun().types("completed", "rerequested"),
        CheckSuite(),
        Create(),
        Delete(),
        Deployment(),
        DeploymentStatus(),
        Discussion().types("created", "edited", "answered"),
        DiscussionComment(),
        Fork(),
        Gollum(),
        IssueComment().types("created", "edited", "deleted"),
        Issues().types("opened", "edited"),
        Label().types("crDiscussionCommenteated", "deleted", "edited"),
        Milestone().types("created", "closed"),
        PageBuild(),
        Project().types("created", "deleted"),
        ProjectCard().types("created", "moved"),
        ProjectColumn().types("moved"),
        PullRequestReviewComment().types("created", "edited"),
        RegistryPackage().types("published", "updated"),
        Release().types("published", "unpublished"),
        Status().types("started"),
        Watch(),
        WorkflowCall(),
        WorkflowRun().types("completed", "requested"),
        ),
      sourceFile = Paths.get("all-triggers.main.kts"),
      targetFile = Paths.get("all-triggers.yml"),
    ) {
      job("job-0", UbuntuLatest) {
        uses(
          name = "Check out",
          action = CheckoutV3(),
        )
      }

    }.also {
        println("Generating YAML")
        println(it.toYaml(addConsistencyCheck = false))
    }
