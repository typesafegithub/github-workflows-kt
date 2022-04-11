package generated

import it.krzeminski.githubactions.actions.actions.CheckoutV2
import it.krzeminski.githubactions.actions.actions.SetupNodeV3
import it.krzeminski.githubactions.domain.RunnerType.UbuntuLatest
import it.krzeminski.githubactions.domain.Workflow
import it.krzeminski.githubactions.domain.triggers.Release
import it.krzeminski.githubactions.dsl.ListCustomValue
import it.krzeminski.githubactions.dsl.expr
import it.krzeminski.githubactions.dsl.workflow
import it.krzeminski.githubactions.yaml.toYaml
import it.krzeminski.githubactions.yaml.writeToFile
import java.nio.`file`.Paths
import kotlin.collections.mapOf

public val workflowNodejsPackage: Workflow = workflow(
      name = "NodeJs Package",
      on = listOf(
        Release(
          _customArguments = mapOf(
            "types" to ListCustomValue("created")
          ),
        ),
        ),
      sourceFile = Paths.get("nodejs-package.main.kts"),
      targetFile = Paths.get("yaml-output/nodejs-package.yml"),
    ) {
      job(
        id = "build",
        runsOn = UbuntuLatest,
      ) {
        uses(
          name = "CheckoutV2",
          action = CheckoutV2(),
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
