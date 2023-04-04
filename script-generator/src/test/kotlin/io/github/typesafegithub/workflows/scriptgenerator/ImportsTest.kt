package io.github.typesafegithub.workflows.scriptgenerator

import io.github.typesafegithub.workflows.domain.triggers.Push
import io.github.typesafegithub.workflows.scriptmodel.YamlJob
import io.github.typesafegithub.workflows.scriptmodel.YamlStep
import io.github.typesafegithub.workflows.scriptmodel.YamlWorkflow
import io.github.typesafegithub.workflows.scriptmodel.YamlWorkflowTriggers
import io.kotest.core.spec.style.FunSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.booleans.shouldBeTrue

class ImportsTest : FunSpec({

    val expectedImports = """
        |import io.github.typesafegithub.workflows.domain.RunnerType
        |import io.github.typesafegithub.workflows.domain.Workflow
        |import io.github.typesafegithub.workflows.domain.triggers.Push
        |import io.github.typesafegithub.workflows.dsl.expressions.expr
        |import io.github.typesafegithub.workflows.dsl.workflow
        |import io.github.typesafegithub.workflows.yaml.toYaml
        |import io.github.typesafegithub.workflows.yaml.writeToFile
        |import java.nio.`file`.Paths
    """.trimMargin().lines()

    val workflow = YamlWorkflow(
        name = "hello",
        on = YamlWorkflowTriggers(Push()),
        jobs = mapOf(
            "job" to YamlJob(
                runsOn = "Unbuntu-Latest",
                steps = listOf(
                    YamlStep(id = "step", run = "run it"),
                ),
            ),
        ),
    )

    test("Check for correct imports") {
        val content = workflow.toFileSpec("filename").toString()
        println(content)
        expectedImports.forAll { import ->
            content.contains(import).shouldBeTrue()
        }
    }
})
