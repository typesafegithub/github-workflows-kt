@file:DependsOn("it.krzeminski:github-actions-kotlin-dsl:0.10.0")

import it.krzeminski.githubactions.actions.actions.CheckoutV2
import it.krzeminski.githubactions.actions.actions.SetupJavaV2
import it.krzeminski.githubactions.actions.actions.SetupJavaV2.Distribution.Adopt
import it.krzeminski.githubactions.actions.gradle.GradleBuildActionV2
import it.krzeminski.githubactions.domain.RunnerType.UbuntuLatest
import it.krzeminski.githubactions.domain.triggers.Push
import it.krzeminski.githubactions.dsl.workflow
import java.nio.file.Paths

val generateWrappersWorkflow = workflow(
    name = "Generate wrappers",
    on = listOf(Push(branchesIgnore = listOf("main"))),
    sourceFile = Paths.get(".github/workflows/_GenerateWorkflows.main.kts"),
    targetFile = Paths.get(".github/workflows/generate-wrappers.yaml"),
) {
    job(
        name = "generate-wrappers",
        runsOn = UbuntuLatest,
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
            name = "Generate wrappers",
            action = GradleBuildActionV2(
                arguments = ":wrapper-generator:run",
            )
        )
        uses(
            name = "Check that the library builds fine with newly generated wrappers",
            action = GradleBuildActionV2(
                arguments = "build",
            )
        )
        run(
            name = "Commit and push",
            command = """
                git config --global user.email "<>"
                git config --global user.name "GitHub Actions Bot"
                git add library/src/gen
                git commit --allow-empty -m "Regenerate wrappers (${'$'}GITHUB_SHA)"  # an empty commit explicitly shows that the wrappers are up-to-date
                git push
            """.trimIndent()
        )
    }
}
