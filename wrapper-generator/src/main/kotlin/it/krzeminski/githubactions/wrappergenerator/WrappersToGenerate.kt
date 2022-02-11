package it.krzeminski.githubactions.wrappergenerator

import it.krzeminski.githubactions.wrappergenerator.domain.ActionCoords
import it.krzeminski.githubactions.wrappergenerator.generation.generateWrapper
import java.nio.file.Paths

fun main() {
    // To ensure there are no leftovers from previous generations.
    Paths.get("library/src/gen").toFile().deleteRecursively()

    listOf(
        ActionCoords("actions", "download-artifact", "v2"),

        ActionCoords("madhead", "check-gradle-version", "v1"),
        ActionCoords("madhead", "semver-utils", "latest"),
    ).forEach { actionCoords ->
        println("Generating ${actionCoords.owner}/${actionCoords.name}@${actionCoords.version}...")
        val (code, path) = actionCoords.generateWrapper()
        with(Paths.get(path).toFile()) {
            parentFile.mkdirs()
            writeText(code)
        }
    }
}
