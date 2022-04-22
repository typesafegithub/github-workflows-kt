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

    val token by map

    val job by map

    val ref by map

    val sha by map

    val repository by map

    val repository_owner by map

    val repositoryUrl by map

    val run_id by map

    val run_number by map

    val retention_days by map

    val run_attempt by map

    val actor by map

    val workflow by map

    val head_ref by map

    val base_ref by map

    val event_name by map

    val server_url by map

    val api_url by map

    val graphql_url by map

    val ref_name by map

    val ref_protected by map

    val ref_type by map

    val secret_source by map

    val workspace by map

    val action by map

    val event_path by map

    val action_repository by map

    val action_ref by map

    val path by map

    val env by map
}
