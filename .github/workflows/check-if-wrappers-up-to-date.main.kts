@file:DependsOn("it.krzeminski:github-actions-kotlin-dsl:0.11.0")

import it.krzeminski.githubactions.actions.actions.CheckoutV3
import it.krzeminski.githubactions.actions.actions.SetupJavaV2
import it.krzeminski.githubactions.actions.actions.SetupJavaV2.Distribution.Adopt
import it.krzeminski.githubactions.actions.gradle.GradleBuildActionV2
import it.krzeminski.githubactions.domain.RunnerType.UbuntuLatest
import it.krzeminski.githubactions.domain.triggers.PullRequest
import it.krzeminski.githubactions.domain.triggers.Push
import it.krzeminski.githubactions.dsl.workflow
import java.nio.file.Paths

val checkIfWrappersUpToDateWorkflow = workflow(
    name = "Check if wrappers up to date",
    on = listOf(
        Push(branches = listOf("main")),
        PullRequest(),
    ),
    sourceFile = Paths.get(".github/workflows/_GenerateWorkflows.main.kts"),
    targetFile = Paths.get(".github/workflows/check-if-wrappers-up-to-date.yaml"),
) {
    job(
        name = "check",
        runsOn = UbuntuLatest,
    ) {
        uses(
            name = "Checkout",
            action = CheckoutV3(),
        )
        uses(
            name = "Set up JDK",
            action = SetupJavaV2(
                javaVersion = "11",
                distribution = Adopt,
            )
        )
        uses(
            name = "Generate wrappers",
            action = GradleBuildActionV2(
                arguments = ":wrapper-generator:run",
            )
        )
        run(
            name = "Fail if there are any changes in the generated wrappers",
            command = "git diff --exit-code library/src/gen/"
        )
    }
}
