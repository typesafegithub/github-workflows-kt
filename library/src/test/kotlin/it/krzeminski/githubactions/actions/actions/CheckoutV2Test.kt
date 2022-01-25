package it.krzeminski.githubactions.actions.actions

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import it.krzeminski.githubactions.actions.actions.CheckoutV2.FetchDepth

class CheckoutV2Test : DescribeSpec({
    it("renders with defaults") {
        // given
        val action = CheckoutV2()

        // when
        val yaml = action.toYamlArguments()

        // then
        yaml shouldBe linkedMapOf()
    }

    describe("fetch-depth parameter") {
        it("renders with specified concrete fetch depth") {
            // given
            val action = CheckoutV2(
                fetchDepth = FetchDepth.Quantity(3),
            )

            // when
            val yaml = action.toYamlArguments()

            // then
            yaml shouldBe linkedMapOf(
                "fetch-depth" to "3",
            )
        }

        it("renders with specified infinite fetch depth") {
            // given
            val action = CheckoutV2(
                fetchDepth = FetchDepth.Infinite,
            )

            // when
            val yaml = action.toYamlArguments()

            // then
            yaml shouldBe linkedMapOf(
                "fetch-depth" to "0",
            )
        }
    }
})
