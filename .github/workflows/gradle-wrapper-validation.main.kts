@file:DependsOn("it.krzeminski:github-actions-kotlin-dsl:0.10.0")

import it.krzeminski.githubactions.actions.actions.CheckoutV2
import it.krzeminski.githubactions.actions.gradle.WrapperValidationActionV1
import it.krzeminski.githubactions.domain.RunnerType.UbuntuLatest
import it.krzeminski.githubactions.domain.triggers.PullRequest
import it.krzeminski.githubactions.domain.triggers.Push
import it.krzeminski.githubactions.dsl.workflow
import java.nio.file.Paths

val gradleWrapperValidationWorkflow = workflow(
    name = "Validate Gradle wrapper",
    on = listOf(
        Push(branches = listOf("main")),
        PullRequest(),
    ),
    sourceFile = Paths.get(".github/workflows/_GenerateWorkflows.main.kts"),
    targetFile = Paths.get(".github/workflows/gradle-wrapper-validation.yaml"),
) {
    job(
        name = "validation",
        runsOn = UbuntuLatest,
    ) {
        uses(
            name = "Checkout",
            action = CheckoutV2(),
        )
        uses(
            name = "Validate wrapper",
            action = WrapperValidationActionV1(),
        )
    }
}
