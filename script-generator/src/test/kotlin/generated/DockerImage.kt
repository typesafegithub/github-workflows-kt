package generated

import it.krzeminski.githubactions.actions.CustomAction
import it.krzeminski.githubactions.actions.actions.CheckoutV2
import it.krzeminski.githubactions.actions.docker.BuildPushActionV2
import it.krzeminski.githubactions.actions.docker.LoginActionV1
import it.krzeminski.githubactions.actions.docker.SetupBuildxActionV1
import it.krzeminski.githubactions.domain.RunnerType.UbuntuLatest
import it.krzeminski.githubactions.domain.Workflow
import it.krzeminski.githubactions.domain.triggers.Push
import it.krzeminski.githubactions.dsl.expr
import it.krzeminski.githubactions.dsl.workflow
import it.krzeminski.githubactions.yaml.toYaml
import java.nio.`file`.Paths

public val workflowDockerImage: Workflow = workflow(
      name = "Docker Image",
      on = listOf(
        Push(
          branches = listOf("main", "feature/dockerfile"),
        ),
        ),
      sourceFile = Paths.get("docker-image.main.kts"),
      targetFile = Paths.get("yaml-output/docker-image.yml"),
    ) {
      job("push_image", UbuntuLatest) {
        uses(
          name = "CheckoutV2",
          action = CheckoutV2(),
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
          action = SetupBuildxActionV1(),
        )
        uses(
          name = "Log in to the Container registry",
          action = LoginActionV1(
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
          action = BuildPushActionV2(
            context = ".",
            push = true,
            tags = listOf(
              "ghcr.io/mhprodev/mhddos:${'$'}{{ github.sha }},ghcr.io/mhprodev/mhddos:latest",
            )
            ,
          ),
        )
      }

    }
