package it.krzeminski.githubactions.integration

import it.krzeminski.githubactions.actions.actions.CheckoutV3
import it.krzeminski.githubactions.actions.endbug.AddAndCommitV9
import it.krzeminski.githubactions.domain.Concurrency
import it.krzeminski.githubactions.domain.RunnerType
import it.krzeminski.githubactions.domain.triggers.Push
import it.krzeminski.githubactions.dsl.expr
import it.krzeminski.githubactions.dsl.workflow
import java.nio.file.Paths

val workflowConcurrencyDefault = workflow(
    name = "Test workflow",
    on = listOf(Push()),
    sourceFile = Paths.get(".github/workflows/some_workflow.main.kts"),
    targetFile = Paths.get(".github/workflows/some_workflow.yaml"),
    concurrency = Concurrency("workflow_staging_environment"),
) {
    job(
        id = "test_job",
        runsOn = RunnerType.UbuntuLatest,
        concurrency = Concurrency("job_staging_environment"),
    ) {
        val addAndCommit = uses(AddAndCommitV9())

        uses(
            name = "Some step consuming other step's output",
            action = CheckoutV3(
                repository = expr(addAndCommit.id),
                ref = expr(addAndCommit.outputs.commitSha),
                token = expr(addAndCommit.outputs["my-unsafe-output"]),
            )
        )
    }
}
