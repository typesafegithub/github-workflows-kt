package it.krzeminski.githubactions.actions.gradle

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class WrapperValidationActionV1Test : DescribeSpec({
    it("renders with defaults") {
        // given
        val action = WrapperValidationActionV1()

        // when
        val yaml = action.toYamlArguments()

        // then
        yaml shouldBe linkedMapOf()
    }

    it("renders with all parameters") {
        // given
        val action = WrapperValidationActionV1(
            minWrapperCount = 3,
            allowSnapshots = true,
            allowChecksums = listOf("checksum1", "checksum2"),
        )

        // when
        val yaml = action.toYamlArguments()

        // then
        yaml shouldBe linkedMapOf(
            "min-wrapper-count" to "3",
            "allow-snapshots" to "true",
            "allow-checksums" to "checksum1,checksum2",
        )
    }
})
