package it.krzeminski.githubactions

import java.io.ByteArrayOutputStream
import java.io.PrintStream

fun <R> withCapturedOutput(block: () -> R): Triple<R, String, String> {
    val outBuffer = ByteArrayOutputStream()
    val errBuffer = ByteArrayOutputStream()
    val originalStdout = System.out
    val originalStderr = System.err

    System.setOut(PrintStream(outBuffer))
    System.setErr(PrintStream(errBuffer))

    val result = block()

    System.setOut(originalStdout)
    System.setOut(originalStderr)
    return Triple(
        result,
        outBuffer.toString().fixNewlines(),
        errBuffer.toString().fixNewlines(),
    )
}

fun String.fixNewlines(): String {
    return if (System.lineSeparator() != "\n") {
        replace(System.lineSeparator(), "\n")
    } else {
        this
    }
}
