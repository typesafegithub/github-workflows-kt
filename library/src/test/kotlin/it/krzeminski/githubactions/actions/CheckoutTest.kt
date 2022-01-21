package it.krzeminski.githubactions.actions

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class CheckoutTest : DescribeSpec({
    it("renders with defaults") {
        // given
        val action = Checkout()

        // when
        val yaml = action.toYamlArguments()

        // then
        yaml shouldBe linkedMapOf(
            "fetch-depth" to "1",
        )
    }

    describe("fetch-depth parameter") {
        it("renders with specified concrete fetch depth") {
            // given
            val action = Checkout(
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
            val action = Checkout(
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
