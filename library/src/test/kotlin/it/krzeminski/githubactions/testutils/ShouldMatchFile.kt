package it.krzeminski.githubactions.testutils

import io.kotest.assertions.fail
import io.kotest.matchers.shouldBe
import java.io.File

private val baseDirectory = File("src/test/resources")

infix fun String.shouldMatchFile(path: String) {
    val expectedFile = baseDirectory.resolve("expected/$path")
    val actualFile = baseDirectory.resolve("actual/$path")

    val actual = this
    val expected = when {
        expectedFile.canRead().not() -> ""
        else -> expectedFile.readText().replace("\r", "")
    }

    if (System.getenv("GITHUB_ACTIONS") == "true") {
        actual shouldBe expected
    } else if (actual == expected) {
        actualFile.delete()
    } else {
        actualFile.writeText(actual)
        fail(
            """
            |Files differ in ${baseDirectory.canonicalPath}
            |Expected: ${expectedFile.relativeTo(baseDirectory)}
            |Actual:     ${actualFile.relativeTo(baseDirectory)} """.trimMargin()
        )
    }
}
