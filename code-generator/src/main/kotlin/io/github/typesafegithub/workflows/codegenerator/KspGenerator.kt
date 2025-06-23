package io.github.typesafegithub.workflows.codegenerator

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import io.github.typesafegithub.workflows.dsl.expressions.generateEventPayloads
import io.github.typesafegithub.workflows.generateVersionInfo

class KspGenerator(
    private val codeGenerator: CodeGenerator,
    private val libraryVersion: String,
) : SymbolProcessor {
    private var wasRun = false

    override fun process(resolver: Resolver): List<KSAnnotated> {
        if (!wasRun) {
            generateEventPayloads(codeGenerator = codeGenerator)
            generateVersionInfo(codeGenerator = codeGenerator, libraryVersion = libraryVersion)
            wasRun = true
        }
        return emptyList()
    }
}
