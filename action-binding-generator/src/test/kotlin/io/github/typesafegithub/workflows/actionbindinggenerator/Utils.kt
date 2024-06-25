package io.github.typesafegithub.workflows.actionbindinggenerator

import io.github.typesafegithub.workflows.actionbindinggenerator.generation.ActionBinding
import io.kotest.assertions.fail
import io.kotest.matchers.shouldBe
import java.nio.file.Paths

fun ActionBinding.shouldMatchFile(path: String) {
    val file =
        Paths
            .get("src/test/kotlin/io/github/typesafegithub/workflows/actionbindinggenerator/bindingsfromunittests/$path")
            .toFile()
    val expectedContent =
        when {
            file.canRead() -> file.readText().removeWindowsNewLines()
            else -> ""
        }
    val actualContent = kotlinCode.removeWindowsNewLines()

    filePath shouldBe "github-workflows-kt/src/gen/kotlin/io/github/typesafegithub/workflows/actions/johnsmith/$path"

    if (System.getenv("GITHUB_ACTIONS") == "true") {
        actualContent shouldBe expectedContent
    } else if (actualContent != expectedContent) {
        file.writeText(actualContent)
        fail(
            "The binding's Kotlin code in ${file.name} doesn't match the expected one.\n" +
                "The file has been updated to match what's expected.",
        )
    }
}

private fun String.removeWindowsNewLines(): String = replace("\r\n", "\n")
