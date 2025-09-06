package io.github.typesafegithub.workflows.updates

import io.github.typesafegithub.workflows.domain.RunnerType
import io.github.typesafegithub.workflows.domain.Workflow
import io.github.typesafegithub.workflows.domain.actions.CustomAction
import io.github.typesafegithub.workflows.domain.triggers.Push
import io.github.typesafegithub.workflows.dsl.workflow
import io.github.typesafegithub.workflows.shared.internal.getGithubAuthTokenOrNull
import io.kotest.common.ExperimentalKotest
import io.kotest.core.spec.style.FunSpec
import io.kotest.engine.spec.tempdir
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldNotBeEmpty
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalKotest::class)
class ReportingTest :
    FunSpec(
        {
            val token = getGithubAuthTokenOrNull()
            context("tests using github token").config(enabled = token != null) {
                val gitRootDir =
                    tempdir()
                        .also {
                            it.resolve(".git").mkdirs()
                        }.toPath()

                val sourceTempFile = gitRootDir.resolve(".github/workflows/some_workflow.main.kts").toFile()

                fun <T> useWithWorkflow(function: (Workflow) -> T): T {
                    var output: T? = null
                    workflow(
                        name = "Test workflow",
                        on = listOf(Push()),
                        sourceFile = sourceTempFile,
                        useWorkflow = { output = function(it) },
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
                                        actionVersion = "v3", // Keep newest, to test no updates reported.
                                    ),
                            )
                        }
                    }
                    return output ?: error("The value should be set in useWorkflow callback!")
                }

                sourceTempFile.absoluteFile.parentFile.mkdirs()
                sourceTempFile.createNewFile()

                val availableVersionsForEachAction =
                    useWithWorkflow { runBlocking { it.availableVersionsForEachAction() } }
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
                        val (_, line) = useWithWorkflow { it.findDependencyDeclaration(regularActionVersions.action) }
                        line shouldBe (index + 1)
                    }
                }

                test("do nothing with missing token") {
                    useWithWorkflow {
                        runBlocking {
                            it.availableVersionsForEachAction(
                                reportWhenTokenUnset = false,
                                githubAuthToken = null,
                            )
                        }
                    }.toList()
                        .shouldHaveSize(0)
                }

                test("should write to github step summary()") {
                    val summary =
                        buildString {
                            useWithWorkflow {
                                runBlocking {
                                    it.reportAvailableUpdatesInternal(
                                        stepSummary =
                                            object : GithubStepSummary {
                                                override fun appendText(text: String) {
                                                    this@buildString.append(text)
                                                }
                                            },
                                    )
                                }
                            }
                        }

                    summary.trim().shouldNotBeEmpty()
                }
            }
        },
    )
