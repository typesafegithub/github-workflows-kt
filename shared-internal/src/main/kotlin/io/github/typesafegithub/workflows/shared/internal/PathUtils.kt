package io.github.typesafegithub.workflows.shared.internal

import java.nio.file.Path
import kotlin.io.path.absolute
import kotlin.io.path.exists
import kotlin.io.path.isDirectory

fun Path.findGitRoot(): Path {
    return generateSequence(this.absolute()) { it.parent }
        .firstOrNull { folder ->
            val dotGit = folder.resolve(".git")
            dotGit.exists() && dotGit.isDirectory()
        } ?: error("could not find a git root from ${this.absolute()}")
}
