package it.krzeminski.githubactions.dsl.expressions

/**
 * Creates an expression, i.e. something evaluated by GitHub, from a string.
 *
 * https://docs.github.com/en/actions/learn-github-actions/expressions#about-expressions
 */
public fun expr(value: String): String = "\${{ ${value.removePrefix("$")} }}"

/**
 * Creates an expression, i.e. something evaluated by GitHub, using type-safe API.
 *
 * https://docs.github.com/en/actions/learn-github-actions/expressions#about-expressions
 * https://docs.github.com/en/actions/learn-github-actions/contexts
 */
public fun expr(expression: Contexts.() -> String): String =
    with(Contexts) { expr(expression()) }
