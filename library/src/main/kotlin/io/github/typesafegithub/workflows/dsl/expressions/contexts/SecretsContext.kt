package io.github.typesafegithub.workflows.dsl.expressions.contexts

import io.github.typesafegithub.workflows.dsl.expressions.ExpressionContext

/**
 * Encrypted secrets
 *
 * Encrypted secrets allow you to store sensitive information in your organization,
 * repository, or repository environments.
 *
 * https://docs.github.com/en/actions/security-guides/encrypted-secrets
 */
public object SecretsContext : ExpressionContext("secrets") {

    /***
     * GITHUB_TOKEN is a secret that is automatically created for every workflow run,
     * and is always included in the secrets context. For more information, see "Automatic token authentication."
     * https://docs.github.com/en/actions/security-guides/automatic-token-authentication
     */
    public val GITHUB_TOKEN: String by propertyToExprPath
}
