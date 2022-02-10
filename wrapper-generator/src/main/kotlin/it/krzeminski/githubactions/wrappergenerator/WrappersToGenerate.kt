package it.krzeminski.githubactions.wrappergenerator

import it.krzeminski.githubactions.wrappergenerator.domain.ActionCoords
import it.krzeminski.githubactions.wrappergenerator.generation.generateWrapper
import java.nio.file.Paths

fun main() {
    listOf(
        ActionCoords("actions", "download-artifact", "v2"),
    ).forEach { actionCoords ->
        println("Generating ${actionCoords.owner}/${actionCoords.name}@${actionCoords.version}...")
        val (code, path) = actionCoords.generateWrapper()
        Paths.get(path).toFile().writeText(code)
    }
}
