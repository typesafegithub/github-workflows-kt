package it.krzeminski.githubactions.integration

import it.krzeminski.githubactions.domain.RunnerType
import it.krzeminski.githubactions.domain.triggers.Push
import it.krzeminski.githubactions.dsl.workflow
import java.nio.file.Paths

val workflowMalformedYaml = workflow(
    name = "Test workflow",
    on = listOf(Push()),
    sourceFile = Paths.get("../.github/workflows/invalid_workflow.main.kts"),
    targetFile = Paths.get("../.github/workflows/invalid_workflow.yaml"),
) {
    job("test_job", runsOn = RunnerType.UbuntuLatest) {
        run(name = "property: something", command = "echo hello")
    }
}
