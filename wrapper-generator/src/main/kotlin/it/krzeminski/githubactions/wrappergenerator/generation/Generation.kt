package it.krzeminski.githubactions.wrappergenerator.generation

import it.krzeminski.githubactions.wrappergenerator.domain.ActionCoords
import it.krzeminski.githubactions.wrappergenerator.metadata.fetchMetadata

fun ActionCoords.generateWrapper(): String {
    val metadata = fetchMetadata()
    println(metadata)
    return ""
}
