package generated

import io.github.typesafegithub.workflows.actions.actions.CheckoutV3
import io.github.typesafegithub.workflows.actions.actions.SetupJavaV3
import io.github.typesafegithub.workflows.actions.endbug.AddAndCommitV9
import io.github.typesafegithub.workflows.actions.gradle.GradleBuildActionV2
import io.github.typesafegithub.workflows.actions.reposync.PullRequestV2
import io.github.typesafegithub.workflows.domain.RunnerType
import io.github.typesafegithub.workflows.domain.Workflow
import io.github.typesafegithub.workflows.domain.actions.CustomAction
import io.github.typesafegithub.workflows.domain.triggers.Cron
import io.github.typesafegithub.workflows.domain.triggers.Schedule
import io.github.typesafegithub.workflows.domain.triggers.WorkflowDispatch
import io.github.typesafegithub.workflows.dsl.expressions.expr
import io.github.typesafegithub.workflows.dsl.workflow
import io.github.typesafegithub.workflows.yaml.toYaml
import io.github.typesafegithub.workflows.yaml.writeToFile
import java.nio.`file`.Paths
import kotlin.collections.mapOf

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
          name = "create-branch",
          action = CustomAction(
            actionOwner = "peterjgrainger",
            actionName = "action-create-branch",
            actionVersion = "v2.1.0",
            inputs = mapOf(
              "branch" to "dependency-update",
            )
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
