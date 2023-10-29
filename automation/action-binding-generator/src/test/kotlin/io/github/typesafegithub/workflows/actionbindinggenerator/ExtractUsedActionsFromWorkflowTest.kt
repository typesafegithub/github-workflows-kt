package io.github.typesafegithub.workflows.actionbindinggenerator

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class ExtractUsedActionsFromWorkflowTest : FunSpec({
    test("parses valid manifest just with 'uses' steps") {
        // Given
        val manifest =
            """
            on:
              push:
                branches: [this-branch-must-never-be-created]

            jobs:
              some-job:
                runs-on: ubuntu-latest
                steps:
                  - uses: actions/checkout@v3
                  - uses: actions/setup-java@main
                  - uses: typesafegithub/github-actions-typing@d34db33f
            """.trimIndent()

        // When
        val actionCoords = extractUsedActionsFromWorkflow(manifest)

        // Then
        actionCoords shouldBe
            listOf(
                ActionCoords(owner = "actions", name = "checkout", version = "v3"),
                ActionCoords(owner = "actions", name = "setup-java", version = "main"),
                ActionCoords(owner = "typesafegithub", name = "github-actions-typing", version = "d34db33f"),
            )
    }

    test("workflow with not only with 'uses' steps") {
        // Given
        val manifest =
            """
            on:
              push:

            jobs:
              some-job:
                runs-on: ubuntu-latest
                steps:
                  - uses: actions/checkout@v3
                  - run: echo "Hello world!"
                  - uses: actions/setup-java@main
            """.trimIndent()

        // When
        val actionCoords = extractUsedActionsFromWorkflow(manifest)

        // Then
        actionCoords shouldBe
            listOf(
                ActionCoords(owner = "actions", name = "checkout", version = "v3"),
                ActionCoords(owner = "actions", name = "setup-java", version = "main"),
            )
    }

    // TODO nested actions, i.e. where names have some "/"

    // TODO: steps using other kinds of actions, like Docker-based or local ones

    // TODO: malformed YAML

    // TODO: no jobs

    // TODO: multiple versions of the same action
})
