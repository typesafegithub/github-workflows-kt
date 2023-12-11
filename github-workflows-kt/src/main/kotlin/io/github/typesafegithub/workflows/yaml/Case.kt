package io.github.typesafegithub.workflows.yaml

import io.github.typesafegithub.workflows.internal.InternalGithubActionsApi

@InternalGithubActionsApi
public inline fun <reified T : Enum<T>> T.toSnakeCase(): String = snakeCaseOf(name)

@InternalGithubActionsApi
public fun snakeCaseOf(name: String): String {
    require(name.first() !in 'a'..'z' && name.all { it in 'a'..'z' || it in 'A'..'Z' })
    return pascalCaseRegex.findAll(name).joinToString(separator = "_") { it.value.lowercase() }
}

internal val pascalCaseRegex = Regex("[A-Z][a-z]*")
