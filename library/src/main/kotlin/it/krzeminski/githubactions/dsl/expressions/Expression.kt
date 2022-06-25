package it.krzeminski.githubactions.dsl.expressions

/**
 * Creates an expression, i.e. something evaluated by GitHub.
 *
 * @see https://docs.github.com/en/actions/learn-github-actions/expressions#about-expressions
 */
fun expr(value: String) = "\${{ $value }}"
