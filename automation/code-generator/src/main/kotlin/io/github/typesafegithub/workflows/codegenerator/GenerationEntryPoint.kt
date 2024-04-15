package io.github.typesafegithub.workflows.codegenerator

import arrow.fx.coroutines.parMap
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.FromLockfile
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.prettyPrint
import io.github.typesafegithub.workflows.actionbindinggenerator.generation.ActionBinding
import io.github.typesafegithub.workflows.actionbindinggenerator.generation.generateBinding
import io.github.typesafegithub.workflows.codegenerator.model.ActionBindingRequest
import io.github.typesafegithub.workflows.dsl.expressions.generateEventPayloads
import java.nio.file.Paths

/***
 * Either run this main() function or run this command: ./gradlew :code-generator:run
 */
suspend fun main() {
    // To ensure there are no leftovers from previous generations.
    Paths.get("github-workflows-kt/src/gen").toFile().deleteRecursively()
    deleteListOfBindingsInDocs()
    generateEventPayloads()
    val requestsAndBindings = generateBindings()
    generateListOfBindingsForDocs(requestsAndBindings)
}

private suspend fun generateBindings(): List<Pair<ActionBindingRequest, ActionBinding>> {
    val requestsAndBindings =
        bindingsToGenerate.parMap(
            // Not too much to not trigger some rate limiting on the GitHub side.
            concurrency = 10,
        ) { actionBindingRequest ->
            println("Generating ${actionBindingRequest.actionCoords.prettyPrint}")
            val binding = actionBindingRequest.actionCoords.generateBinding(metadataRevision = FromLockfile)
            Pair(actionBindingRequest, binding ?: error("Couldn't generate binding for ${actionBindingRequest.actionCoords}"))
        }
    requestsAndBindings.forEach { (_, binding) ->
        with(Paths.get(binding.filePath).toFile()) {
            parentFile.mkdirs()
            writeText(binding.kotlinCode)
        }
    }
    return requestsAndBindings
}
