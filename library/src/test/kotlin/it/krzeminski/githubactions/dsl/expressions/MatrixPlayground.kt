package it.krzeminski.githubactions.dsl.expressions

import io.kotest.matchers.shouldBe
import it.krzeminski.githubactions.actions.actions.SetupNodeV3
import it.krzeminski.githubactions.domain.RunnerType
import it.krzeminski.githubactions.domain.triggers.Push
import it.krzeminski.githubactions.dsl.expressions.contexts.StrategyMatrix
import it.krzeminski.githubactions.dsl.workflow
import it.krzeminski.githubactions.yaml.toYaml
import org.junit.jupiter.api.Test
import java.nio.file.Path
class MatrixPlayground {
    @Test
    fun `matrix with strings and ints`() {
        val actual = simpleWorkflowWithMatrix.toYaml(addConsistencyCheck = false)
        actual shouldBe expectedMatrixYaml

        val strategyMatrix = simpleWorkflowWithMatrix.jobs.first().strategyMatrix
        strategyMatrix shouldBe mapOf(
            "os" to listOf("ubuntu-latest", "windows-latest"),
            "node" to listOf("14", "16")
        )
    }

    val simpleWorkflowWithMatrix = workflow(
        name = "Test matrix",
        on = listOf(Push()),
        sourceFile = Path.of("some_workflow.main.kts"),
    ) {
        val matrix = object : StrategyMatrix() {
            val os by strings("ubuntu-latest", "windows-latest")
            val node by integers(14, 16)
        }
        job(
            id = "test_job",
            name = "Test Job",
            strategyMatrix = matrix.stringsMap, // TODO: not sure we should keep that
            runsOn = RunnerType.Custom(matrix.os),
        ) {
            // TODO: how do I use strategyMatrix.distribution? https://github.com/krzema12/github-workflows-kt/issues/297

            uses(SetupNodeV3(
                nodeVersion = matrix.node,
            ))
        }
    }

    @Test
    fun `matrix with objects`() {
        val matrix = object : StrategyMatrix() {
            val distribution by objects(debianObject, alpineObject)
        }
        matrix.distribution shouldBe expr("matrix.distribution")
        matrix.objectsMap shouldBe mapOf(
            "distribution" to listOf(debianObject, alpineObject)
        )
        // TODO not sure how we are supposed to use "matrix.distribution"
    }

    companion object {
        val debianObject = mapOf(
            "wsl-id" to "Debian",
            "user-id" to "Debian",
            "match-pattern" to "*Debian*",
            "default-absent-tool" to "dos2unix",
        )
        val alpineObject = mapOf(
            "wsl-id" to "Alpine",
            "user-id" to "Alpine",
            "match-pattern" to "*Alpine*",
            "default-absent-tool" to "dos2unix",
        )
    }
}

private val expectedMatrixYaml = """
# This file was generated using Kotlin DSL (library/some_workflow.main.kts).
# If you want to modify the workflow, please change the Kotlin file and regenerate this YAML file.
# Generated with https://github.com/krzema12/github-workflows-kt

name: Test matrix
on:
  push: {}
jobs:
  test_job:
    name: Test Job
    runs-on: ${expr("matrix.os")}
    strategy:
      matrix:
        os:
        - ubuntu-latest
        - windows-latest
        node:
        - 14
        - 16
    steps:
    - id: step-0
      uses: actions/setup-node@v3
      with:
        node-version: ${expr("matrix.node")}
""".trimStart()
