package io.github.typesafegithub.workflows.wrappergenerator.generation.wrappersfromunittests

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.github.typesafegithub.workflows.actions.johnsmith.SimpleActionWithRequiredStringInputsV3

class SimpleActionWithRequiredStringInputsV3Test : DescribeSpec({
    it("renders with defaults") {
        // given
        val action = SimpleActionWithRequiredStringInputsV3(
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
