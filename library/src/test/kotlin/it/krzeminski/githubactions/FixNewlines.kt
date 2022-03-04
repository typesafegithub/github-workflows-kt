package it.krzeminski.githubactions

fun String.fixNewlines(): String {
    return if (System.lineSeparator() != "\n") {
        replace(System.lineSeparator(), "\n")
    } else {
        this
    }
}
