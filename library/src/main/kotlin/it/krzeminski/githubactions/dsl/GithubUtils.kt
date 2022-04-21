package it.krzeminski.githubactions.dsl

import it.krzeminski.githubactions.expr.Expr

/**
 * Creates an expression, i.e. something evaluated by GitHub.
 *
 * https://docs.github.com/en/actions/learn-github-actions/expressions#about-expressions
 */
fun expr(value: String) = "\${{ $value }}"

/**
 * Type-safe expressions, i.e. something evaluated by GitHub.
 *
 * https://docs.github.com/en/actions/learn-github-actions/expressions#about-expressions
 * https://docs.github.com/en/actions/learn-github-actions/contexts
 */
fun expr(expression: Expr.() -> String): String =
    with(Expr) { expr(expression()) }
