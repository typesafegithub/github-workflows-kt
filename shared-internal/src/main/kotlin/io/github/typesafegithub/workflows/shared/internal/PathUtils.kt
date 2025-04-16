package io.github.typesafegithub.workflows.shared.internal

import java.nio.file.Path
import kotlin.io.path.absolute
import kotlin.io.path.exists

fun Path.findGitRoot(): Path =
    generateSequence(this.absolute()) { it.parent }
        .firstOrNull { it.resolve(".git").exists() }
        ?: error("could not find a git root from ${this.absolute()}")
