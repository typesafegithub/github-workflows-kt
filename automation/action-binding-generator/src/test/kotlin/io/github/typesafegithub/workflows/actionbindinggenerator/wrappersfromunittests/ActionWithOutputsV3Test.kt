package io.github.typesafegithub.workflows.actionbindinggenerator.wrappersfromunittests

import io.github.typesafegithub.workflows.actions.johnsmith.ActionWithOutputsV3
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class ActionWithOutputsV3Test : DescribeSpec({
    it("fields have correct output placeholders") {
        // given
        val outputs = ActionWithOutputsV3(fooBar = "1").buildOutputObject("someStepId")

        // when & then
        outputs.bazGoo shouldBe "steps.someStepId.outputs.baz-goo"
        outputs.looWoz shouldBe "steps.someStepId.outputs.loo-woz"
        outputs["custom-output"] shouldBe "steps.someStepId.outputs.custom-output"
    }
})
