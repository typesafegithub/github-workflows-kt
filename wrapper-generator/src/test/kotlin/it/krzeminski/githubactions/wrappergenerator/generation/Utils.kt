package it.krzeminski.githubactions.wrappergenerator.generation

import io.kotest.assertions.fail
import io.kotest.matchers.shouldBe
import java.nio.file.Paths

fun Wrapper.shouldMatchFile(path: String) {
    val expectedFile =
        Paths.get("src/test/kotlin/it/krzeminski/githubactions/wrappergenerator/generation/wrappersfromunittests/$path")
            .toFile()
    val actualFile = expectedFile.resolveSibling(expectedFile.nameWithoutExtension + "Actual.kt")
    val expected = if (expectedFile.canRead()) expectedFile.readText() else ""

    filePath shouldBe "library/src/gen/kotlin/it/krzeminski/githubactions/actions/johnsmith/$path"

    if (System.getenv("GITHUB_ACTIONS") == "true") {
        kotlinCode shouldBe expected
    } else if (kotlinCode == expected) {
        actualFile.delete()
    } else {
        actualFile.writeText(
            kotlinCode.replace(
                "package it.krzeminski.githubactions.actions.johnsmith",
                "package it.krzeminski.githubactions.actions.actual"
            )
        )
        fail("The Wrapper's kotlin code in ${actualFile.name} doesn't match ${expectedFile.name}\nSee folder ${expectedFile.parentFile.canonicalPath}")
    }
}
