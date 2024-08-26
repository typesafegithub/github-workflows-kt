package io.github.typesafegithub.workflows.actionbindinggenerator

import io.github.typesafegithub.workflows.actionbindinggenerator.generation.ActionBinding
import io.kotest.matchers.Matcher.Companion.failure
import io.kotest.matchers.shouldBe
import java.nio.file.Paths

fun List<ActionBinding>.shouldContainAndMatchFile(path: String) {
    val binding =
        this
            .firstOrNull { it.filePath.endsWith(path) }
            ?: error("Binding with path ending with $path not found!")
    val file =
        Paths
            .get("src/test/kotlin/io/github/typesafegithub/workflows/actionbindinggenerator/bindingsfromunittests/$path")
            .toFile()
    val expectedContent =
        when {
            file.canRead() -> file.readText().removeWindowsNewLines()
            else -> ""
        }
    val actualContent = binding.kotlinCode.removeWindowsNewLines()

    binding.filePath shouldBe "kotlin/io/github/typesafegithub/workflows/actions/johnsmith/$path"

    if (System.getenv("GITHUB_ACTIONS") == "true") {
        actualContent shouldBe expectedContent
    } else if (actualContent != expectedContent) {
        file.writeText(actualContent)
        actualContent shouldBe
            failure<Nothing>(
                "The binding's Kotlin code in ${file.name} doesn't match the expected one.\n" +
                    "The file has been updated to match what's expected.",
            )
    }
}

private fun String.removeWindowsNewLines(): String = replace("\r\n", "\n")
