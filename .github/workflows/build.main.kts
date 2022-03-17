@file:DependsOn("it.krzeminski:github-actions-kotlin-dsl:0.10.0")

import it.krzeminski.githubactions.actions.actions.CheckoutV2
import it.krzeminski.githubactions.actions.actions.SetupJavaV2
import it.krzeminski.githubactions.actions.actions.SetupJavaV2.Distribution.Adopt
import it.krzeminski.githubactions.actions.gradle.GradleBuildActionV2
import it.krzeminski.githubactions.domain.RunnerType.UbuntuLatest
import it.krzeminski.githubactions.domain.RunnerType.Windows2022
import it.krzeminski.githubactions.domain.triggers.PullRequest
import it.krzeminski.githubactions.domain.triggers.Push
import it.krzeminski.githubactions.dsl.workflow
import java.nio.file.Paths

val buildWorkflow = workflow(
    name = "Build",
    on = listOf(
        Push(branches = listOf("main")),
        PullRequest(),
    ),
    sourceFile = Paths.get(".github/workflows/_GenerateWorkflows.main.kts"),
    targetFile = Paths.get(".github/workflows/build.yaml"),
) {
    listOf(UbuntuLatest, Windows2022).forEach { runnerType ->
        job(
            name = "build_for_$runnerType",
            runsOn = runnerType,
        ) {
            uses(
                name = "Checkout",
                action = CheckoutV2(),
            )
            uses(
                name = "Set up JDK",
                action = SetupJavaV2(
                    javaVersion = "11",
                    distribution = Adopt,
                )
            )
            uses(
                name = "Build",
                action = GradleBuildActionV2(
                    arguments = "build",
                )
            )
        }
    }
}
