package it.krzeminski.githubactions.actions

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import it.krzeminski.githubactions.domain.ExternalActionStep
import it.krzeminski.githubactions.yaml.stepsToYaml

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
            )
        )
        val steps = listOf(
            ExternalActionStep(
                id = "latex",
                name = "Latex",
                action = customAction
            )
        )

        // when
        val yaml = steps.stepsToYaml()

        // then
        yaml shouldBe """|- id: latex
                         |  name: Latex
                         |  uses: xu-cheng/latex-action@v2
                         |  with:
                         |    root_file: report.tex
                         |    compiler: latexmk""".trimMargin()

        // given
        val outputs = customAction.buildOutputObject("someStepId")

        // when & then
        outputs["custom-output"] shouldBe "steps.someStepId.outputs.custom-output"
    }
})
