package it.krzeminski.githubactions.dsl.expressions

import it.krzeminski.githubactions.dsl.expressions.contexts.RunnerContext

/**
 * Root elements of GitHub expressions.
 *
 * https://docs.github.com/en/actions/learn-github-actions/expressions#about-expressions
 * https://docs.github.com/en/actions/learn-github-actions/contexts
 */
object Contexts {
    val runner: RunnerContext = RunnerContext

    val env: Env = Env

    val secrets: Secrets = Secrets
}
