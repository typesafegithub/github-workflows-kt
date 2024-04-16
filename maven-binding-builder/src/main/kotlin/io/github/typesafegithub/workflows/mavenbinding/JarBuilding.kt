package io.github.typesafegithub.workflows.mavenbinding

import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.NewestForVersion
import io.github.typesafegithub.workflows.actionbindinggenerator.generation.ActionBinding
import io.github.typesafegithub.workflows.actionbindinggenerator.generation.ClientType
import io.github.typesafegithub.workflows.actionbindinggenerator.generation.generateBinding
import io.ktor.client.HttpClient
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

internal fun buildJar(
    owner: String,
    name: String,
    version: String,
    httpClient: HttpClient,
): ByteArray? {
    val binding = generateBinding(owner = owner, name = name, version = version, httpClient) ?: return null
    val pathWithJarContents = binding.compileBinding()
    val byteArrayOutputStream = ByteArrayOutputStream()
    byteArrayOutputStream.createZipFile(pathWithJarContents)
    return byteArrayOutputStream.toByteArray()
}

private fun generateBinding(
    owner: String,
    name: String,
    version: String,
    httpClient: HttpClient,
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
        httpClient = httpClient,
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
