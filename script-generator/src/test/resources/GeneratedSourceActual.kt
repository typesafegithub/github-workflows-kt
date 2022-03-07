package actual

import it.krzeminski.githubactions.actions.actions.CheckoutV2
import it.krzeminski.githubactions.actions.actions.SetupJavaV2
import it.krzeminski.githubactions.actions.docker.SetupBuildxActionV1
import it.krzeminski.githubactions.actions.gradle.GradleBuildActionV2
import it.krzeminski.githubactions.domain.RunnerType
import it.krzeminski.githubactions.domain.RunnerType.UbuntuLatest
import it.krzeminski.githubactions.domain.Workflow
import it.krzeminski.githubactions.domain.triggers.Cron
import it.krzeminski.githubactions.domain.triggers.PullRequest
import it.krzeminski.githubactions.domain.triggers.Push
import it.krzeminski.githubactions.domain.triggers.Schedule
import it.krzeminski.githubactions.domain.triggers.WorkflowDispatch
import it.krzeminski.githubactions.domain.triggers.WorkflowDispatch.Type.Choice
import it.krzeminski.githubactions.dsl.expr
import it.krzeminski.githubactions.dsl.workflow
import it.krzeminski.githubactions.yaml.toYaml
import java.nio.`file`.Paths
import kotlin.collections.linkedMapOf

public val workflowGenerated: Workflow = workflow(
      name = "generated",
      on = listOf(
        Push(
          branches = listOf("branch1", "branch2"),
          tags = listOf("tag1", "tag2"),
          paths = listOf("path1", "path2"),
        ),
        PullRequest(
          branches = listOf("branch1", "branch2"),
          paths = listOf("path1", "path2"),
          types = listOf(PullRequest.Type.AutoMergeDisabled, PullRequest.Type.Opened),
        ),
        Schedule(listOf(
          Cron("0 0 * * *"),
        )),
        WorkflowDispatch(mapOf(
          "logLevel" to WorkflowDispatch.Input(
            type = Choice,
            description = "Log level",
            default = "warning",
            required = true,
          ),
        ))
        ),
      sourceFile = Paths.get("generated.main.kts"),
      targetFile = Paths.get("generated.yml"),
      env = linkedMapOf(
        "GRADLE_ENTERPRISE_ACCESS_KEY" to expr("secrets.GRADLE_ENTERPRISE_ACCESS_KEY"),
        "GRADLE_BUILD_ACTION_CACHE_DEBUG_ENABLED" to "true",
      ),
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
          command = "diff -u '.github/workflows/build.yaml' <('.github/workflows/build.main.kts')",
          env = linkedMapOf(
            "HELLO" to "ok",
            "PAT" to "rick",
          ),
          condition = "true",
        )
      }

      job("build_for_UbuntuLatest", UbuntuLatest) {
        uses(
          name = "Checkout",
          action = CheckoutV2(),
          env = linkedMapOf(
            "HELLO" to "ok",
            "PAT" to "rick",
          ),
        )
        uses(
          name = "Set up JDK",
          action = SetupJavaV2(
            javaVersion = "11",
            distribution = SetupJavaV2.Distribution.Adopt,
          ),
          env = linkedMapOf(
            "HELLO" to "ok",
            "PAT" to "rick",
          ),
        )
        uses(
          name = "Build",
          action = GradleBuildActionV2(
            arguments = "build",
          ),
        )
        uses(
          name = "setup",
          action = SetupBuildxActionV1(
            driverOpts = listOf("hello", "world"),
            install = true,
          ),
        )
      }

    }.also {
        println("Generating YAML")
        println(it.toYaml(addConsistencyCheck = false))
    }
