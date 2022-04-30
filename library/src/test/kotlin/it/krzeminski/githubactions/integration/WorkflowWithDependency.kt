package it.krzeminski.githubactions.integration

import it.krzeminski.githubactions.domain.RunnerType
import it.krzeminski.githubactions.domain.triggers.Push
import it.krzeminski.githubactions.dsl.workflow
import java.nio.file.Paths

val workflowWithDependency = workflow(
    name = "Test workflow",
    on = listOf(Push()),
    sourceFile = Paths.get(".github/workflows/some_workflow.main.kts"),
    targetFile = Paths.get(".github/workflows/some_workflow.yaml"),
) {
    val testJob1 = job(
        id = "test_job_1",
        runsOn = RunnerType.UbuntuLatest,
    ) {
        run(
            name = "Hello world!",
            command = "echo 'hello!'",
        )
    }

    job(
        id = "test_job_2",
        runsOn = RunnerType.UbuntuLatest,
        needs = listOf(testJob1),
    ) {
        run(
            name = "Hello world, again!",
            command = "echo 'hello again!'",
        )
    }
}
