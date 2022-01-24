package it.krzeminski.githubactions.actions.madhead

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class SemverUtilsTest : DescribeSpec({
    it("renders with defaults") {
        // given
        val action = SemverUtils("1.2.3")

        // when
        val yaml = action.toYamlArguments()

        // then
        yaml shouldBe linkedMapOf(
            "version" to "1.2.3",
        )
    }

    describe("compareTo parameter") {
        it("renders with specified compareTo parameter") {
            // given
            val action = SemverUtils(
                version = "1.2.3",
                compareTo = "1.2.4",
            )

            // when
            val yaml = action.toYamlArguments()

            // then
            yaml shouldBe linkedMapOf(
                "version" to "1.2.3",
                "compareTo" to "1.2.4",
            )
        }
    }

    describe("satisfies parameter") {
        it("renders with specified satisfies parameter") {
            // given
            val action = SemverUtils(
                version = "1.2.3",
                satisfies = "1.0+",
            )

            // when
            val yaml = action.toYamlArguments()

            // then
            yaml shouldBe linkedMapOf(
                "version" to "1.2.3",
                "satisfies" to "1.0+",
            )
        }
    }

    describe("identifier parameter") {
        it("renders with specified identifier parameter") {
            // given
            val action = SemverUtils(
                version = "1.2.3",
                identifier = "major",
            )

            // when
            val yaml = action.toYamlArguments()

            // then
            yaml shouldBe linkedMapOf(
                "version" to "1.2.3",
                "identifier" to "major",
            )
        }
    }
})
