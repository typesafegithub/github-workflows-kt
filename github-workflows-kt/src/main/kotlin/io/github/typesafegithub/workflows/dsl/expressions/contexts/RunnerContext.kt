package io.github.typesafegithub.workflows.dsl.expressions.contexts

import io.github.typesafegithub.workflows.dsl.expressions.ExpressionContext

/**
 * The runner context contains information about the runner that is executing the current job.
 * https://docs.github.com/en/actions/learn-github-actions/contexts#example-contents-of-the-runner-context
 */
public object RunnerContext : ExpressionContext("runner") {
    public val name: String by propertyToExprPath
    public val os: String by propertyToExprPath
    public val arch: String by propertyToExprPath
    public val temp: String by propertyToExprPath
    public val tool_cache: String by propertyToExprPath
    public val workspace: String by propertyToExprPath
}
