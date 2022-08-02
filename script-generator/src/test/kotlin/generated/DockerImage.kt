package generated

import it.krzeminski.githubactions.actions.CustomAction
import it.krzeminski.githubactions.actions.actions.CheckoutV3
import it.krzeminski.githubactions.actions.docker.BuildPushActionV3
import it.krzeminski.githubactions.actions.docker.LoginActionV2
import it.krzeminski.githubactions.actions.docker.SetupBuildxActionV2
import it.krzeminski.githubactions.domain.Concurrency
import it.krzeminski.githubactions.domain.RunnerType
import it.krzeminski.githubactions.domain.Workflow
import it.krzeminski.githubactions.domain.triggers.Push
import it.krzeminski.githubactions.dsl.workflow
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
          action = BuildPushActionV3(
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
