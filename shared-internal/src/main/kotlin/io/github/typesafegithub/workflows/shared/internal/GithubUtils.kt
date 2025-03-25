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
                println(
                    """
                    Missing environment variable export GITHUB_TOKEN=token
                    Create a personal token at https://github.com/settings/tokens
                    The token needs to have public_repo scope.
                    """.trimIndent(),
                )
            }
        }
    }

/**
 * Returns a token that should be used to make authorized calls to GitHub,
 * or throws an exception if no token was configured.
 * The token may be of various kind, e.g. a Personal Access Token, or an
 * Application Installation Token.
 */
fun getGithubAuthToken(): String =
    getGithubAuthTokenOrNull()
        ?: error(
            """
            Missing environment variable export GITHUB_TOKEN=token
            Create a personal token at https://github.com/settings/tokens
            The token needs to have public_repo scope.
            """.trimIndent(),
        )
