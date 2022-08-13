package it.krzeminski.githubactions.yaml

import it.krzeminski.githubactions.internal.InternalGithubActionsApi

@InternalGithubActionsApi
inline fun <reified T : Enum<T>> T.toSnakeCase(): String =
    snakeCaseOf(name)

@InternalGithubActionsApi
fun snakeCaseOf(name: String): String {
    require(name.first() !in 'a'..'z' && name.all { it in 'a'..'z' || it in 'A'..'Z' })
    return pascalCaseRegex.findAll(name).joinToString(separator = "_") { it.value.lowercase() }
}

val pascalCaseRegex = Regex("[A-Z][a-z]*")
