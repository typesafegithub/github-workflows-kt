package io.github.typesafegithub.workflows.dsl.expressions.contexts

import io.github.typesafegithub.workflows.dsl.expressions.ExpressionContext

/**
 * Vars
 *
 * Vars allow you to store configuration variables in your organization, repository, or repository environments.
 *
 * https://docs.github.com/en/actions/writing-workflows/choosing-what-your-workflow-does/accessing-contextual-information-about-workflow-runs#vars-context
 */
public object VarsContext : ExpressionContext("vars")
