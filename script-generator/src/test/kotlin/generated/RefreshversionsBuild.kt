package generated

import io.github.typesafegithub.workflows.actions.actions.CheckoutV3
import io.github.typesafegithub.workflows.actions.actions.SetupJavaV3
import io.github.typesafegithub.workflows.actions.gradle.GradleBuildActionV2
import io.github.typesafegithub.workflows.domain.RunnerType
import io.github.typesafegithub.workflows.domain.Workflow
import io.github.typesafegithub.workflows.domain.triggers.PullRequest
import io.github.typesafegithub.workflows.domain.triggers.Push
import io.github.typesafegithub.workflows.domain.triggers.WorkflowDispatch
import io.github.typesafegithub.workflows.domain.triggers.WorkflowDispatch.Type.Boolean
import io.github.typesafegithub.workflows.domain.triggers.WorkflowDispatch.Type.Choice
import io.github.typesafegithub.workflows.dsl.expressions.expr
import io.github.typesafegithub.workflows.dsl.workflow
import java.nio.`file`.Paths
import kotlin.collections.listOf

public val workflowRefreshversionsBuild: Workflow = workflow(
      name = "RefreshVersions build",
      on = listOf(
        PullRequest(
          paths = listOf("plugins/**", "sample-kotlin/**", "sample-groovy/**", "!**.md",
              "!.editorconfig", "!**/.gitignore", "!**.adoc", "!docs/**"),
        ),
        Push(
          branches = listOf("main"),
          paths = listOf("plugins/**", "sample-kotlin/**", "sample-groovy/**", "!**.md",
              "!.editorconfig", "!**/.gitignore", "!**.adoc", "!docs/**"),
        ),
        WorkflowDispatch(mapOf(
          "run-refreshVersions-task" to WorkflowDispatch.Input(
            type = Boolean,
            description = "Run the refreshVersions task",
            default = "false",
            required = true,
          ),
          "sample-kotlin" to WorkflowDispatch.Input(
            type = Boolean,
            description = "Enable sample-kotlin",
            default = "true",
            required = true,
          ),
          "sample-groovy" to WorkflowDispatch.Input(
            type = Boolean,
            description = "Enable sample-groovy",
            default = "true",
            required = true,
          ),
          "sample-multi-modules" to WorkflowDispatch.Input(
            type = Boolean,
            description = "Enable sample-multi-modules",
            default = "true",
            required = true,
          ),
          "sample-kotlin-js" to WorkflowDispatch.Input(
            type = Boolean,
            description = "Enable sample-kotlin-js",
            default = "true",
            required = true,
          ),
          "sample-android" to WorkflowDispatch.Input(
            type = Boolean,
            description = "Enable sample-android",
            default = "false",
            required = true,
          ),
          "run-on" to WorkflowDispatch.Input(
            type = Choice,
            description = "Where to run this workflow",
            default = "ubuntu-latest",
            required = true,
            options = listOf("windows-2022", "windows-2019", "windows-latest", "macos-11",
                "macos-10.5", "macos-latest", "ubuntu-20.04", "ubuntu-18.04", "ubuntu-latest"),
          ),
        ))
        ),
      sourceFile = Paths.get(".github/workflows/refreshversions-build.main.kts"),
    ) {
      job(
        id = "check-all",
        runsOn = RunnerType.Custom(expr("github.event.inputs.run-on || 'ubuntu-latest'")),
      ) {
        run(
          name = "Enable long paths for git Windows",
          command = "git config --global core.longpaths true",
          condition = expr("runner.os == 'Windows'"),
        )
        uses(
          name = "CheckoutV3",
          action = CheckoutV3(),
        )
        uses(
          name = "Configure JDK",
          action = SetupJavaV3(
            distribution = SetupJavaV3.Distribution.Adopt,
            javaVersion = "11",
          ),
        )
        uses(
          name = "Check plugins and publish them to MavenLocal",
          action = GradleBuildActionV2(
            gradleExecutable = "plugins/gradlew",
            buildRootDirectory = "plugins",
            arguments = "check publishToMavenLocal --stacktrace",
          ),
        )
        uses(
          name = "Run refreshVersions on sample-kotlin",
          action = GradleBuildActionV2(
            gradleExecutable = "sample-kotlin/gradlew",
            buildRootDirectory = "sample-kotlin",
            arguments = "refreshVersions --stacktrace",
          ),
          condition =
              expr("github.event_name != 'workflow_dispatch' || github.event.inputs.sample-kotlin == 'true' && github.event.inputs.run-refreshVersions-task == 'true'"),
        )
        uses(
          name = "Check sample-kotlin",
          action = GradleBuildActionV2(
            gradleExecutable = "sample-kotlin/gradlew",
            buildRootDirectory = "sample-kotlin",
            arguments = "check --stacktrace --configuration-cache",
          ),
          condition =
              expr("github.event_name != 'workflow_dispatch' || github.event.inputs.sample-kotlin == 'true'"),
        )
        uses(
          name = "Run refreshVersions on sample-groovy",
          action = GradleBuildActionV2(
            gradleExecutable = "sample-groovy/gradlew",
            buildRootDirectory = "sample-groovy",
            arguments = "refreshVersions --stacktrace",
          ),
          condition =
              expr("github.event.inputs.sample-groovy == 'true' && github.event.inputs.run-refreshVersions-task == 'true'"),
        )
        uses(
          name = "Check sample-groovy",
          action = GradleBuildActionV2(
            gradleExecutable = "sample-groovy/gradlew",
            buildRootDirectory = "sample-groovy",
            arguments = "check --stacktrace",
          ),
          condition = expr("github.event.inputs.sample-groovy == 'true'"),
        )
        uses(
          name = "Check buildSrc of sample-groovy (simulates IDE Gradle sync)",
          action = GradleBuildActionV2(
            gradleExecutable = "sample-groovy/gradlew",
            buildRootDirectory = "sample-groovy/buildSrc",
            arguments = "help --stacktrace",
          ),
          condition = expr("github.event.inputs.sample-groovy == 'true'"),
        )
        uses(
          name = "Run refreshVersions on sample-multi-modules",
          action = GradleBuildActionV2(
            gradleExecutable = "sample-multi-modules/gradlew",
            buildRootDirectory = "sample-multi-modules",
            arguments = "refreshVersions --stacktrace",
          ),
          condition =
              expr("github.event.inputs.sample-multi-modules == 'true' && github.event.inputs.run-refreshVersions-task == 'true'"),
        )
        uses(
          name = "Check sample-multi-modules",
          action = GradleBuildActionV2(
            gradleExecutable = "sample-multi-modules/gradlew",
            buildRootDirectory = "sample-multi-modules",
            arguments = "check --stacktrace",
          ),
          condition = expr("github.event.inputs.sample-multi-modules == 'true'"),
        )
        uses(
          name = "Run refreshVersions on sample-kotlin-js",
          action = GradleBuildActionV2(
            gradleExecutable = "sample-kotlin-js/gradlew",
            buildRootDirectory = "sample-kotlin-js",
            arguments = "refreshVersions --stacktrace",
          ),
          condition =
              expr("github.event.inputs.sample-kotlin-js == 'true' && github.event.inputs.run-refreshVersions-task == 'true'"),
        )
        uses(
          name = "Check sample-kotlin-js",
          action = GradleBuildActionV2(
            gradleExecutable = "sample-kotlin-js/gradlew",
            buildRootDirectory = "sample-kotlin-js",
            arguments = "check --stacktrace",
          ),
          condition = expr("github.event.inputs.sample-kotlin-js == 'true'"),
        )
        uses(
          name = "Run refreshVersions on sample-android",
          action = GradleBuildActionV2(
            gradleExecutable = "sample-android/gradlew",
            buildRootDirectory = "sample-android",
            arguments = "refreshVersions --stacktrace",
          ),
          condition =
              expr("github.event.inputs.sample-android == 'true' && github.event.inputs.run-refreshVersions-task == 'true'"),
        )
        uses(
          name = "Check sample-android",
          action = GradleBuildActionV2(
            gradleExecutable = "sample-android/gradlew",
            buildRootDirectory = "sample-android",
            arguments = "check --stacktrace",
          ),
          condition = expr("github.event.inputs.sample-android == 'true'"),
        )
      }

    }
