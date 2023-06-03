package io.github.typesafegithub.workflows.codegenerator.generation

import io.kotest.assertions.fail
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNot
import io.kotest.matchers.string.contain
import java.nio.file.Paths

fun Wrapper.shouldMatchFile(path: String) {
    val expectedFile =
        Paths.get("src/test/kotlin/io/github/typesafegithub/workflows/codegenerator/generation/wrappersfromunittests/$path")
            .toFile()
    val actualFile = expectedFile.resolveSibling(expectedFile.nameWithoutExtension + "Actual.kt")
    val expectedContent = when {
        expectedFile.canRead() -> expectedFile.readText().removeWindowsNewLines()
        else -> ""
    }
    val actualContent = kotlinCode.removeWindowsNewLines()

    filePath shouldBe "library/src/gen/kotlin/io/github/typesafegithub/workflows/actions/johnsmith/$path"

    val packageName = "io.github.typesafegithub.workflows.actions.johnsmith"
    expectedContent shouldNot contain("package $packageName.actual")

    if (System.getenv("GITHUB_ACTIONS") == "true") {
        actualContent shouldBe expectedContent
    } else if (actualContent == expectedContent) {
        actualFile.delete()
    } else {
        actualFile.writeText(
            // change the package to avoid compilation errors because of duplicate classes / functions
            actualContent.replace("package $packageName", "package $packageName.actual"),
        )
        fail("The Wrapper's kotlin code in ${actualFile.name} doesn't match ${expectedFile.name}\nSee folder ${expectedFile.parentFile.canonicalPath}")
    }
}

private fun String.removeWindowsNewLines(): String =
    replace("\r\n", "\n")
