package io.github.typesafegithub.workflows.mavenbinding

import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.NewestForVersion
import io.github.typesafegithub.workflows.actionbindinggenerator.generation.ActionBinding
import io.github.typesafegithub.workflows.actionbindinggenerator.generation.generateBinding
import org.jetbrains.kotlin.cli.common.arguments.K2JVMCompilerArguments
import org.jetbrains.kotlin.cli.common.messages.MessageRenderer
import org.jetbrains.kotlin.cli.common.messages.PrintingMessageCollector
import org.jetbrains.kotlin.cli.jvm.K2JVMCompiler
import org.jetbrains.kotlin.config.Services
import java.io.OutputStream
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.createParentDirectories
import kotlin.io.path.createTempDirectory
import kotlin.io.path.div
import kotlin.io.path.writeText

fun OutputStream.buildJar(
    owner: String,
    name: String,
    version: String,
) {
    val binding = generateBinding(owner = owner, name = name, version = version)
    val pathWithJarContents = binding.compileBinding()
    return this.createZipFile(pathWithJarContents)
}

private fun generateBinding(
    owner: String,
    name: String,
    version: String,
): ActionBinding {
    val actionCoords =
        ActionCoords(
            owner = owner,
            name = name,
            version = version,
        )
    return actionCoords.generateBinding(
        metadataRevision = NewestForVersion,
    )
}

private fun ActionBinding.compileBinding(): Path {
    val compilationInput = createTempDirectory()
    val compilationOutput = createTempDirectory()

    val sourceFilePath = compilationInput / Path(filePath.substringAfter("kotlin/"))
    sourceFilePath.createParentDirectories()
    sourceFilePath.writeText(kotlinCode)

    val args =
        K2JVMCompilerArguments().apply {
            destination = compilationOutput.toString()
            classpath = System.getProperty("java.class.path")
            freeArgs = listOf(sourceFilePath.toString())
            noStdlib = true
            noReflect = true
            includeRuntime = false
        }
    val compilerMessageCollector =
        PrintingMessageCollector(
            System.out,
            MessageRenderer.GRADLE_STYLE,
            false,
        )
    K2JVMCompiler().exec(
        messageCollector = compilerMessageCollector,
        services = Services.EMPTY,
        arguments = args,
    )
    return compilationOutput
}
