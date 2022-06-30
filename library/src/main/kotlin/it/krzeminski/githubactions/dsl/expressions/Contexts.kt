package it.krzeminski.githubactions.dsl.expressions

import it.krzeminski.githubactions.dsl.expressions.contexts.EnvContext
import it.krzeminski.githubactions.dsl.expressions.contexts.FunctionsContext
import it.krzeminski.githubactions.dsl.expressions.contexts.GitHubContext
import it.krzeminski.githubactions.dsl.expressions.contexts.RunnerContext
import it.krzeminski.githubactions.dsl.expressions.contexts.SecretsContext

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
