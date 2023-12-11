package io.github.typesafegithub.workflows.internal

import java.nio.file.Path
import kotlin.io.path.absolute
import kotlin.io.path.relativeTo

internal fun Path.relativeToAbsolute(base: Path): Path = absolute().relativeTo(base.absolute())
