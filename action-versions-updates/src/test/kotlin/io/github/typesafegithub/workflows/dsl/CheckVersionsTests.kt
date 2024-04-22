package io.github.typesafegithub.workflows.dsl

import io.github.typesafegithub.workflows.domain.RunnerType
import io.github.typesafegithub.workflows.domain.actions.CustomAction
import io.github.typesafegithub.workflows.domain.triggers.Push
import io.github.typesafegithub.workflows.updates.GithubStepSummary
import io.github.typesafegithub.workflows.updates.availableVersionsForEachAction
import io.github.typesafegithub.workflows.updates.findDependencyDeclaration
import io.github.typesafegithub.workflows.updates.mavenCoordinatesForAction
import io.github.typesafegithub.workflows.updates.reportAvailableUpdates
import io.kotest.core.spec.style.FunSpec
import io.kotest.engine.spec.tempdir
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldNotBeEmpty

class CheckVersionsTests : FunSpec(
    {
        val gitRootDir =
            tempdir().also {
                it.resolve(".git").mkdirs()
            }.toPath()

        val sourceTempFile = gitRootDir.resolve(".github/workflows/some_workflow.main.kts").toFile()

        val workflow =
            workflow(
                name = "Test workflow",
                on = listOf(Push()),
                sourceFile = sourceTempFile.toPath(),
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
                .shouldHaveSize(0)
        }

        test("should write to github step summary()") {
            val summary =
                buildString {
                    workflow.reportAvailableUpdates(
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
    },
)
