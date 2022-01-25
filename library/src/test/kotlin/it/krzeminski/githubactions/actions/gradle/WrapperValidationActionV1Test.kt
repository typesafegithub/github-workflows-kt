package it.krzeminski.githubactions.actions.gradle

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class WrapperValidationActionV1Test : DescribeSpec({
    it("renders with defaults") {
        // given
        val action = WrapperValidationActionV1()

        // when
        val yaml = action.toYamlArguments()

        // then
        yaml shouldBe linkedMapOf()
    }
})
