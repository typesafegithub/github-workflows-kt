#!/usr/bin/env kotlin

@file:DependsOn("it.krzeminski:github-actions-kotlin-dsl:0.9.0")

import it.krzeminski.githubactions.actions.actions.CheckoutV3
import it.krzeminski.githubactions.actions.actions.SetupJavaV2
import it.krzeminski.githubactions.actions.gradle.GradleBuildActionV2
import it.krzeminski.githubactions.domain.RunnerType.UbuntuLatest
import it.krzeminski.githubactions.domain.Workflow
import it.krzeminski.githubactions.domain.triggers.PullRequest
import it.krzeminski.githubactions.domain.triggers.Push
import it.krzeminski.githubactions.domain.triggers.WorkflowDispatch
import it.krzeminski.githubactions.domain.triggers.WorkflowDispatch.Type.Boolean
import it.krzeminski.githubactions.domain.triggers.WorkflowDispatch.Type.Choice
import it.krzeminski.githubactions.dsl.expr
import it.krzeminski.githubactions.dsl.workflow
import it.krzeminski.githubactions.yaml.toYaml
import java.nio.`file`.Paths

public val workflowCheckBuild: Workflow = workflow(
      name = "Check build",
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
          ),
        ))
        ),
      sourceFile = Paths.get("check-build.main.kts"),
      targetFile = Paths.get("check-build.yml"),
    ) {
      job("check-all", UbuntuLatest) {
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
          action = SetupJavaV2(
            distribution = SetupJavaV2.Distribution.Adopt,
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

    }.also {
        println("Generating YAML")
        println(it.toYaml(addConsistencyCheck = false))
    }
