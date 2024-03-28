package io.github.typesafegithub.workflows.mavenbinding

import com.intellij.util.io.PagedFileStorage
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.io.OutputStream
import java.nio.file.Path
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import kotlin.io.path.isDirectory
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.name

internal fun OutputStream.createZipFile(contents: Path) =
    ZipOutputStream(this).use { zipOutputStream ->
        contents.listDirectoryEntries().forEach { file ->
            if (file.isDirectory()) {
                zipDirectory(file.toFile(), file.name, zipOutputStream)
            } else {
                zipFile(file.toFile(), zipOutputStream)
            }
        }
        zipOutputStream.flush()
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
        val bytesIn = ByteArray(PagedFileStorage.BUFFER_SIZE)
        var read: Int
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
    val bytesIn = ByteArray(PagedFileStorage.BUFFER_SIZE)
    var read: Int
    while ((bis.read(bytesIn).also { read = it }) != -1) {
        zos.write(bytesIn, 0, read)
        bytesRead += read.toLong()
    }
    zos.closeEntry()
}
