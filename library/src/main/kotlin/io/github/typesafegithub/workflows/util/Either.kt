package io.github.typesafegithub.workflows.util

public fun <T> either(one: Pair<String, T?>, other: Pair<String, T?>): T? = run {
    if (one.second != null && other.second != null) {
        throw Error("You can only use either \"${one.first}\" or \"${other.first}\"")
    }
    one.second ?: other.second
}
