package it.krzeminski.githubactions.dsl.expressions

import it.krzeminski.githubactions.actions.actions.SetupNodeV3
import it.krzeminski.githubactions.domain.RunnerType
import it.krzeminski.githubactions.domain.triggers.Push
import it.krzeminski.githubactions.dsl.expressions.contexts.StrategyMatrix
import it.krzeminski.githubactions.dsl.workflow
import it.krzeminski.githubactions.yaml.toYaml
import java.nio.file.Path

fun main() {
    println(expectedWorkflow.toYaml())
}

val expectedWorkflow = workflow(
    name = "Test matrix",
    on = listOf(Push()),
    sourceFile = Path.of("/tmp/some_workflow.main.kts"),
) {
    val strategyMatrix = object : StrategyMatrix() {
        val os by iterate("ubuntu-latest", "windows-latest")
        val node by iterate("14", "16")
    }
    job(
        id = "test_job",
        name = "Test Job",
        strategyMatrix = strategyMatrix,
        runsOn = RunnerType.Custom(strategyMatrix.os),
    ) {
        uses(SetupNodeV3(
            nodeVersion = strategyMatrix.node
        ))
    }
}

val expectedMatrixYaml = """
name: Test matrix
on: push

jobs:
  build:
    runs-on: ${expr("matrix.os")}
    strategy:
      matrix:
        os: [ubuntu-latest, windows-latest]
        node: [14, 16]
    steps:
      - uses: actions/setup-node@v3
        with:
          node-version: ${expr("matrix.node")}
""".trimIndent()
