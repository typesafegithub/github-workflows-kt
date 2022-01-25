package it.krzeminski.githubactions.actions.actions

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import it.krzeminski.githubactions.actions.actions.SetupJavaV2.Distribution.Adopt
import it.krzeminski.githubactions.actions.actions.SetupJavaV2.Distribution.Custom

class SetupJavaV2Test : DescribeSpec({
    it("renders with defaults") {
        // given
        val action = SetupJavaV2(
            distribution = Adopt,
            javaVersion = "11",
        )

        // when
        val yaml = action.toYamlArguments()

        // then
        yaml shouldBe linkedMapOf(
            "distribution" to "adopt",
            "java-version" to "11",
        )
    }

    it("renders with custom distribution") {
        // given
        val action = SetupJavaV2(
            distribution = Custom("my-custom-distribution"),
            javaVersion = "11",
        )

        // when
        val yaml = action.toYamlArguments()

        // then
        yaml shouldBe linkedMapOf(
            "distribution" to "my-custom-distribution",
            "java-version" to "11",
        )
    }
})
