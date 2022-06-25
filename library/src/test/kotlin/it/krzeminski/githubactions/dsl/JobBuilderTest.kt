package it.krzeminski.githubactions.dsl

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import it.krzeminski.githubactions.actions.actions.CheckoutV3
import it.krzeminski.githubactions.actions.actions.SetupJavaV3
import it.krzeminski.githubactions.actions.actions.SetupJavaV3.Distribution.Adopt
import it.krzeminski.githubactions.domain.RunnerType
import it.krzeminski.githubactions.domain.triggers.Push
import java.nio.file.Paths

class JobBuilderTest : FunSpec({
    context("job builder") {
        test("with custom step arguments") {
            // Given
            val workflow = workflow(
                name = "Test workflow",
                on = listOf(Push()),
                sourceFile = Paths.get(".github/workflows/some_workflow.main.kts"),
            ) {
                job(
                    id = "test_job",
                    runsOn = RunnerType.UbuntuLatest,
                ) {
                    run(
                        name = "Hello world!",
                        command = "echo 'hello!'",
                        _customArguments = mapOf("foo0" to BooleanCustomValue(true)),
                    )
                    run(
                        command = "echo 'hello!'",
                        _customArguments = mapOf("foo1" to BooleanCustomValue(true)),
                    )
                    uses(
                        name = "Check out",
                        action = CheckoutV3(),
                        _customArguments = mapOf("foo2" to BooleanCustomValue(true)),
                    )
                    uses(
                        action = CheckoutV3(),
                        _customArguments = mapOf("foo3" to BooleanCustomValue(true)),
                    )
                    uses(
                        name = "Set up Java",
                        action = SetupJavaV3(distribution = Adopt, javaVersion = "11"),
                        _customArguments = mapOf("foo4" to BooleanCustomValue(true)),
                    )
                    uses(
                        action = SetupJavaV3(distribution = Adopt, javaVersion = "11"),
                        _customArguments = mapOf("foo5" to BooleanCustomValue(true)),
                    )
                }
            }

            // When
            val job = workflow.jobs.first()

            // Then
            job.steps.forEachIndexed { index, step ->
                step._customArguments shouldBe mapOf("foo$index" to BooleanCustomValue(true))
            }
        }
    }
})
