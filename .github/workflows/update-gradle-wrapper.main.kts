@file:DependsOn("it.krzeminski:github-actions-kotlin-dsl:0.10.0")

import it.krzeminski.githubactions.actions.actions.CheckoutV2
import it.krzeminski.githubactions.actions.gradleupdate.UpdateGradleWrapperActionV1
import it.krzeminski.githubactions.domain.RunnerType.UbuntuLatest
import it.krzeminski.githubactions.domain.triggers.Cron
import it.krzeminski.githubactions.domain.triggers.Schedule
import it.krzeminski.githubactions.domain.triggers.WorkflowDispatch
import it.krzeminski.githubactions.dsl.workflow
import java.nio.file.Paths

val updateGradleWrapperWorkflow = workflow(
    name = "Update Gradle Wrapper",
    on = listOf(
        Schedule(listOf(Cron("0 0 * * *"))), // Daily, at midnight.
        WorkflowDispatch(),
    ),
    sourceFile = Paths.get(".github/workflows/_GenerateWorkflows.main.kts"),
    targetFile = Paths.get(".github/workflows/update-gradle-wrapper.yml"),
) {
    job(
        name = "update-gradle-wrapper",
        runsOn = UbuntuLatest,
    ) {
        uses(
            name = "Checkout",
            action = CheckoutV2(),
        )
        uses(
            name = "Update Gradle Wrapper",
            action = UpdateGradleWrapperActionV1(),
        )
    }
}
