package it.krzeminski.githubactions.wrappergenerator.generation.wrappersfromunittests

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import it.krzeminski.githubactions.actions.johnsmith.ActionWithSomeOptionalInputsV3

class ActionWithSomeOptionalInputsV3Test : DescribeSpec({
    it("renders with defaults") {
        // given
        val action = ActionWithSomeOptionalInputsV3(
            bazGoo = "def456",
            bonTon = "qwe789"
        )

        // when
        val yaml = action.toYamlArguments()

        // then
        yaml shouldBe linkedMapOf(
            "baz-goo" to "def456",
            "bon-ton" to "qwe789",
        )
    }
})
