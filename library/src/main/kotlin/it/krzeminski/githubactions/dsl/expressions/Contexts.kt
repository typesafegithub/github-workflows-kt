package it.krzeminski.githubactions.dsl.expressions

import it.krzeminski.githubactions.dsl.expressions.contexts.EnvContext
import it.krzeminski.githubactions.dsl.expressions.contexts.RunnerContext
import it.krzeminski.githubactions.dsl.expressions.contexts.SecretsContext

/**
 * Root elements of GitHub expressions.
 *
 * https://docs.github.com/en/actions/learn-github-actions/expressions#about-expressions
 * https://docs.github.com/en/actions/learn-github-actions/contexts
 */
object Contexts {
    val runner: RunnerContext = RunnerContext

    val env: EnvContext = EnvContext

    val secrets: SecretsContext = SecretsContext
}
