package io.github.typesafegithub.workflows.domain

import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.util.Locale

class RunnerTypeTest :
    FunSpec({
        context("GitHub-hosted runners") {
            test("classes should have names derived from labels using the convention") {
                val runnerClasses =
                    RunnerType.GitHubHosted::class
                        .sealedSubclasses

                assertSoftly {
                    runnerClasses.forEach { runnerClass ->
                        val actualClassName = runnerClass.simpleName
                        val label = runnerClass.objectInstance!!.value
                        val classNameFromLabel =
                            label.split("-").joinToString("") {
                                it
                                    .replaceFirstChar {
                                        if (it.isLowerCase()) {
                                            it.titlecase(
                                                Locale.getDefault(),
                                            )
                                        } else {
                                            it.toString()
                                        }
                                    }.replace(".", "")
                            }

                        actualClassName shouldBe classNameFromLabel
                    }
                }
            }
        }

        context("Labelled") {
            test("should throw on invalid arguments") {
                shouldThrow<IllegalArgumentException> {
                    RunnerType.Labelled()
                }
                shouldThrow<IllegalArgumentException> {
                    RunnerType.Labelled(emptySet())
                }
            }
        }
    })
