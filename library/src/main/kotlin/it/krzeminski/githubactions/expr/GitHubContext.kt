package it.krzeminski.githubactions.expr

/**
 * The github context contains information about the workflow run and the event that triggered the run.
 * You can also read most of the github context data in environment variables.
 * For more information about environment variables,
 * https://docs.github.com/en/actions/automating-your-workflow-with-github-actions/using-environment-variables
 *
 * See https://docs.github.com/en/actions/learn-github-actions/contexts#github-context
 * **/
object GitHubContext : ExprContext("github") {
    val sha by map

    // TODO: add other fields

    object Event : ExprContext("$path.event") {
        val action by map

        // TODO: add other fields
    }
}
