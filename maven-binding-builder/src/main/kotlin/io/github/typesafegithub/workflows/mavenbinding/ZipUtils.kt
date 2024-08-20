package io.github.typesafegithub.workflows.mavenbinding

import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.io.OutputStream
import java.nio.file.Path
import java.nio.file.attribute.FileTime
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import kotlin.io.path.absolute
import kotlin.io.path.isDirectory
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.name

internal fun OutputStream.createZipFile(contents: Path) =
    ZipOutputStream(this).use { zipOutputStream ->
        contents
            .listDirectoryEntries()
            // Sorting to add ZIP entries in deterministic order, to provide exactly the same ZIP regardless of
            // generation time.
            .sortedBy { it.absolute().toString() }
            .forEach { file ->
                if (file.isDirectory()) {
                    zipDirectory(file.toFile(), file.name, zipOutputStream)
                } else {
                    zipFile(file.toFile(), zipOutputStream)
                }
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
        val zipEntry =
            ZipEntry(parentFolder + "/" + file.getName()).apply {
                // Resetting various timestamps to provide exactly the same ZIP regardless of generation time.
                time = 0
                creationTime = FileTime.fromMillis(0)
                lastModifiedTime = FileTime.fromMillis(0)
                lastAccessTime = FileTime.fromMillis(0)
            }
        zos.putNextEntry(zipEntry)
        BufferedInputStream(FileInputStream(file))
            .use { it.copyTo(zos) }
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
    val zipEntry =
        ZipEntry(file.getName()).apply {
            // Resetting various timestamps to provide exactly the same ZIP regardless of generation time.
            time = 0
            creationTime = FileTime.fromMillis(0)
            lastModifiedTime = FileTime.fromMillis(0)
            lastAccessTime = FileTime.fromMillis(0)
        }
    zos.putNextEntry(zipEntry)
    BufferedInputStream(FileInputStream(file))
        .use { it.copyTo(zos) }
    zos.closeEntry()
}
