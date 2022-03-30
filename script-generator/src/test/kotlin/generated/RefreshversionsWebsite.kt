package generated

import it.krzeminski.githubactions.actions.actions.CheckoutV2
import it.krzeminski.githubactions.actions.actions.SetupPythonV2
import it.krzeminski.githubactions.domain.RunnerType.UbuntuLatest
import it.krzeminski.githubactions.domain.Workflow
import it.krzeminski.githubactions.domain.triggers.Push
import it.krzeminski.githubactions.domain.triggers.WorkflowDispatch
import it.krzeminski.githubactions.dsl.expr
import it.krzeminski.githubactions.dsl.workflow
import it.krzeminski.githubactions.yaml.toYaml
import it.krzeminski.githubactions.yaml.writeToFile
import java.nio.`file`.Paths

public val workflowRefreshversionsWebsite: Workflow = workflow(
      name = "RefreshVersions Website",
      on = listOf(
        Push(
          branches = listOf("release"),
        ),
        WorkflowDispatch(),
        ),
      sourceFile = Paths.get("refreshversions-website.main.kts"),
      targetFile = Paths.get("yaml-output/refreshversions-website.yml"),
    ) {
      job(
        id = "deploy",
        runsOn = UbuntuLatest,
      ) {
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

    }
