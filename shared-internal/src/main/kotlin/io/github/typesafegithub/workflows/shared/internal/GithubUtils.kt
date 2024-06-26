package io.github.typesafegithub.workflows.shared.internal

fun getGithubTokenOrNull(): String? =
    System
        .getenv("GITHUB_TOKEN")
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

fun getGithubToken(): String =
    System.getenv("GITHUB_TOKEN")
        ?: error(
            """
            Missing environment variable export GITHUB_TOKEN=token
            Create a personal token at https://github.com/settings/tokens
            The token needs to have public_repo scope.
            """.trimIndent(),
        )
