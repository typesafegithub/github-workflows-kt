@file:DependsOn("it.krzeminski:github-actions-kotlin-dsl:0.12.0")

import it.krzeminski.githubactions.actions.actions.CheckoutV3
import it.krzeminski.githubactions.actions.endbug.AddAndCommitV8
import it.krzeminski.githubactions.actions.gradleupdate.UpdateGradleWrapperActionV1
import it.krzeminski.githubactions.domain.RunnerType.UbuntuLatest
import it.krzeminski.githubactions.domain.triggers.Cron
import it.krzeminski.githubactions.domain.triggers.Push
import it.krzeminski.githubactions.domain.triggers.Schedule
import it.krzeminski.githubactions.domain.triggers.WorkflowDispatch
import it.krzeminski.githubactions.dsl.expr
import it.krzeminski.githubactions.dsl.workflow
import java.nio.file.Paths

val githubPayloadsWorkflow = workflow(
    name = "Download GitHub payloads",
    on = listOf(
        Push(),
        Schedule(listOf(
            Cron(dayWeek = "4", hour = "7", minute = "0"),
        )),
        WorkflowDispatch(),
    ),
    sourceFile = Paths.get(".github/workflows/_GenerateWorkflows.main.kts"),
    targetFile = Paths.get(".github/workflows/github-payloads.yml"),
) {
    val payloads = "library/src/test/resources/payloads"
    val contexts = listOf("github", "job", "runner", "strategy")

    job(
        id = "github-payload",
        runsOn = UbuntuLatest,
    ) {
        uses(CheckoutV3())
        for (context in contexts) {
            val filename = when (context) {
                "github" -> "github-${'$'}GITHUB_EVENT_NAME.json"
                else -> "$context.json"
            }
            run(
                name = "Payload for context = $context",
                command = """echo '${expr("toJSON($context)")}' | tee $payloads/$filename"""
            )
        }
        uses(
            name = "commit",
            action = AddAndCommitV8(
                add = payloads,
                message = "Commit GitHub payloads",
                push = "true",
            )
        )
    }
}
