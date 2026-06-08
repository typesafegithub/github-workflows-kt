package io.github.typesafegithub.workflows.domain

import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.assertions.withClue
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

            test("all supported runner labels should correspond to lib's classes") {
                val fromLib =
                    RunnerType.GitHubHosted::class
                        .sealedSubclasses
                        .mapNotNull { it.objectInstance?.value }
                        .toSet()
                val fromDocs = listRunnerLabels().toSet()

                assertSoftly {
                    withClue("no obsolete labels are in the lib") {
                        fromLib - fromDocs shouldBe emptySet<String>()
                    }

                    withClue("no missing labels are in the lib") {
                        fromDocs - fromLib shouldBe emptySet<String>()
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
