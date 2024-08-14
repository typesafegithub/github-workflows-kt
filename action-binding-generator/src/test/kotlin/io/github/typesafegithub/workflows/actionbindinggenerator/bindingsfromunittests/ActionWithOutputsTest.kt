package io.github.typesafegithub.workflows.actionbindinggenerator.bindingsfromunittests

import io.github.typesafegithub.workflows.actions.johnsmith.ActionWithOutputs
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class ActionWithOutputsTest : DescribeSpec({
    it("fields have correct output placeholders") {
        // given
        val outputs = ActionWithOutputs(fooBar = "1").buildOutputObject("someStepId")

        // when & then
        outputs.bazGoo shouldBe "steps.someStepId.outputs.baz-goo"
        outputs.looWoz shouldBe "steps.someStepId.outputs.loo-woz"
        outputs["custom-output"] shouldBe "steps.someStepId.outputs.custom-output"
    }
})
