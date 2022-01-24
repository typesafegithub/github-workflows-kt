package it.krzeminski.githubactions.actions.madhead

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class CheckGradleVersionV1Test : DescribeSpec({
    it("renders with defaults") {
        // given
        val action = CheckGradleVersionV1()

        // when
        val yaml = action.toYamlArguments()

        // then
        yaml shouldBe linkedMapOf()
    }

    describe("gradlew parameter") {
        it("renders with specified gradlew parameter") {
            // given
            val action = CheckGradleVersionV1(
                gradlew = "path/to/gradlew"
            )

            // when
            val yaml = action.toYamlArguments()

            // then
            yaml shouldBe linkedMapOf(
                "gradlew" to "path/to/gradlew"
            )
        }
    }
})
