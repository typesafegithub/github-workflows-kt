package io.github.typesafegithub.workflows.domain

import io.github.typesafegithub.workflows.dsl.expressions.expr

public data class Expression<in T>(
    public val expression: String,
) {
    public val expressionString: String = expr(expression)
}
