package it.krzeminski.githubactions.integration

import it.krzeminski.githubactions.actions.actions.CheckoutV3
import it.krzeminski.githubactions.domain.RunnerType
import it.krzeminski.githubactions.domain.triggers.Push
import it.krzeminski.githubactions.dsl.workflow
import java.nio.file.Paths

val workflowEnvironmentVariables = workflow(
    name = "Test workflow",
    on = listOf(Push()),
    env = linkedMapOf(
        "SIMPLE_VAR" to "simple-value-workflow",
        "MULTILINE_VAR" to """
                    hey,
                    hi,
                    hello! workflow
        """.trimIndent()
    ),
    sourceFile = Paths.get(".github/workflows/some_workflow.main.kts"),
    targetFile = Paths.get(".github/workflows/some_workflow.yaml"),
) {
    job(
        id = "test_job",
        runsOn = RunnerType.UbuntuLatest,
        condition = "\${{ always() }}",
        env = linkedMapOf(
            "SIMPLE_VAR" to "simple-value-job",
            "MULTILINE_VAR" to """
                        hey,
                        hi,
                        hello! job
            """.trimIndent()
        ),
    ) {
        uses(
            name = "Check out",
            action = CheckoutV3(),
            env = linkedMapOf(
                "SIMPLE_VAR" to "simple-value-uses",
                "MULTILINE_VAR" to """
                            hey,
                            hi,
                            hello! uses
                """.trimIndent()
            ),
        )

        run(
            name = "Hello world!",
            command = "echo 'hello!'",
            env = linkedMapOf(
                "SIMPLE_VAR" to "simple-value-run",
                "MULTILINE_VAR" to """
                            hey,
                            hi,
                            hello! run
                """.trimIndent()
            ),
        )
    }
}
