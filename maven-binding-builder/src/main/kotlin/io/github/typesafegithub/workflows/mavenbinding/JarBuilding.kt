@file:Suppress("ktlint:standard:filename")

package io.github.typesafegithub.workflows.mavenbinding

import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.NewestForVersion
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.TypingActualSource
import io.github.typesafegithub.workflows.actionbindinggenerator.generation.ActionBinding
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
    val mainJar: () -> ByteArray,
    val sourcesJar: () -> ByteArray,
    val typingActualSource: TypingActualSource?,
)

internal fun ActionCoords.buildJars(): Jars? {
    val binding =
        generateBinding(metadataRevision = NewestForVersion).also {
            if (it.isEmpty()) return null
        }

    val mainJar by lazy {
        val (sourceFilePaths, compilationInputDir) = binding.prepareDirectoryWithSources()
        val pathWithJarContents = compileBinding(sourceFilePaths = sourceFilePaths)
        val mainJarByteArrayOutputStream = ByteArrayOutputStream()
        mainJarByteArrayOutputStream.createZipFile(pathWithJarContents)
        pathWithJarContents.toFile().deleteRecursively()
        compilationInputDir.toFile().deleteRecursively()
        mainJarByteArrayOutputStream.toByteArray()
    }

    val sourcesJar by lazy {
        val (_, compilationInputDir) = binding.prepareDirectoryWithSources()
        val sourcesJarByteArrayOutputStream = ByteArrayOutputStream()
        sourcesJarByteArrayOutputStream.createZipFile(compilationInputDir)
        compilationInputDir.toFile().deleteRecursively()
        sourcesJarByteArrayOutputStream.toByteArray()
    }

    return Jars(
        mainJar = { mainJar },
        sourcesJar = { sourcesJar },
        typingActualSource = binding.firstNotNullOfOrNull { it.typingActualSource },
    )
}

private const val TARGET_KOTLIN_VERSION = "2.0"

private fun compileBinding(sourceFilePaths: List<Path>): Path {
    val compilationOutput = createTempDirectory(prefix = "gwkt-classes_")

    val args =
        K2JVMCompilerArguments().apply {
            destination = compilationOutput.toString()
            classpath = System.getProperty("java.class.path")
            freeArgs = sourceFilePaths.map { it.toString() }
            languageVersion = TARGET_KOTLIN_VERSION
            apiVersion = TARGET_KOTLIN_VERSION
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
    check(exitCode == ExitCode.OK) {
        "Binding compilation failed! Compiler messages:\n$compilerMessagesOutputStream"
    }
    return compilationOutput
}

private fun List<ActionBinding>.prepareDirectoryWithSources(): Pair<List<Path>, Path> {
    val directory = createTempDirectory(prefix = "gwkt-sources_")
    val sourceFilePaths =
        this
            .map { binding ->
                val sourceFilePath = directory / Path(binding.filePath)
                sourceFilePath.also {
                    it.createParentDirectories()
                    it.writeText(binding.kotlinCode)
                }
            }
    return Pair(sourceFilePaths, directory)
}
