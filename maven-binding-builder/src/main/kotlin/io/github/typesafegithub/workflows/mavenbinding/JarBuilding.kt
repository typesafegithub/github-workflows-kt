@file:Suppress("ktlint:standard:filename")

package io.github.typesafegithub.workflows.mavenbinding

import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.NewestForVersion
import io.github.typesafegithub.workflows.actionbindinggenerator.generation.ActionBinding
import io.github.typesafegithub.workflows.actionbindinggenerator.generation.ClientType
import io.github.typesafegithub.workflows.actionbindinggenerator.generation.generateBinding
import org.jetbrains.kotlin.cli.common.ExitCode
import org.jetbrains.kotlin.cli.common.arguments.K2JVMCompilerArguments
import org.jetbrains.kotlin.cli.common.messages.MessageRenderer
import org.jetbrains.kotlin.cli.common.messages.PrintingMessageCollector
import org.jetbrains.kotlin.cli.jvm.K2JVMCompiler
import org.jetbrains.kotlin.config.Services
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.createParentDirectories
import kotlin.io.path.createTempDirectory
import kotlin.io.path.div
import kotlin.io.path.writeText

internal data class Jars(
    val mainJar: ByteArray,
    val sourcesJar: ByteArray,
)

internal fun buildJars(
    owner: String,
    name: String,
    version: String,
): Jars? {
    val binding = generateBinding(owner = owner, name = name, version = version) ?: return null
    val (sourceFilePath, compilationInputDir) = binding.prepareDirectoryWithSources()

    val pathWithJarContents = compileBinding(sourceFilePath = sourceFilePath)
    val mainJarByteArrayOutputStream = ByteArrayOutputStream()
    mainJarByteArrayOutputStream.createZipFile(pathWithJarContents)

    val sourcesJarByteArrayOutputStream = ByteArrayOutputStream()
    sourcesJarByteArrayOutputStream.createZipFile(compilationInputDir)

    return Jars(
        mainJar = mainJarByteArrayOutputStream.toByteArray(),
        sourcesJar = sourcesJarByteArrayOutputStream.toByteArray(),
    )
}

private fun generateBinding(
    owner: String,
    name: String,
    version: String,
): ActionBinding? {
    val actionCoords =
        ActionCoords(
            owner = owner,
            name = name,
            version = version,
        )
    return actionCoords.generateBinding(
        metadataRevision = NewestForVersion,
        clientType = ClientType.VERSIONED_JAR,
    )
}

private fun compileBinding(sourceFilePath: Path): Path {
    val compilationOutput = createTempDirectory()

    val args =
        K2JVMCompilerArguments().apply {
            destination = compilationOutput.toString()
            classpath = System.getProperty("java.class.path")
            freeArgs = listOf(sourceFilePath.toString())
            noStdlib = true
            noReflect = true
            includeRuntime = false
        }
    val compilerMessagesOutputStream = ByteArrayOutputStream()
    val compilerMessageCollector =
        PrintingMessageCollector(
            PrintStream(compilerMessagesOutputStream),
            MessageRenderer.GRADLE_STYLE,
            false,
        )
    val exitCode =
        K2JVMCompiler().exec(
            messageCollector = compilerMessageCollector,
            services = Services.EMPTY,
            arguments = args,
        )
    require(exitCode == ExitCode.OK) {
        "Binding compilation failed! Compiler messages: $compilerMessagesOutputStream"
    }
    return compilationOutput
}

private fun ActionBinding.prepareDirectoryWithSources(): Pair<Path, Path> {
    val directory = createTempDirectory()
    val sourceFilePath = directory / Path(this.filePath.substringAfter("kotlin/"))
    sourceFilePath.also {
        it.createParentDirectories()
        it.writeText(this.kotlinCode)
    }
    return Pair(sourceFilePath, directory)
}
