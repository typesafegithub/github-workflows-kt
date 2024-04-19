package io.github.typesafegithub.workflows.dsl

import io.github.typesafegithub.workflows.domain.RunnerType
import io.github.typesafegithub.workflows.domain.actions.CustomAction
import io.github.typesafegithub.workflows.domain.triggers.Push
import io.github.typesafegithub.workflows.updates.getAvailableActionVersionUpdates
import io.kotest.core.spec.style.FunSpec
import io.kotest.engine.spec.tempdir
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull

class CheckVersionsTests : FunSpec({
    val gitRootDir =
        tempdir().also {
            it.resolve(".git").mkdirs()
        }.toPath()
    test("checkForNewerVersions()") {
        // given
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
                                actionOwner = "google-github-actions",
                                actionName = "setup-gcloud",
                                actionVersion = "v1",
                            ),
                    )
                }
            }

        workflow.getAvailableActionVersionUpdates()
            .shouldNotBeNull()
            .shouldHaveSize(2)
    }
})
