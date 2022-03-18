package it.krzeminski.githubactions.wrappergenerator.generation

import io.kotest.assertions.fail
import io.kotest.matchers.shouldBe
import java.nio.file.Paths

fun Wrapper.shouldMatchFile(path: String) {
    val expectedFile =
        Paths.get("src/test/kotlin/it/krzeminski/githubactions/wrappergenerator/generation/wrappersfromunittests/$path")
            .toFile()
    val actualFile = expectedFile.resolveSibling(expectedFile.nameWithoutExtension + "Actual.kt")
    val expectedContent = when {
        expectedFile.canRead() -> expectedFile.readText().removeWindowsNewLines()
        else -> ""
    }
    val actualContent = kotlinCode.removeWindowsNewLines()

    filePath shouldBe "library/src/gen/kotlin/it/krzeminski/githubactions/actions/johnsmith/$path"

    if (System.getenv("GITHUB_ACTIONS") == "true") {
        actualContent shouldBe expectedContent
    } else if (actualContent == expectedContent) {
        actualFile.delete()
    } else {
        actualFile.writeText(
            actualContent.replace(
                "package it.krzeminski.githubactions.actions.johnsmith",
                "package it.krzeminski.githubactions.actions.actual"
            )
        )
        fail("The Wrapper's kotlin code in ${actualFile.name} doesn't match ${expectedFile.name}\nSee folder ${expectedFile.parentFile.canonicalPath}")
    }
}

private fun String.removeWindowsNewLines(): String =
    replace("\r\n", "\n")
