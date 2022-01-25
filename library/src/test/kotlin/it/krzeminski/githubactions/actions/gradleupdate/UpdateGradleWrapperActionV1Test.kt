package it.krzeminski.githubactions.actions.gradleupdate

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class UpdateGradleWrapperActionV1Test : DescribeSpec({
    it("renders with defaults") {
        // given
        val action = UpdateGradleWrapperActionV1()

        // when
        val yaml = action.toYamlArguments()

        // then
        yaml shouldBe linkedMapOf()
    }
})
