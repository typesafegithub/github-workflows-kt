package it.krzeminski.githubactions.internal

import java.nio.file.Path
import kotlin.io.path.*

internal fun Path.findGitRoot(): Path {
    return generateSequence(absolute()) { it.parent }
        .firstOrNull {
            it.resolve(".git")?.let { it.exists() && it.isDirectory() } ?: false
        } ?: error("could not find a git root from ${this.absolute()}")
}

internal fun Path.relativeToAbsolute(base: Path): String =
    absolute().relativeTo(base.absolute()).invariantSeparatorsPathString