package io.github.typesafegithub.workflows.yaml

import io.github.typesafegithub.workflows.domain.contexts.Contexts
import io.github.typesafegithub.workflows.domain.contexts.GithubContext
import kotlinx.serialization.json.Json

internal fun loadContextsFromEnvVars(getenv: (String) -> String?): Contexts {
    fun getEnvVarOrFail(varName: String): String = getenv(varName) ?: error("$varName should be set!")

    val githubContextRaw = getEnvVarOrFail("GHWKT_GITHUB_CONTEXT_JSON")
    val githubContext = json.decodeFromString<GithubContext>(githubContextRaw)
    return Contexts(
        github = githubContext,
    )
}

private val json = Json { ignoreUnknownKeys = true }
