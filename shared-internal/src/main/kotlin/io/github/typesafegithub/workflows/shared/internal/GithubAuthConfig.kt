package io.github.typesafegithub.workflows.shared.internal

data class GithubAuthConfig(
    val token: String?,
    val githubAppConfig: GithubAppConfig?,
) {
    override fun toString(): String =
        "GithubAuthConfig(token=${if (token != null) REDACTED else null}, githubAppConfig=$githubAppConfig)"
}

data class GithubAppConfig(
    val appInstallationId: String,
    val appClientId: String,
    val appPrivateKey: String,
) {
    override fun toString(): String =
        "GithubAppConfig(appInstallationId=$appInstallationId, appClientId=$appClientId, appPrivateKey=$REDACTED)"
}

private const val REDACTED = "<redacted>"

fun getGithubAuthConfig(): GithubAuthConfig =
    GithubAuthConfig(
        token = env("GITHUB_TOKEN"),
        githubAppConfig = githubAppConfig(),
    )

private fun githubAppConfig(): GithubAppConfig? {
    val installationId = env("APP_INSTALLATION_ID")
    val clientId = env("APP_CLIENT_ID")
    val privateKey = env("APP_PRIVATE_KEY")
    return if (installationId != null && clientId != null && privateKey != null) {
        GithubAppConfig(
            appInstallationId = installationId,
            appClientId = clientId,
            appPrivateKey = privateKey,
        )
    } else {
        null
    }
}

private fun env(name: String): String? = System.getenv(name)?.takeIf { it.isNotBlank() }
