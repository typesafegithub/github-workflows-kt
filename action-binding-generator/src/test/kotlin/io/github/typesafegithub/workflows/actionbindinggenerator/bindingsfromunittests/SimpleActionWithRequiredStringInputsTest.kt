package io.github.typesafegithub.workflows.actionbindinggenerator.bindingsfromunittests

import io.github.typesafegithub.workflows.actions.johnsmith.SimpleActionWithRequiredStringInputs
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class SimpleActionWithRequiredStringInputsTest : DescribeSpec({
    it("renders with defaults") {
        // given
        val action = SimpleActionWithRequiredStringInputs(
            fooBar = "abc123",
            bazGoo = "def456",
        )

        // when
        val yaml = action.toYamlArguments()

        // then
        yaml shouldBe linkedMapOf(
            "foo-bar" to "abc123",
            "baz-goo" to "def456",
        )
    }
})
