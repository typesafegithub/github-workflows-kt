package io.github.typesafegithub.workflows

import com.tschuchort.compiletesting.JvmCompilationResult
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.KotlinCompilation.ExitCode.COMPILATION_ERROR
import com.tschuchort.compiletesting.SourceFile
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.intellij.lang.annotations.Language
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi

@OptIn(ExperimentalCompilerApi::class)
class NonCompilableTest : FunSpec({
    test("job nested inside a job") {
        val compilationResult =
            compile(
                code =
                    """
                    import io.github.typesafegithub.workflows.dsl.workflow
                    import io.github.typesafegithub.workflows.actions.actions.CheckoutV4
                    import io.github.typesafegithub.workflows.domain.RunnerType
                    import io.github.typesafegithub.workflows.domain.triggers.Push
                    import java.nio.file.Path

                    val workflow = workflow(
                        name = "Test workflow",
                        on = listOf(Push()),
                        sourceFile = Path.of(".github/workflows/some_workflow.main.kts"),
                    ) {
                        job(
                            id = "test_job",
                            name = "Test Job",
                            runsOn = RunnerType.UbuntuLatest,
                        ) {
                            uses(action = CheckoutV4())
                            run(command = "echo 'hello!'")

                            job(
                                id = "test_job",
                                name = "Test Job",
                                runsOn = RunnerType.UbuntuLatest,
                            ) {
                                uses(action = CheckoutV4())
                                run(command = "echo 'hello!'")
                            }
                        }
                    }
                    """.trimIndent(),
            )

        compilationResult.exitCode shouldBe COMPILATION_ERROR
        compilationResult.messages shouldContain "can't be called in this context by implicit receiver"
    }

    test("no named argument") {
        val compilationResult =
            compile(
                code =
                    """
                    import io.github.typesafegithub.workflows.dsl.workflow
                    import io.github.typesafegithub.workflows.actions.actions.CheckoutV4
                    import io.github.typesafegithub.workflows.domain.RunnerType
                    import io.github.typesafegithub.workflows.domain.triggers.Push
                    import java.nio.file.Path

                    val workflow = workflow(
                        name = "Test workflow",
                        on = listOf(Push()),
                        sourceFile = Path.of(".github/workflows/some_workflow.main.kts"),
                    ) {
                        job(
                            "test_job",
                            name = "Test Job",
                            runsOn = RunnerType.UbuntuLatest,
                        ) {
                            uses(action = CheckoutV4())
                            run(command = "echo 'hello!'")
                        }
                    }
                    """.trimIndent(),
            )

        compilationResult.exitCode shouldBe COMPILATION_ERROR
        compilationResult.messages shouldContain "No value passed for parameter 'id'"
    }
})

@OptIn(ExperimentalCompilerApi::class)
private fun compile(
    @Language("kotlin") code: String,
): JvmCompilationResult =
    KotlinCompilation().apply {
        sources =
            listOf(
                SourceFile.kotlin(
                    name = "MyWorkflow.kt",
                    contents = code,
                ),
            )
        inheritClassPath = true
    }.compile()
