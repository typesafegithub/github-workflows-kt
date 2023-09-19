package io.github.typesafegithub.workflows.actionbindinggenerator.wrappersfromunittests

import io.github.typesafegithub.workflows.actions.johnsmith.ActionWithSomeOptionalInputsV3
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class ActionWithSomeOptionalInputsV3Test : DescribeSpec({
    it("renders with defaults") {
        // given
        val action = ActionWithSomeOptionalInputsV3(
            bazGoo = "def456",
            `package` = "qwe789"
        )

        // when
        val yaml = action.toYamlArguments()

        // then
        yaml shouldBe linkedMapOf(
            "baz-goo" to "def456",
            "package" to "qwe789",
        )
    }
})
