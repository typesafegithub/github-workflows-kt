package io.github.typesafegithub.workflows.actionbindinggenerator.generation

import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.startWith
import java.lang.IllegalArgumentException

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

    test("nested actions") {
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
                  - uses: actions/cache/restore@v5
            """.trimIndent()

        // When
        val actionCoords = extractUsedActionsFromWorkflow(manifest)

        // Then
        actionCoords shouldBe
            listOf(
                ActionCoords(owner = "actions", name = "checkout", version = "v3"),
                ActionCoords(owner = "actions", name = "cache/restore", version = "v5"),
            )
    }

    test("workflow with not only 'uses' steps") {
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

    test("multiple versions of the same action") {
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
                  - uses: actions/setup-java@v4
                  - uses: actions/checkout@v5
              another-job:
                runs-on: ubuntu-latest
                steps:
                  - uses: actions/setup-java@v4
            """.trimIndent()

        // Then
        shouldThrow<IllegalArgumentException> {
            // When
            extractUsedActionsFromWorkflow(manifest)
        }.also {
            it.message should startWith("Multiple versions defined for actions: [actions/checkout]")
        }
    }

    test("workflow with zero jobs") {
        // Given
        val manifest =
            """
            on:
              push:

            jobs:
            """.trimIndent()

        // When
        val actionCoords = extractUsedActionsFromWorkflow(manifest)

        // Then
        actionCoords shouldBe emptyList()
    }

    test("workflow with no 'jobs' key") {
        // Given
        val manifest =
            """
            on:
              push:
            """.trimIndent()

        // When
        val actionCoords = extractUsedActionsFromWorkflow(manifest)

        // Then
        actionCoords shouldBe emptyList()
    }

    test("malformed YAML") {
        // Given
        val manifest =
            """
            on:--
              push:
            """.trimIndent()

        // Then
        shouldThrow<IllegalArgumentException> {
            // When
            extractUsedActionsFromWorkflow(manifest)
        }.also {
            it.message should startWith("The YAML is invalid:")
        }
    }
})
