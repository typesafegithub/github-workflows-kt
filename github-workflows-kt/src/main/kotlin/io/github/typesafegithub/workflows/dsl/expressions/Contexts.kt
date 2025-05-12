package io.github.typesafegithub.workflows.dsl.expressions

import io.github.typesafegithub.workflows.dsl.expressions.contexts.EnvContext
import io.github.typesafegithub.workflows.dsl.expressions.contexts.FunctionsContext
import io.github.typesafegithub.workflows.dsl.expressions.contexts.GitHubContext
import io.github.typesafegithub.workflows.dsl.expressions.contexts.RunnerContext
import io.github.typesafegithub.workflows.dsl.expressions.contexts.SecretsContext
import io.github.typesafegithub.workflows.dsl.expressions.contexts.VarsContext

/**
 * Root elements of GitHub expressions.
 *
 * https://docs.github.com/en/actions/learn-github-actions/expressions#about-expressions
 * https://docs.github.com/en/actions/learn-github-actions/contexts
 */
public object Contexts : FunctionsContext() {
    public val env: EnvContext = EnvContext
    public val github: GitHubContext = GitHubContext
    public val runner: RunnerContext = RunnerContext
    public val secrets: SecretsContext = SecretsContext
    public val vars: VarsContext = VarsContext
}
