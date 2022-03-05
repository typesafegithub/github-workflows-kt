package it.krzeminski.githubactions

fun String.unixNewlines(): String {
    return if (System.lineSeparator() != "\n") {
        replace(System.lineSeparator(), "\n")
    } else {
        this
    }
}
