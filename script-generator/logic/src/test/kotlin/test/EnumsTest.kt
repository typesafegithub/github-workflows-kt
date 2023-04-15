package test

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.asClassName
import io.github.typesafegithub.workflows.domain.RunnerType
import io.github.typesafegithub.workflows.domain.triggers.PullRequest
import io.github.typesafegithub.workflows.scriptmodel.YamlWorkflowTriggers
import io.github.typesafegithub.workflows.scriptmodel.myYaml
import io.github.typesafegithub.workflows.scriptmodel.runnerTypeBlockOf
import io.kotest.core.spec.style.FunSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString

class EnumsTest : FunSpec({
    test("CaseEnumSerializer, when deserialize type Enums, use PascalCase") {
        myYaml.decodeFromString<PullRequest.Type>("opened") shouldBe PullRequest.Type.Opened
        myYaml.decodeFromString<PullRequest.Type>("review_requested") shouldBe PullRequest.Type.ReviewRequested
        myYaml.encodeToString(PullRequest.Type.ReviewRequested) shouldBe "\"review_requested\""

        val input =
            """|pull_request:
                |  types:
                |      - 'opened'
                |      - 'review_requested'
                |
            """.trimMargin()
        val triggers = myYaml.decodeFromString<YamlWorkflowTriggers>(input)
        triggers.pull_request shouldBe PullRequest(listOf(PullRequest.Type.Opened, PullRequest.Type.ReviewRequested))
    }

    test("Convert RunnerType") {
        val runnerTypes = listOf(
            RunnerType.UbuntuLatest,
            RunnerType.WindowsLatest,
            RunnerType.MacOSLatest,
            RunnerType.Windows2022,
            RunnerType.MacOS11,
        )
        val inputs = listOf("UbuntuLatest", "windows_latest", "mac+os+latest", "windows_2022", "macos 1 1")
        inputs.zip(runnerTypes).forAll { (input, runnerType) ->
            runnerTypeBlockOf(input) shouldBe CodeBlock.of("runsOn = %T,\n", runnerType::class.asClassName())
        }
        runnerTypeBlockOf("windows-3.0") shouldBe CodeBlock.of("runsOn = io.github.typesafegithub.workflows.domain.RunnerType.Custom(\"windows-3.0\"),\n")

        runnerTypeBlockOf("${'$'}{{ github.event.inputs.run-on || 'ubuntu-latest' }}") shouldBe CodeBlock.of(
            "runsOn = io.github.typesafegithub.workflows.domain.RunnerType.Custom(expr(\"github.event.inputs.run-on || 'ubuntu-latest'\")),\n",
        )
    }
})
