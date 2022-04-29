@file:DependsOn("it.krzeminski:github-actions-kotlin-dsl:0.15.0")

import it.krzeminski.githubactions.actions.CustomAction
import it.krzeminski.githubactions.actions.actions.CheckoutV3
import it.krzeminski.githubactions.actions.actions.SetupJavaV3
import it.krzeminski.githubactions.actions.gradle.GradleBuildActionV2
import it.krzeminski.githubactions.domain.RunnerType.UbuntuLatest
import it.krzeminski.githubactions.domain.triggers.Cron
import it.krzeminski.githubactions.domain.triggers.Schedule
import it.krzeminski.githubactions.domain.triggers.WorkflowDispatch
import it.krzeminski.githubactions.dsl.expr
import it.krzeminski.githubactions.dsl.workflow
import java.nio.file.Paths

val checkIfNewActionVersionsWorkflow = workflow(
    name = "Updates available",
    on = listOf(
        Schedule(listOf(
            Cron(dayWeek = "4", hour = "7", minute = "0")
        )),
        WorkflowDispatch(),
    ),
    sourceFile = Paths.get(".github/workflows/_GenerateWorkflows.main.kts"),
    targetFile = Paths.get(".github/workflows/actions-versions.yml"),
) {
    job(
        id = "updates-available",
        runsOn = UbuntuLatest,
    ) {
        uses(CheckoutV3())
        uses(
            name = "Set up JDK",
            action = SetupJavaV3(
                javaVersion = "11",
                distribution = SetupJavaV3.Distribution.Adopt,
            )
        )
        uses(
            name = "Run suggestVersions",
            env = linkedMapOf("GITHUB_TOKEN" to expr("secrets.GITHUB_TOKEN")),
            action = GradleBuildActionV2(
                arguments = "suggestVersions",
            )
        )
        uses(
            name = "Create issue",
            action = CustomAction(
                "peter-evans", "create-issue-from-file", "v4",
                inputs = mapOf(
                    "title" to "Updates available",
                    "content-filepath" to "wrapper-generator/build/suggestVersions.md",
                ))
        )
    }
}
