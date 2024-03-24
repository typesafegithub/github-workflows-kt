package io.github.typesafegithub.workflows.mavenbinding

import com.intellij.util.io.PagedFileStorage.BUFFER_SIZE
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.NewestForVersion
import io.github.typesafegithub.workflows.actionbindinggenerator.generation.ActionBinding
import io.github.typesafegithub.workflows.actionbindinggenerator.generation.generateBinding
import org.jetbrains.kotlin.cli.common.arguments.K2JVMCompilerArguments
import org.jetbrains.kotlin.cli.common.messages.MessageRenderer
import org.jetbrains.kotlin.cli.common.messages.PrintingMessageCollector
import org.jetbrains.kotlin.cli.jvm.K2JVMCompiler
import org.jetbrains.kotlin.config.Services
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.nio.file.Path
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import kotlin.io.path.Path
import kotlin.io.path.createParentDirectories
import kotlin.io.path.createTempDirectory
import kotlin.io.path.div
import kotlin.io.path.isDirectory
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.name
import kotlin.io.path.writeText
import kotlin.time.measureTimedValue

fun buildMavenBinding(binding: ActionBinding): Path {
    val compilationInput = createTempDirectory()
    val compilationOutput = createTempDirectory()
    println("Output: $compilationOutput")

    val sourceFilePath = compilationInput / Path(binding.filePath.substringAfter("kotlin/"))
    sourceFilePath.createParentDirectories()
    sourceFilePath.writeText(binding.kotlinCode)

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
    val exitCode =
        K2JVMCompiler().exec(
            messageCollector = compilerMessageCollector,
            services = Services.EMPTY,
            arguments = args,
        )
    return compilationOutput
}

fun generateBinding(): ActionBinding {
    val actionCoords =
        ActionCoords(
            owner = "Vampire",
            name = "setup-wsl",
            version = "v3",
        )
    return actionCoords.generateBinding(
        metadataRevision = NewestForVersion,
    )
}

fun createJarFile(contents: Path): ByteArrayOutputStream =
    ByteArrayOutputStream().also { byteArrayOutputStream ->
        // TODO use https://github.com/srikanth-lingala/zip4j to avoid boilerplate
        ZipOutputStream(byteArrayOutputStream).use { zipOutputStream ->
            contents.listDirectoryEntries().forEach { file ->
                if (file.isDirectory()) {
                    zipDirectory(file.toFile(), file.name, zipOutputStream)
                } else {
                    zipFile(file.toFile(), zipOutputStream)
                }
            }
            zipOutputStream.flush()
        }
    }

/**
 * Adds a directory to the current zip output stream
 * @param folder the directory to be  added
 * @param parentFolder the path of parent directory
 * @param zos the current zip output stream
 * @throws FileNotFoundException
 * @throws IOException
 */
@Throws(FileNotFoundException::class, IOException::class)
private fun zipDirectory(
    folder: File,
    parentFolder: String,
    zos: ZipOutputStream,
) {
    for (file in folder.listFiles()) {
        if (file.isDirectory()) {
            zipDirectory(file, parentFolder + "/" + file.getName(), zos)
            continue
        }
        zos.putNextEntry(ZipEntry(parentFolder + "/" + file.getName()))
        val bis =
            BufferedInputStream(
                FileInputStream(file),
            )
        var bytesRead: Long = 0
        val bytesIn = ByteArray(BUFFER_SIZE)
        var read = 0
        while ((bis.read(bytesIn).also { read = it }) != -1) {
            zos.write(bytesIn, 0, read)
            bytesRead += read.toLong()
        }
        zos.closeEntry()
    }
}

/**
 * Adds a file to the current zip output stream
 * @param file the file to be added
 * @param zos the current zip output stream
 * @throws FileNotFoundException
 * @throws IOException
 */
@Throws(FileNotFoundException::class, IOException::class)
private fun zipFile(
    file: File,
    zos: ZipOutputStream,
) {
    zos.putNextEntry(ZipEntry(file.getName()))
    val bis =
        BufferedInputStream(
            FileInputStream(
                file,
            ),
        )
    var bytesRead: Long = 0
    val bytesIn = ByteArray(BUFFER_SIZE)
    var read = 0
    while ((bis.read(bytesIn).also { read = it }) != -1) {
        zos.write(bytesIn, 0, read)
        bytesRead += read.toLong()
    }
    zos.closeEntry()
}

fun main() {
    val (binding, bindingGenerationDuration) =
        measureTimedValue {
            generateBinding()
        }
    println("Generating binding took $bindingGenerationDuration")
    val (pathWithJarContents, compilationDuration) =
        measureTimedValue {
            buildMavenBinding(binding)
        }
    println("Compilation took $compilationDuration")
    val (outputStream, jarCreationDuration) =
        measureTimedValue {
            createJarFile(contents = pathWithJarContents)
        }
    println("Packing into JAR took $jarCreationDuration")
    println("Writing to file")
    FileOutputStream("/Users/piotr/checkout.jar").use {
        outputStream.writeTo(it)
    }
}
