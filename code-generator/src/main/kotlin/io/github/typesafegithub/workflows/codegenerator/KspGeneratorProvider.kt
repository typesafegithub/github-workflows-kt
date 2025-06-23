package io.github.typesafegithub.workflows.codegenerator

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

class KspGeneratorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor =
        KspGenerator(
            codeGenerator = environment.codeGenerator,
            libraryVersion = environment.options["library-version"] ?: error("No library version specified"),
        )
}
