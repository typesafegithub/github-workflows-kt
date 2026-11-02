package io.github.typesafegithub.workflows.shared.internal

import io.github.oshai.kotlinlogging.KotlinLogging.logger

private val logger = logger { }

/**
 * Returns a token that should be used to make authorized calls to GitHub,
 * or null if no token could be obtained.
 * The token may be of various kind, e.g. a Personal Access Token, or an
 * Application Installation Token.
 */
fun getGithubAuthTokenOrNull(): String? = getGithubAuthTokenOrNull(githubAuthConfig = getGithubAuthConfig())

private fun getGithubAuthTokenOrNull(githubAuthConfig: GithubAuthConfig): String? {
    val installationAccessToken =
        githubAuthConfig.githubAppConfig?.let { githubAppConfig ->
            runCatching {
                getInstallationAccessToken(githubAppConfig = githubAppConfig)
            }.onFailure {
                logger.warn(it) { "Failed to get GitHub App Installation token." }
            }.getOrNull()
        }

    return installationAccessToken ?: githubAuthConfig.token
}

/**
 * Returns a token that should be used to make authorized calls to GitHub,
 * or throws an exception if no token could be obtained.
 * The token may be of various kind, e.g. a Personal Access Token, or an
 * Application Installation Token.
 */
fun getGithubAuthToken(): String {
    val githubAuthConfig = getGithubAuthConfig()
    if (githubAuthConfig.token == null && githubAuthConfig.githubAppConfig == null) {
        error(ERROR_NO_CONFIGURATION)
    }
    return getGithubAuthTokenOrNull(githubAuthConfig = githubAuthConfig)
        ?: error(ERROR_AUTH_FAILED)
}

private val ERROR_NO_CONFIGURATION =
    """
    Missing environment variables for generating an auth token. There are two options:
    1. Create a personal access token at https://github.com/settings/tokens.
       The token needs to have public_repo scope. Then, set it in `GITHUB_TOKEN` env var.
       With this approach, listing versions for some actions may not work.
    2. Create a GitHub app, and generate a private key. Then, set it in `APP_PRIVATE_KEY` env var,
       along with `APP_INSTALLATION_ID` and `APP_CLIENT_ID`.
       With this approach, listing versions for all actions works.
    """.trimIndent()

private val ERROR_AUTH_FAILED =
    """
    Found GitHub auth configuration, but failed to obtain an auth token.
    If you use a GitHub App, make sure that the server's clock is in sync, as the generated JWT is time-sensitive.
    See the preceding warning log for the underlying error.
    """.trimIndent()
