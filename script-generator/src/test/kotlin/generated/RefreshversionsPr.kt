package generated

import it.krzeminski.githubactions.actions.actions.CheckoutV3
import it.krzeminski.githubactions.actions.actions.SetupJavaV3
import it.krzeminski.githubactions.actions.endbug.AddAndCommitV9
import it.krzeminski.githubactions.actions.gradle.GradleBuildActionV2
import it.krzeminski.githubactions.actions.reposync.PullRequestV2
import it.krzeminski.githubactions.domain.RunnerType
import it.krzeminski.githubactions.domain.Workflow
import it.krzeminski.githubactions.domain.triggers.Cron
import it.krzeminski.githubactions.domain.triggers.Schedule
import it.krzeminski.githubactions.domain.triggers.WorkflowDispatch
import it.krzeminski.githubactions.dsl.workflow
import java.nio.`file`.Paths

public val workflowRefreshversionsPr: Workflow = workflow(
      name = "RefreshVersions Pr",
      on = listOf(
        Schedule(listOf(
          Cron("0 7 * * 1"),
        )),
        WorkflowDispatch(),
        ),
      sourceFile = Paths.get(".github/workflows/refreshversions-pr.main.kts"),
    ) {
      job(
        id = "Refresh-Versions",
        runsOn = RunnerType.UbuntuLatest,
      ) {
        uses(
          name = "check-out",
          action = CheckoutV3(
            ref = "main",
          ),
        )
        uses(
          name = "setup-java",
          action = SetupJavaV3(
            javaVersion = "11",
            distribution = SetupJavaV3.Distribution.Adopt,
          ),
        )
        uses(
          name = "gradle refreshVersions",
          action = GradleBuildActionV2(
            arguments = "refreshVersions",
          ),
        )
        uses(
          name = "Commit",
          action = AddAndCommitV9(
            authorName = "GitHub Actions",
            authorEmail = "noreply@github.com",
            message = "Refresh versions.properties",
            newBranch = "dependency-update",
            push = "--force --set-upstream origin dependency-update",
          ),
        )
        uses(
          name = "Pull Request",
          action = PullRequestV2(
            sourceBranch = "dependency-update",
            destinationBranch = "main",
            prTitle = "Upgrade gradle dependencies",
            prBody =
                "[refreshVersions](https://github.com/jmfayard/refreshVersions) has found those library updates!",
            prDraft = true,
            githubToken = "${'$'}{{ secrets.GITHUB_TOKEN }}",
          ),
        )
      }

    }
