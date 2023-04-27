package generated

import io.github.typesafegithub.workflows.actions.actions.CheckoutV3
import io.github.typesafegithub.workflows.actions.actions.SetupJavaV3
import io.github.typesafegithub.workflows.actions.gradle.GradleBuildActionV2
import io.github.typesafegithub.workflows.domain.RunnerType
import io.github.typesafegithub.workflows.domain.Workflow
import io.github.typesafegithub.workflows.domain.triggers.Push
import io.github.typesafegithub.workflows.dsl.expressions.expr
import io.github.typesafegithub.workflows.dsl.workflow
import io.github.typesafegithub.workflows.yaml.toYaml
import io.github.typesafegithub.workflows.yaml.writeToFile
import java.nio.`file`.Paths
import kotlin.collections.mapOf

public val workflowGenerateWrappers: Workflow = workflow(
      name = "Generate wrappers",
      on = listOf(
        Push(
          branchesIgnore = listOf("main"),
        ),
        ),
      sourceFile = Paths.get(".github/workflows/generate-wrappers.main.kts"),
    ) {
      job(
        id = "check_yaml_consistency",
        name = "Check YAML consistency",
        runsOn = RunnerType.UbuntuLatest,
      ) {
        uses(
          name = "Check out",
          action = CheckoutV3(),
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

      job(
        id = "generate-wrappers",
        runsOn = RunnerType.UbuntuLatest,
        _customArguments = mapOf(
        "needs" to listOf("check_yaml_consistency"),
        )
      ) {
        uses(
          name = "Checkout",
          action = CheckoutV3(),
        )
        uses(
          name = "Set up JDK",
          action = SetupJavaV3(
            javaVersion = "11",
            distribution = SetupJavaV3.Distribution.Adopt,
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
          |""".trimMargin(),
        )
      }

    }
