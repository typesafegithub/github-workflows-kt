package generated

import io.github.typesafegithub.workflows.actions.actions.CheckoutV3
import io.github.typesafegithub.workflows.actions.actions.SetupPythonV4
import io.github.typesafegithub.workflows.domain.RunnerType
import io.github.typesafegithub.workflows.domain.Workflow
import io.github.typesafegithub.workflows.domain.triggers.Push
import io.github.typesafegithub.workflows.domain.triggers.WorkflowDispatch
import io.github.typesafegithub.workflows.dsl.expressions.expr
import io.github.typesafegithub.workflows.dsl.workflow
import io.github.typesafegithub.workflows.yaml.toYaml
import io.github.typesafegithub.workflows.yaml.writeToFile
import java.nio.`file`.Paths

public val workflowRefreshversionsWebsite: Workflow = workflow(
      name = "RefreshVersions Website",
      on = listOf(
        Push(
          branches = listOf("release"),
        ),
        WorkflowDispatch(),
        ),
      sourceFile = Paths.get(".github/workflows/refreshversions-website.main.kts"),
    ) {
      job(
        id = "deploy",
        runsOn = RunnerType.UbuntuLatest,
      ) {
        uses(
          name = "CheckoutV3",
          action = CheckoutV3(),
        )
        run(
          name = "./docs/DocsCopier.main.kts",
          command = "./docs/DocsCopier.main.kts",
        )
        uses(
          name = "SetupPythonV4",
          action = SetupPythonV4(
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
