package it.krzeminski.githubactions.wrappergenerator

import it.krzeminski.githubactions.wrappergenerator.generation.generateWrapper
import java.nio.file.Paths

fun main() {
    // To ensure there are no leftovers from previous generations.
    Paths.get("library/src/gen").toFile().deleteRecursively()

    wrappersToGenerate.forEach { request ->
        val actionCoords = request.coords
        println("Generating ${actionCoords.owner}/${actionCoords.name}@${actionCoords.version}...")
        val (code, path) = request.generateWrapper()
        with(Paths.get(path).toFile()) {
            parentFile.mkdirs()
            writeText(code)
        }
    }
}
