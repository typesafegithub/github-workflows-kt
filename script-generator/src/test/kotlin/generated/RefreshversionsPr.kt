package generated

import it.krzeminski.githubactions.actions.actions.CheckoutV2
import it.krzeminski.githubactions.actions.actions.SetupJavaV2
import it.krzeminski.githubactions.actions.endbug.AddAndCommitV8
import it.krzeminski.githubactions.actions.gradle.GradleBuildActionV2
import it.krzeminski.githubactions.actions.peterjgrainger.ActionCreateBranchV2
import it.krzeminski.githubactions.actions.reposync.PullRequestV2
import it.krzeminski.githubactions.domain.RunnerType.UbuntuLatest
import it.krzeminski.githubactions.domain.Workflow
import it.krzeminski.githubactions.domain.triggers.Cron
import it.krzeminski.githubactions.domain.triggers.Schedule
import it.krzeminski.githubactions.domain.triggers.WorkflowDispatch
import it.krzeminski.githubactions.dsl.expr
import it.krzeminski.githubactions.dsl.workflow
import it.krzeminski.githubactions.yaml.toYaml
import java.nio.`file`.Paths

public val workflowRefreshversionsPr: Workflow = workflow(
      name = "RefreshVersions Pr",
      on = listOf(
        Schedule(listOf(
          Cron("0 7 * * 1"),
        )),
        WorkflowDispatch(),
        ),
      sourceFile = Paths.get("refreshversions-pr.main.kts"),
      targetFile = Paths.get("yaml-output/refreshversions-pr.yml"),
    ) {
      job("Refresh-Versions", UbuntuLatest) {
        uses(
          name = "check-out",
          action = CheckoutV2(
            ref = "main",
          ),
        )
        uses(
          name = "setup-java",
          action = SetupJavaV2(
            javaVersion = "11",
            distribution = SetupJavaV2.Distribution.Adopt,
          ),
        )
        uses(
          name = "create-branch",
          action = ActionCreateBranchV2(
            branch = "dependency-update",
          ),
          env = linkedMapOf(
            "GITHUB_TOKEN" to expr("secrets.GITHUB_TOKEN"),
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
          action = AddAndCommitV8(
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
