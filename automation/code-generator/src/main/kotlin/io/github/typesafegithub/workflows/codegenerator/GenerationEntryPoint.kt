package io.github.typesafegithub.workflows.codegenerator

import io.github.typesafegithub.workflows.actionbindinggenerator.ActionBinding
import io.github.typesafegithub.workflows.actionbindinggenerator.FromLockfile
import io.github.typesafegithub.workflows.actionbindinggenerator.generateBinding
import io.github.typesafegithub.workflows.actionbindinggenerator.prettyPrint
import io.github.typesafegithub.workflows.actionsmetadata.bindingsToGenerate
import io.github.typesafegithub.workflows.actionsmetadata.model.ActionBindingRequest
import io.github.typesafegithub.workflows.dsl.expressions.generateEventPayloads
import java.nio.file.Paths

/***
 * Either run this main() function or run this command: ./gradlew :code-generator:run
 */
fun main() {
    // To ensure there are no leftovers from previous generations.
    Paths.get("library/src/gen").toFile().deleteRecursively()
    deleteListOfBindingsInDocs()
    generateEventPayloads()
    val requestsAndBindings = generateBindings()
    generateListOfBindingsForDocs(requestsAndBindings)
}

private fun generateBindings(): List<Pair<ActionBindingRequest, ActionBinding>> {
    val requestsAndBindings =
        bindingsToGenerate.map { actionBindingRequest ->
            println("Generating ${actionBindingRequest.actionCoords.prettyPrint}")
            val binding = actionBindingRequest.actionCoords.generateBinding(metadataRevision = FromLockfile)
            Pair(actionBindingRequest, binding)
        }
    requestsAndBindings.forEach { (_, binding) ->
        with(Paths.get(binding.filePath).toFile()) {
            parentFile.mkdirs()
            writeText(binding.kotlinCode)
        }
    }
    return requestsAndBindings
}
