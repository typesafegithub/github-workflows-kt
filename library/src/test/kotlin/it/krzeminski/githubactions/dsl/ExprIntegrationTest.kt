package it.krzeminski.githubactions.dsl

import io.kotest.core.spec.style.FunSpec
import it.krzeminski.githubactions.actions.actions.CheckoutV3
import it.krzeminski.githubactions.actions.actions.SetupNodeV3
import it.krzeminski.githubactions.domain.RunnerType
import it.krzeminski.githubactions.domain.triggers.Push
import it.krzeminski.githubactions.expr.Env
import it.krzeminski.githubactions.expr.Expr
import it.krzeminski.githubactions.expr.Secrets
import it.krzeminski.githubactions.yaml.writeToFile
import java.nio.file.Path

class ExprIntegrationTest : FunSpec({

    test("Executing workflow with type-safe expressions") {
        exprWorkflow.writeToFile(addConsistencyCheck = false)
    }
})

@Suppress("VariableNaming")
val exprWorkflow = workflow(
    name = "expr-typesafe",
    on = listOf(Push()),
    sourceFile = Path.of("library/src/test/kotlin/it/krzeminski/githubactions/dsl/ExprIntegrationTest.kt"),
    targetFile = Path.of("../.github/workflows/expr-testing.yml"),
) {
    val NODE by Expr.matrix
    val GREETING by Expr.env
    val FIRST_NAME by Expr.env
    val SECRET by Expr.env
    val TOKEN by Expr.env
    val SUPER_SECRET by Expr.secrets

    job(
        id = "job1",
        runsOn = RunnerType.UbuntuLatest,
        strategyMatrix = mapOf(
            "OS" to listOf("ubuntu-latest", "windows-latest"),
            NODE to listOf("14", "16"),
        ),
        env = linkedMapOf(
            GREETING to "World",
        )
    ) {
        uses(CheckoutV3())
        run(
            name = "Default environment variable",
            command = "action=${Env.GITHUB_ACTION} repo=${Env.GITHUB_REPOSITORY}",
            condition = expr { always() }
        )
        run(
            name = "Custom environment variable",
            env = linkedMapOf(
                FIRST_NAME to "Patrick",
            ),
            command = "echo " + expr { GREETING } + " " + expr { FIRST_NAME }
        )
        run(
            name = "Encrypted secret",
            env = linkedMapOf(
                SECRET to expr { SUPER_SECRET },
                TOKEN to expr { Secrets.GITHUB_TOKEN }
            ),
            command = "echo secret=$SECRET token=$TOKEN"
        )
        uses(
            name = "MatrixContext node",
            SetupNodeV3(nodeVersion = expr { NODE })
        )
        run(
            name = "RunnerContext create temp directory",
            command = "mkdir " + expr { runner.temp } + "/build_logs"
        )
        run(
            name = "GitHubContext echo sha",
            command = "echo " + expr { github.sha }
        )
        run(
            name = "StrategyContext job-index",
            command = "npm test > test-job-" + expr { strategy.`job-index` } + ".txt"
        )
    }
}
