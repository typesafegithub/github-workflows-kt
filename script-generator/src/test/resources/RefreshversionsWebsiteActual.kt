package actual

import it.krzeminski.githubactions.actions.actions.CheckoutV2
import it.krzeminski.githubactions.actions.actions.SetupPythonV2
import it.krzeminski.githubactions.domain.RunnerType.UbuntuLatest
import it.krzeminski.githubactions.domain.Workflow
import it.krzeminski.githubactions.domain.triggers.Push
import it.krzeminski.githubactions.dsl.expr
import it.krzeminski.githubactions.dsl.workflow
import it.krzeminski.githubactions.yaml.toYaml
import java.nio.`file`.Paths

public val workflowPublishMkdocs: Workflow = workflow(
      name = "Publish MkDocs",
      on = listOf(
        Push(
          branches = listOf("release"),
        ),
        ),
      sourceFile = Paths.get("publish-mkdocs.main.kts"),
      targetFile = Paths.get("publish-mkdocs.yml"),
    ) {
      job("deploy", UbuntuLatest) {
        uses(
          name = "CheckoutV2",
          action = CheckoutV2(),
        )
        run(
          name = "./docs/DocsCopier.main.kts",
          command = "./docs/DocsCopier.main.kts",
        )
        uses(
          name = "SetupPythonV2",
          action = SetupPythonV2(
            pythonVersion = "3.x",
          ),
        )
        run(
          name = "pip install -r docs/requirements.txt",
          command = "pip install -r docs/requirements.txt",
        )
        run(
          name = "mkdocs gh-deploy --force",
          command = "mkdocs gh-deploy --force",
        )
      }

    }.also {
        println("Generating YAML")
        println(it.toYaml(addConsistencyCheck = false))
    }
