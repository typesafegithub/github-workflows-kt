package it.krzeminski.githubactions.dsl.expressions.contexts

import it.krzeminski.githubactions.dsl.expressions.ExpressionContext

/**
 * The runner context contains information about the runner that is executing the current job.
 * https://docs.github.com/en/actions/learn-github-actions/contexts#example-contents-of-the-runner-context
 */
object RunnerContext : ExpressionContext("runner") {
    val name by map
    val os by map
    val arch by map
    val temp by map
    val tool_cache by map
    val workspace by map
}
