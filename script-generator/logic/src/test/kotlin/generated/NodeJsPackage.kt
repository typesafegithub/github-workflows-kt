package generated

import io.github.typesafegithub.workflows.actions.actions.CheckoutV3
import io.github.typesafegithub.workflows.actions.actions.SetupNodeV3
import io.github.typesafegithub.workflows.domain.Concurrency
import io.github.typesafegithub.workflows.domain.RunnerType
import io.github.typesafegithub.workflows.domain.Workflow
import io.github.typesafegithub.workflows.domain.triggers.Release
import io.github.typesafegithub.workflows.dsl.workflow
import java.nio.`file`.Paths
import kotlin.collections.mapOf

public val workflowNodejsPackage: Workflow = workflow(
      name = "NodeJs Package",
      on = listOf(
        Release(
          _customArguments = mapOf(
            "types" to listOf("created")
          ),
        ),
        ),
      sourceFile = Paths.get(".github/workflows/nodejs-package.main.kts"),
      concurrency = Concurrency(group = "workflow_staging_environment", cancelInProgress = false),
    ) {
      job(
        id = "build",
        runsOn = RunnerType.UbuntuLatest,
        concurrency = Concurrency(group = "job_staging_environment", cancelInProgress = false),
      ) {
        uses(
          name = "CheckoutV3",
          action = CheckoutV3(),
        )
        uses(
          name = "SetupNodeV3",
          action = SetupNodeV3(
            nodeVersion = "12.x",
            registryUrl = "https://npm.pkg.github.com",
            scope = "octocat",
          ),
        )
        run(
          name = "npm install",
          command = "npm install",
        )
        run(
          name = "npm publish",
          command = "npm publish",
          env = linkedMapOf(
            "NODE_AUTH_TOKEN" to "${'$'}",
          ),
        )
      }

    }
