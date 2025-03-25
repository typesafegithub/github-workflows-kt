package io.github.typesafegithub.workflows.shared.internal

import kotlinx.coroutines.runBlocking

/**
 * Returns a token that should be used to make authorized calls to GitHub,
 * or null if no token was configured.
 * The token may be of various kind, e.g. a Personal Access Token, or an
 * Application Installation Token.
 */
fun getGithubAuthTokenOrNull(): String? =
    runBlocking {
        (System.getenv("GITHUB_TOKEN") ?: getInstallationAccessToken())
            .also {
                if (it == null) {
                    println(ERROR_NO_CONFIGURATION)
                }
            }
    }

/**
 * Returns a token that should be used to make authorized calls to GitHub,
 * or throws an exception if no token was configured.
 * The token may be of various kind, e.g. a Personal Access Token, or an
 * Application Installation Token.
 */
fun getGithubAuthToken(): String = getGithubAuthTokenOrNull() ?: error(ERROR_NO_CONFIGURATION)

private val ERROR_NO_CONFIGURATION =
    """
    Missing environment variables for generating an auth token. There are two options:
    1. Create a personal access token at https://github.com/settings/tokens.
       The token needs to have public_repo scope. Then, set it in `GITHUB_TOKEN` env var.
       With this approach, listing versions for some actions may not work.
    2. Create a GitHub app, and generate a private key. Then, set it in `APP_PRIVATE_KEY` env var.
       With this approach, listing versions for all actions works.
    """.trimIndent()
