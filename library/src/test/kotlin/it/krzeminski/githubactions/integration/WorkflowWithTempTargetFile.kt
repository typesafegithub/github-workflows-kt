package it.krzeminski.githubactions.integration

import it.krzeminski.githubactions.actions.actions.CheckoutV3
import it.krzeminski.githubactions.domain.RunnerType
import it.krzeminski.githubactions.domain.triggers.Push
import it.krzeminski.githubactions.dsl.workflow
import java.io.File
import java.nio.file.Paths

fun workflowWithTempTargetFile(targetTempFile: File) = workflow(
    name = "Test workflow",
    on = listOf(Push()),
    sourceFile = Paths.get(".github/workflows/some_workflow.main.kts"),
    targetFile = targetTempFile.toPath(),
) {
    job(
        id = "test_job",
        runsOn = RunnerType.UbuntuLatest,
    ) {
        uses(
            name = "Check out",
            action = CheckoutV3(),
        )

        run(
            name = "Hello world!",
            command = "echo 'hello!'",
        )
    }
}
