package it.krzeminski.githubactions.expr

/**
 * The runner context contains information about the runner that is executing the current job.
 * https://docs.github.com/en/actions/learn-github-actions/contexts#example-contents-of-the-runner-context
 */
object RunnerContext : ExprContext("runner") {
    val name by map
    val os by map
    val arch by map
    val temp by map
    val tool_cache by map
    val workspace by map
}
