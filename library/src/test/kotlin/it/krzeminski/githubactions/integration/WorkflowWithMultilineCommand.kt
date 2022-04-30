package it.krzeminski.githubactions.integration

import it.krzeminski.githubactions.domain.RunnerType
import it.krzeminski.githubactions.domain.triggers.Push
import it.krzeminski.githubactions.dsl.workflow
import java.nio.file.Paths

val workflowWithMultilineCommand = workflow(
    name = "Test workflow",
    on = listOf(Push()),
    sourceFile = Paths.get(".github/workflows/some_workflow.main.kts"),
    targetFile = Paths.get(".github/workflows/some_workflow.yaml"),
) {
    job(
        id = "test_job",
        runsOn = RunnerType.UbuntuLatest,
    ) {
        run(
            name = "Hello world!",
            command = """
                        less test.txt \
                        | grep -P "foobar" \
                        | sort \
                        > result.txt
            """.trimIndent(),
        )
    }
}
