package it.krzeminski.githubactions.dsl.expressions.contexts

import it.krzeminski.githubactions.dsl.expressions.ExpressionContext

/**
 * The runner context contains information about the runner that is executing the current job.
 * https://docs.github.com/en/actions/learn-github-actions/contexts#example-contents-of-the-runner-context
 */
object RunnerContext : ExpressionContext("runner") {
    val name by propertyToExprPath
    val os by propertyToExprPath
    val arch by propertyToExprPath
    val temp by propertyToExprPath
    val tool_cache by propertyToExprPath
    val workspace by propertyToExprPath
}
