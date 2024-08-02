package io.github.typesafegithub.workflows.actionbindinggenerator.bindingsfromunittests

import io.github.typesafegithub.workflows.actions.johnsmith.ActionWithSomeOptionalInputs
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class ActionWithSomeOptionalInputsTest : DescribeSpec({
    it("renders with defaults") {
        // given
        val action = ActionWithSomeOptionalInputs(
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
