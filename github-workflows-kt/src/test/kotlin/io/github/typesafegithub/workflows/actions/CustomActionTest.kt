package io.github.typesafegithub.workflows.actions

import io.github.typesafegithub.workflows.domain.Expression
import io.github.typesafegithub.workflows.domain.actions.CustomAction
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class CustomActionTest :
    FunSpec({

        test("custom action") {
            // given
            val customAction =
                CustomAction(
                    actionOwner = "xu-cheng",
                    actionName = "latex-action",
                    actionVersion = "v2",
                    inputs =
                        mapOf(
                            "root_file" to "report.tex",
                            "compiler" to "latexmk",
                        ),
                )

            // given
            val outputs = customAction.buildOutputObject("someStepId")

            // when & then
            outputs["custom-output"] shouldBe Expression("steps.someStepId.outputs.custom-output")
        }
    })
