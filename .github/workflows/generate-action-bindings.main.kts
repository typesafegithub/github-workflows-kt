#!/usr/bin/env kotlin
@file:Repository("https://s01.oss.sonatype.org/content/repositories/snapshots/")
@file:DependsOn("io.github.typesafegithub:action-binding-generator:1.3.2-20231021.093813-11")

import io.github.typesafegithub.workflows.actionbindinggenerator.ActionCoords
import io.github.typesafegithub.workflows.actionbindinggenerator.NewestForVersion
import io.github.typesafegithub.workflows.actionbindinggenerator.generateBinding
import kotlin.io.path.Path

val actions = listOf(
    ActionCoords(owner = "peter-evans", name = "create-issue-from-file", version = "v4"),
)

actions.forEach { action ->
    Path("generated").resolve(action.owner).resolve("${action.name}.kt").let {
        it.parent.toFile().mkdirs()
        val binding = action.generateBinding(
            metadataRevision = NewestForVersion,
            generateForScript = true,
        )
        it.toFile().writeText(binding.kotlinCode)
    }
}
