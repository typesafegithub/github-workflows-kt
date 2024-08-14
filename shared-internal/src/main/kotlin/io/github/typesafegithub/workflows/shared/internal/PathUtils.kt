package io.github.typesafegithub.workflows.shared.internal

import java.nio.file.Path
import kotlin.io.path.absolute
import kotlin.io.path.exists
import kotlin.io.path.isDirectory

fun Path.findGitRoot(): Path =
    generateSequence(this.absolute()) { it.parent }
        .firstOrNull {
            it.resolve(".git")?.let { it.exists() && it.isDirectory() } ?: false
        } ?: error("could not find a git root from ${this.absolute()}")
