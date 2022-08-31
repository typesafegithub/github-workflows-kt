package it.krzeminski.githubactions.dsl.expressions

import it.krzeminski.githubactions.dsl.expressions.contexts.*

/**
 * Root elements of GitHub expressions.
 *
 * https://docs.github.com/en/actions/learn-github-actions/expressions#about-expressions
 * https://docs.github.com/en/actions/learn-github-actions/contexts
 */
object Contexts : FunctionsContext() {
    val env = EnvContext
    val github = GitHubContext
    val runner = RunnerContext
    val secrets = SecretsContext
}
