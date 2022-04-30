package it.krzeminski.githubactions.integration

import it.krzeminski.githubactions.actions.actions.CheckoutV3
import it.krzeminski.githubactions.domain.RunnerType
import it.krzeminski.githubactions.domain.triggers.Push
import it.krzeminski.githubactions.dsl.workflow
import java.nio.file.Paths

val workflowTest = workflow(
    name = "Test workflow",
    on = listOf(Push()),
    sourceFile = Paths.get(".github/workflows/some_workflow.main.kts"),
    targetFile = Paths.get(".github/workflows/some_workflow.yaml"),
) {
    job(
        id = "test_job",
        name = "Test Job",
        runsOn = RunnerType.UbuntuLatest,
    ) {
        uses(CheckoutV3())
        run("echo 'hello!'")
    }
}
