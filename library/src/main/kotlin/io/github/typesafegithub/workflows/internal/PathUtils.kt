package io.github.typesafegithub.workflows.internal

import java.nio.file.Path
import kotlin.io.path.absolute
import kotlin.io.path.exists
import kotlin.io.path.isDirectory
import kotlin.io.path.relativeTo

internal fun Path.findGitRoot(): Path {
    return generateSequence(absolute()) { it.parent }
        .firstOrNull {
            it.resolve(".git")?.let { it.exists() && it.isDirectory() } ?: false
        } ?: error("could not find a git root from ${this.absolute()}")
}

internal fun Path.relativeToAbsolute(base: Path): Path =
    absolute().relativeTo(base.absolute())
