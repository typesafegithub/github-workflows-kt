package io.github.typesafegithub.workflows.domain

import io.github.typesafegithub.workflows.actions.actions.Checkout
import io.github.typesafegithub.workflows.domain.AbstractResult.Status
import io.github.typesafegithub.workflows.domain.actions.Action
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class StepTest :
    FunSpec({
        test("step.outcome") {
            val step0: Step<*> = CommandStep(id = "step-0", command = "ls")
            step0.outcome.toString() shouldBe "steps.step-0.outcome"
            step0.outcome eq Status.Failure shouldBe "steps.step-0.outcome == 'failure'"
            step0.outcome eq Status.Cancelled shouldBe "steps.step-0.outcome == 'cancelled'"
            step0.outcome eq Status.Skipped shouldBe "steps.step-0.outcome == 'skipped'"
            step0.outcome eq Status.Success shouldBe "steps.step-0.outcome == 'success'"
        }
        test("step.conclusion") {
            val someStep: Step<*> =
                ActionStep(
                    id = "whatever",
                    action = Checkout(),
                )
            someStep.conclusion.toString() shouldBe "steps.whatever.conclusion"
            someStep.conclusion eq Status.Failure shouldBe "steps.whatever.conclusion == 'failure'"
            someStep.conclusion eq Status.Cancelled shouldBe "steps.whatever.conclusion == 'cancelled'"
            someStep.conclusion eq Status.Skipped shouldBe "steps.whatever.conclusion == 'skipped'"
            someStep.conclusion eq Status.Success shouldBe "steps.whatever.conclusion == 'success'"
        }
        test("step.outputs") {
            val step0: Step<*> = CommandStep(id = "step-0", command = "ls")
            step0.outputs["foo"] shouldBe "steps.step-0.outputs.foo"
        }

        test("incompatible library version with the binding") {
            // Given
            val incompatibleAction =
                object : Action<Action.Outputs>() {
                    override fun isCompatibleWithLibraryVersion(libraryVersion: String): Boolean = false

                    override fun toYamlArguments(): LinkedHashMap<String, String> =
                        throw NotImplementedError("Irrelevant for this test")

                    override fun buildOutputObject(stepId: String): Outputs = Outputs(stepId)

                    override val usesString: String
                        get() = throw NotImplementedError("Irrelevant for this test")
                }

            // When
            shouldThrow<AssertionError> {
                ActionStep(
                    id = "whatever",
                    action = incompatibleAction,
                )
            }.also {
                it.message shouldBe "This version of the library is not compatible with the provided action binding!"
            }
        }
    })
