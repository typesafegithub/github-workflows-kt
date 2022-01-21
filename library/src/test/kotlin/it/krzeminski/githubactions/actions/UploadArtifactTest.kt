package it.krzeminski.githubactions.actions

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class UploadArtifactTest : DescribeSpec({
    it("renders with defaults") {
        // given
        val action = UploadArtifact(
            artifactName = "CoolArtifact",
            path = listOf(
                "/some/path",
                "/another/path",
            )
        )

        // when
        val yaml = action.toYamlArguments()

        // then
        yaml shouldBe linkedMapOf(
            "name" to "CoolArtifact",
            "path" to "/some/path\n/another/path",
        )
    }
})
