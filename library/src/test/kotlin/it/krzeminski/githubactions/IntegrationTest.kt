package it.krzeminski.githubactions

import com.charleskorn.kaml.MalformedYamlException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.engine.spec.tempfile
import io.kotest.matchers.shouldBe
import it.krzeminski.githubactions.integration.workflowConcurrencyCancelInProgress
import it.krzeminski.githubactions.integration.workflowConcurrencyDefault
import it.krzeminski.githubactions.integration.workflowEnvironmentVariables
import it.krzeminski.githubactions.integration.workflowJobCondition
import it.krzeminski.githubactions.integration.workflowMalformedYaml
import it.krzeminski.githubactions.integration.workflowStepWithOutputs
import it.krzeminski.githubactions.integration.workflowTest
import it.krzeminski.githubactions.integration.workflowWithDependency
import it.krzeminski.githubactions.integration.workflowWithMultilineCommand
import it.krzeminski.githubactions.integration.workflowWithTempTargetFile
import it.krzeminski.githubactions.kotest.setupShouldMatchFile
import it.krzeminski.githubactions.kotest.shouldMatchFile
import it.krzeminski.githubactions.yaml.toYaml
import it.krzeminski.githubactions.yaml.writeToFile
import java.io.File

class IntegrationTest : FunSpec({

    setupShouldMatchFile(
        baseDirectory = File("src/test/resources/integration"),
        normalize = { it.replace("\r\n", "\n") }
    )

    test("workflowTest : toYaml()") {
        workflowTest.toYaml() shouldMatchFile "workflowTest.yaml"
    }

    test("toYaml() - workflow with one job depending on another") {
        workflowWithDependency.toYaml() shouldMatchFile "workflowWithDependency.yaml"
    }

    test("toYaml() - 'hello world' workflow without consistency check") {
        workflowTest.toYaml(addConsistencyCheck = false)
            .shouldMatchFile("workflowTest-toYaml-consistency.yaml")
    }

    test("workflowWithMultilineCommand - multiline command with pipes") {
        workflowWithMultilineCommand.toYaml(addConsistencyCheck = false)
            .shouldMatchFile("workflowWithMultilineCommand.yaml")
    }

    test("workflowWithTempTargetFile - no consistency") {
        // given
        val targetTempFile = tempfile()

        // when
        workflowWithTempTargetFile(targetTempFile).writeToFile(addConsistencyCheck = false)

        // then
        targetTempFile.readText() shouldMatchFile "workflowWithTempTargetFile.yaml"
    }

    test("workflowWithTempTargetFile - consistency") {
        // given
        val targetTempFile = tempfile()
        val workflowWithTempTargetFile = workflowWithTempTargetFile(tempfile())

        // when
        workflowWithTempTargetFile.writeToFile(addConsistencyCheck = true)

        // then
        targetTempFile.readText() shouldMatchFile "workflowWithTempTargetFile-consistency.yaml"
    }

    test("workflowJobCondition") {
        workflowJobCondition.toYaml(addConsistencyCheck = false) shouldMatchFile "workflowJobCondition.yaml"
    }

    test("workflowEnvironmentVariables") {
        workflowEnvironmentVariables.toYaml(addConsistencyCheck = false)
            .shouldMatchFile("workflowEnvironmentVariables.yaml")
    }

    test("workflowStepWithOutputs") {
        workflowStepWithOutputs.toYaml(addConsistencyCheck = false)
            .shouldMatchFile("workflowStepWithOutputs.yaml")
    }

    test("workflowConcurrencyDefault") {
        workflowConcurrencyDefault.toYaml(addConsistencyCheck = false)
            .shouldMatchFile("workflowConcurrencyDefault.yaml")
    }

    test("workflowConcurrencyCancelInProgress") {
        workflowConcurrencyCancelInProgress.toYaml(addConsistencyCheck = false)
            .shouldMatchFile("workflowConcurrencyCancelInProgress.yaml")
    }

    test("workflowMalformedYaml") {

        shouldThrow<MalformedYamlException> {
            workflowMalformedYaml.toYaml()
        }.message shouldBe """
            |mapping values are not allowed here (is the indentation level of this line or a line nearby incorrect?)
            | at line 26, column 23:
            |            name: property: something
            |                          ^
        """.trimMargin()
    }
})
