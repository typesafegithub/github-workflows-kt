package generated

import io.github.typesafegithub.workflows.actions.actions.CheckoutV3
import io.github.typesafegithub.workflows.actions.docker.BuildPushActionV4
import io.github.typesafegithub.workflows.actions.docker.LoginActionV2
import io.github.typesafegithub.workflows.actions.docker.SetupBuildxActionV2
import io.github.typesafegithub.workflows.domain.Concurrency
import io.github.typesafegithub.workflows.domain.RunnerType
import io.github.typesafegithub.workflows.domain.Workflow
import io.github.typesafegithub.workflows.domain.actions.CustomAction
import io.github.typesafegithub.workflows.domain.triggers.Push
import io.github.typesafegithub.workflows.dsl.expressions.expr
import io.github.typesafegithub.workflows.dsl.workflow
import io.github.typesafegithub.workflows.yaml.toYaml
import io.github.typesafegithub.workflows.yaml.writeToFile
import java.nio.`file`.Paths

public val workflowDockerImage: Workflow = workflow(
      name = "Docker Image",
      on = listOf(
        Push(
          branches = listOf("main", "feature/dockerfile"),
        ),
        ),
      sourceFile = Paths.get(".github/workflows/docker-image.main.kts"),
      concurrency = Concurrency(group = "workflow_staging_environment", cancelInProgress = true),
    ) {
      job(
        id = "push_image",
        runsOn = RunnerType.UbuntuLatest,
        concurrency = Concurrency(group = "job_staging_environment", cancelInProgress = true),
      ) {
        uses(
          name = "CheckoutV3",
          action = CheckoutV3(),
        )
        uses(
          name = "Set up QEMU",
          action = CustomAction(
            actionOwner = "docker",
            actionName = "setup-qemu-action",
            actionVersion = "v1",
            inputs = emptyMap()),
        )
        uses(
          name = "Set up Docker Buildx",
          action = SetupBuildxActionV2(),
        )
        uses(
          name = "Log in to the Container registry",
          action = LoginActionV2(
            registry = "ghcr.io",
            username = "${'$'}{{ github.actor }}",
            password = "${'$'}{{ secrets.GITHUB_TOKEN }}",
            _customInputs = mapOf(
              "custom-boolean" to "true",
              "custom-string" to "Hello World",
            ),
          ),
        )
        uses(
          name = "Build and push Docker image",
          action = BuildPushActionV4(
            context = ".",
            push = true,
            tags = listOf(
              "ghcr.io/mhprodev/mhddos:${'$'}{{ github.sha }},ghcr.io/mhprodev/mhddos:latest",
            )
            ,
            _customVersion = "v2.10.0",
          ),
        )
      }

    }
