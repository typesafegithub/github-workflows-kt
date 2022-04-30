package it.krzeminski.githubactions.integration

import it.krzeminski.githubactions.actions.actions.CheckoutV3
import it.krzeminski.githubactions.domain.RunnerType
import it.krzeminski.githubactions.domain.triggers.Push
import it.krzeminski.githubactions.dsl.workflow
import java.nio.file.Paths

val workflowJobCondition = workflow(
    name = "Test workflow",
    on = listOf(Push()),
    sourceFile = Paths.get(".github/workflows/some_workflow.main.kts"),
    targetFile = Paths.get(".github/workflows/some_workflow.yaml"),
) {
    job(
        id = "test_job",
        runsOn = RunnerType.UbuntuLatest,
        condition = "\${{ always() }}"
    ) {
        uses(
            name = "Check out",
            action = CheckoutV3(),
        )
    }
}
