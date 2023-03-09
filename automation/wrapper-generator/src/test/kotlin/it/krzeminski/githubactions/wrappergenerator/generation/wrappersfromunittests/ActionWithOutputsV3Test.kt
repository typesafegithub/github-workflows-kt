package it.krzeminski.githubactions.wrappergenerator.generation.wrappersfromunittests

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import it.krzeminski.githubactions.actions.johnsmith.ActionWithOutputsV3

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
