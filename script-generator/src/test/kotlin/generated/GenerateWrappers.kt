package generated

import it.krzeminski.githubactions.actions.actions.CheckoutV2
import it.krzeminski.githubactions.actions.actions.SetupJavaV2
import it.krzeminski.githubactions.actions.gradle.GradleBuildActionV2
import it.krzeminski.githubactions.domain.RunnerType
import it.krzeminski.githubactions.domain.RunnerType.UbuntuLatest
import it.krzeminski.githubactions.domain.Workflow
import it.krzeminski.githubactions.domain.triggers.Push
import it.krzeminski.githubactions.dsl.ListCustomValue
import it.krzeminski.githubactions.dsl.expr
import it.krzeminski.githubactions.dsl.workflow
import it.krzeminski.githubactions.yaml.toYaml
import java.nio.`file`.Paths
import kotlin.collections.mapOf

public val workflowGenerateWrappers: Workflow = workflow(
      name = "Generate wrappers",
      on = listOf(
        Push(
          branchesIgnore = listOf("main"),
        ),
        ),
      sourceFile = Paths.get("generate-wrappers.main.kts"),
      targetFile = Paths.get("yaml-output/generate-wrappers.yml"),
    ) {
      job("check_yaml_consistency", UbuntuLatest) {
        uses(
          name = "Check out",
          action = CheckoutV2(),
        )
        run(
          name = "Install Kotlin",
          command = "sudo snap install --classic kotlin",
        )
        run(
          name = "Consistency check",
          command =
              "diff -u '.github/workflows/generate-wrappers.yaml' <('.github/workflows/generate-wrappers.main.kts')",
        )
      }

      job("generate-wrappers", UbuntuLatest, _customArguments = mapOf(
      "needs" to ListCustomValue("check_yaml_consistency"),
      )
      ) {
        uses(
          name = "Checkout",
          action = CheckoutV2(),
        )
        uses(
          name = "Set up JDK",
          action = SetupJavaV2(
            javaVersion = "11",
            distribution = SetupJavaV2.Distribution.Adopt,
          ),
        )
        uses(
          name = "Generate wrappers",
          action = GradleBuildActionV2(
            arguments = ":wrapper-generator:run",
            _customVersion = "v1",
          ),
        )
        uses(
          name = "Check that the library builds fine with newly generated wrappers",
          action = GradleBuildActionV2(
            arguments = "build",
          ),
        )
        run(
          name = "Commit and push",
          command = """
          |git config --global user.email "<>"
          |git config --global user.name "GitHub Actions Bot"
          |git add .
          |git commit --allow-empty -m "Regenerate wrappers (${'$'}GITHUB_SHA)"  # an empty commit explicitly shows that the wrappers are up-to-date
          |git push
          """.trimMargin(),
        )
      }

    }
