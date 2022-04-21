package it.krzeminski.githubactions.expr

/**
 * matrix context
 * For workflows with a build matrix, the matrix context contains the matrix properties
 * defined in the workflow file that apply to the current job.
 * https://docs.github.com/en/actions/using-jobs/using-a-build-matrix-for-your-jobs
 * https://docs.github.com/en/actions/learn-github-actions/contexts#matrix-context
 */
object MatrixContext : ExprContext("matrix") {
    val os by map
}
