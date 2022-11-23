package it.krzeminski.githubactions.scriptgenerator

import io.kotest.core.spec.style.FunSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.booleans.shouldBeTrue
import it.krzeminski.githubactions.domain.triggers.Push
import it.krzeminski.githubactions.scriptmodel.YamlJob
import it.krzeminski.githubactions.scriptmodel.YamlStep
import it.krzeminski.githubactions.scriptmodel.YamlWorkflow
import it.krzeminski.githubactions.scriptmodel.YamlWorkflowTriggers

class ImportsTest : FunSpec({

    val expectedImports = """
        |import it.krzeminski.githubactions.domain.RunnerType
        |import it.krzeminski.githubactions.domain.Workflow
        |import it.krzeminski.githubactions.domain.triggers.Push
        |import it.krzeminski.githubactions.dsl.expressions.expr
        |import it.krzeminski.githubactions.dsl.workflow
        |import it.krzeminski.githubactions.yaml.toYaml
        |import it.krzeminski.githubactions.yaml.writeToFile
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
},)
