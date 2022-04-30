package it.krzeminski.githubactions.kotest

import io.kotest.assertions.fail
import io.kotest.matchers.shouldBe
import java.io.File

fun setupShouldMatchFile(
    baseDirectory: File = File("src/test/resources"),
    normalize: (String) -> String = { it },
) {
    settingsBaseDirectory = baseDirectory
    settingsNormalizeString = normalize
    baseDirectory.resolve("actual").deleteRecursively()
    baseDirectory.resolve("actual").mkdirs()
}

private var settingsBaseDirectory = File("src/test/resources")
private var settingsNormalizeString: (String) -> String = { it }

infix fun String.shouldMatchFile(path: String) {
    val expectedFile = settingsBaseDirectory.resolve("expected/$path")
    val actualFile = settingsBaseDirectory.resolve("actual/$path")

    val actual = settingsNormalizeString(this)
    val expected = when {
        expectedFile.canRead().not() -> ""
        else -> settingsNormalizeString(expectedFile.readText())
    }

    if (System.getenv("GITHUB_ACTIONS") == "true") {
        actual shouldBe expected
    } else if (actual == expected) {
        actualFile.delete()
    } else {
        actualFile.writeText(actual)
        fail(
            """
            |Files differ in ${settingsBaseDirectory.canonicalPath}
            |Expected: ${expectedFile.relativeTo(settingsBaseDirectory)}
            |Actual:     ${actualFile.relativeTo(settingsBaseDirectory)} """.trimMargin()
        )
    }
}
