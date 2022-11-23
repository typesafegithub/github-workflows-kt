package it.krzeminski.githubactions.actions

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class CustomActionTest : FunSpec({

    test("custom action") {
        // given
        val customAction = CustomAction(
            actionOwner = "xu-cheng",
            actionName = "latex-action",
            actionVersion = "v2",
            inputs = linkedMapOf(
                "root_file" to "report.tex",
                "compiler" to "latexmk",
            ),
        )

        // given
        val outputs = customAction.buildOutputObject("someStepId")

        // when & then
        outputs["custom-output"] shouldBe "steps.someStepId.outputs.custom-output"
    }
},)
