#!/usr/bin/env kotlin
@file:Repository("https://s01.oss.sonatype.org/content/repositories/snapshots/")
@file:DependsOn("io.github.typesafegithub:action-binding-generator:1.4.1-20231028.112251-9")

import io.github.typesafegithub.workflows.actionbindinggenerator.NewestForVersion
import io.github.typesafegithub.workflows.actionbindinggenerator.extractUsedActionsFromWorkflow
import io.github.typesafegithub.workflows.actionbindinggenerator.generateBinding
import java.io.File
import kotlin.io.path.Path

val workflowYamlFileName: String? = when {
    args.isEmpty() -> null
    args.size == 1 -> args[0]
    else -> error("At most one argument is supported!")
}

val allUsedActions = extractUsedActionsFromWorkflow(
    manifest = File(".github/workflows/_used-actions.yaml").readText(),
)

val actionsToGenerateBindingsFor = workflowYamlFileName?.let {
    val actionsUsedInRequestedWorkflow = extractUsedActionsFromWorkflow(
        manifest = File(".github/workflows/$workflowYamlFileName").readText(),
    )
    allUsedActions.intersect(actionsUsedInRequestedWorkflow.toSet())
} ?: allUsedActions

actionsToGenerateBindingsFor.forEach { action ->
    Path(".github").resolve("workflows").resolve("generated").resolve(action.owner).resolve("${action.name}.kt").let {
        it.parent.toFile().mkdirs()
        val binding = action.generateBinding(
            metadataRevision = NewestForVersion,
            generateForScript = true,
        )
        it.toFile().writeText(binding.kotlinCode)
    }
}
