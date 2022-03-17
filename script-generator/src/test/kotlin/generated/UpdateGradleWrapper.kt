package generated

import it.krzeminski.githubactions.actions.MissingAction
import it.krzeminski.githubactions.actions.actions.CheckoutV2
import it.krzeminski.githubactions.actions.gradleupdate.UpdateGradleWrapperActionV1
import it.krzeminski.githubactions.domain.RunnerType
import it.krzeminski.githubactions.domain.RunnerType.UbuntuLatest
import it.krzeminski.githubactions.domain.Workflow
import it.krzeminski.githubactions.domain.triggers.Cron
import it.krzeminski.githubactions.domain.triggers.Schedule
import it.krzeminski.githubactions.domain.triggers.WorkflowDispatch
import it.krzeminski.githubactions.dsl.ListCustomValue
import it.krzeminski.githubactions.dsl.expr
import it.krzeminski.githubactions.dsl.workflow
import it.krzeminski.githubactions.yaml.toYaml
import java.nio.`file`.Paths
import kotlin.collections.linkedMapOf
import kotlin.collections.mapOf

public val workflowUpdateGradleWrapper: Workflow = workflow(
      name = "Update Gradle Wrapper",
      on = listOf(
        Schedule(listOf(
          Cron("0 0 * * *"),
        )),
        WorkflowDispatch(),
        ),
      sourceFile = Paths.get("update-gradle-wrapper.main.kts"),
      targetFile = Paths.get("yaml-output/update-gradle-wrapper.yml"),
    ) {
      job("check_yaml_consistency", UbuntuLatest) {
        uses(
          name = "Check out",
          action = CheckoutV2(),
          condition = "true",
        )
        run(
          name = "Install Kotlin",
          command = "sudo snap install --classic kotlin",
        )
        run(
          name = "Consistency check",
          command =
              "diff -u '.github/workflows/update-gradle-wrapper.yml' <('.github/workflows/update-gradle-wrapper.main.kts')",
        )
      }

      job("update-gradle-wrapper", UbuntuLatest, _customArguments = mapOf(
      "needs" to ListCustomValue("check_yaml_consistency"),
      )
      ) {
        uses(
          name = "Checkout",
          action = CheckoutV2(),
        )
        uses(
          name = "Update Gradle Wrapper",
          action = UpdateGradleWrapperActionV1(),
        )
        uses(
          name = "Latex",
          action = MissingAction(
            actionOwner = "xu-cheng",
            actionName = "latex-action",
            actionVersion = "v2",
            freeArgs = linkedMapOf(
              "root_file" to "report.tex",
              "compiler" to "latexmk",
            )
          ),
        )
      }

    }
