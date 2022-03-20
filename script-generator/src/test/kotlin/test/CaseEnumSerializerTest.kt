package test

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import it.krzeminski.githubactions.domain.triggers.PullRequest
import it.krzeminski.githubactions.scriptmodel.YamlWorkflowTriggers
import it.krzeminski.githubactions.scriptmodel.myYaml
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString

class CaseEnumSerializerTest : FunSpec({
    test("when deserialize type Enums, use PascalCase") {
        myYaml.decodeFromString<PullRequest.Type>("opened") shouldBe PullRequest.Type.Opened
        myYaml.decodeFromString<PullRequest.Type>("review_requested") shouldBe PullRequest.Type.ReviewRequested
        myYaml.encodeToString(PullRequest.Type.ReviewRequested) shouldBe "\"review_requested\""

        val input =
            """|pull_request:
                |  types:
                |      - 'opened'
                |      - 'review_requested'
                |""".trimMargin()
        val triggers = myYaml.decodeFromString<YamlWorkflowTriggers>(input)
        triggers.pull_request shouldBe PullRequest(listOf(PullRequest.Type.Opened, PullRequest.Type.ReviewRequested))
    }
})
