package it.krzeminski.githubactions.expr

/**
 * The job context contains information about the currently running job.
 * https://docs.github.com/en/actions/learn-github-actions/contexts#job-context
 */
object JobContext : ExprContext("job") {
    val status by map
    val container = Container
    object Container : ExprContext("job.container") {
        val id by map
        val network by map
    }
    val services: Map<String, JobContextService> =
        MapFromLambda { key -> JobContextService("$_path.services.$key") }
}

class JobContextService(path: String) : ExprContext(path) {
    val id by map
    val network by map
    val ports by map
}
