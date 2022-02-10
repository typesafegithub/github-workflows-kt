package it.krzeminski.githubactions.wrappergenerator.generation

import java.nio.file.Paths

fun writeToUnitTests(wrapper: Wrapper) {
    Paths.get("src/test/kotlin/it/krzeminski/githubactions/wrappergenerator/generation/wrappersfromunittests/${Paths.get(wrapper.filePath).fileName}")
        .toFile().writeText(wrapper.kotlinCode)
}
