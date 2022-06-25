package it.krzeminski.githubactions.dsl.expressions

/**
 * Creates an expression, i.e. something evaluated by GitHub, from a string.
 *
 * https://docs.github.com/en/actions/learn-github-actions/expressions#about-expressions
 */
fun expr(value: String) = "\${{ $value }}"

/**
 * Creates an expression, i.e. something evaluated by GitHub, using type-safe API.
 *
 * https://docs.github.com/en/actions/learn-github-actions/expressions#about-expressions
 * https://docs.github.com/en/actions/learn-github-actions/contexts
 */
fun expr(expression: Contexts.() -> String): String =
    with(Contexts) { expr(expression()) }
