package io.github.typesafegithub.workflows.shared.internal

data class GithubAuthConfig(
    val token: String?,
    val githubAppConfig: GithubAppConfig?,
)

data class GithubAppConfig(
    val appInstallationId: String,
    val appClientId: String,
    val appPrivateKey: String,
)

fun getGithubAuthConfig(): GithubAuthConfig =
    GithubAuthConfig(
        token = System.getenv("GITHUB_TOKEN"),
        githubAppConfig = githubAppConfig(),
    )

private fun githubAppConfig(): GithubAppConfig? {
    val installationId = System.getenv("APP_INSTALLATION_ID") ?: return null
    val clientId = System.getenv("APP_CLIENT_ID") ?: return null
    val privateKey = System.getenv("APP_PRIVATE_KEY") ?: return null
    return GithubAppConfig(
        appInstallationId = installationId,
        appClientId = clientId,
        appPrivateKey = privateKey,
    )
}
