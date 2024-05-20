package io.github.typesafegithub.workflows.updates

import io.github.typesafegithub.workflows.domain.RunnerType
import io.github.typesafegithub.workflows.domain.actions.CustomAction
import io.github.typesafegithub.workflows.domain.triggers.Push
import io.github.typesafegithub.workflows.dsl.workflow
import io.github.typesafegithub.workflows.shared.internal.getGithubTokenOrNull
import io.kotest.common.ExperimentalKotest
import io.kotest.core.spec.style.FunSpec
import io.kotest.engine.spec.tempdir
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldNotBeEmpty
import kotlinx.coroutines.flow.toList

@OptIn(ExperimentalKotest::class)
class ReportingTest : FunSpec(
    {
        val token = getGithubTokenOrNull()
        context("tests using github token").config(enabled = token != null) {
            val gitRootDir =
                tempdir().also {
                    it.resolve(".git").mkdirs()
                }.toPath()

            val sourceTempFile = gitRootDir.resolve(".github/workflows/some_workflow.main.kts").toFile()

            val workflow =
                workflow(
                    name = "Test workflow",
                    on = listOf(Push()),
                    sourceFile = sourceTempFile,
                ) {
                    job(
                        id = "test_job",
                        runsOn = RunnerType.UbuntuLatest,
                    ) {
                        uses(
                            name = "Check out",
                            action =
                                CustomAction(
                                    actionOwner = "actions",
                                    actionName = "checkout",
                                    actionVersion = "v3",
                                ),
                        )
                        uses(
                            action =
                                CustomAction(
                                    actionOwner = "actions",
                                    actionName = "setup-java",
                                    actionVersion = "v2",
                                ),
                        )
                        uses(
                            action =
                                CustomAction(
                                    actionOwner = "google-github-actions",
                                    actionName = "setup-gcloud",
                                    actionVersion = "v2",
                                ),
                        )
                    }
                }

            sourceTempFile.absoluteFile.parentFile.mkdirs()
            sourceTempFile.createNewFile()

            val availableVersionsForEachAction =
                workflow
                    .availableVersionsForEachAction()
                    .toList()

            // write dependency notations into a file,
            // so we can test finding the correct line numbers
            availableVersionsForEachAction.forEach {
                val coordinates = it.action.mavenCoordinatesForAction()
                sourceTempFile.appendText(
                    "@file:DependsOn(\"$coordinates\")\n",
                )
            }
            test("there should be 3 actions with available versions") {
                availableVersionsForEachAction
                    .shouldHaveSize(3)
            }
            test("there should be 2 actions with updates available") {
                availableVersionsForEachAction
                    .filter { it.newerVersions.isNotEmpty() }
                    .shouldHaveSize(2)
            }
            test("dependency annotations can be looked up") {
                availableVersionsForEachAction.forEachIndexed { index, regularActionVersions ->
                    val (_, line) = workflow.findDependencyDeclaration(regularActionVersions.action)
                    line shouldBe (index + 1)
                }
            }

            test("do nothing with missing token") {
                workflow
                    .availableVersionsForEachAction(
                        reportWhenTokenUnset = false,
                        githubToken = null,
                    )
                    .toList()
                    .shouldHaveSize(0)
            }

            test("should write to github step summary()") {
                val summary =
                    buildString {
                        workflow.reportAvailableUpdatesInternal(
                            stepSummary =
                                object : GithubStepSummary {
                                    override fun appendText(text: String) {
                                        this@buildString.append(text)
                                    }
                                },
                        )
                    }

                summary.trim().shouldNotBeEmpty()
            }
        }
    },
)
