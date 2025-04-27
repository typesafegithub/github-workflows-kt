package io.github.typesafegithub.workflows.shared.internal

import io.github.typesafegithub.workflows.shared.internal.model.Version
import org.kohsuke.github.GHRef
import org.kohsuke.github.GitHubBuilder

fun fetchAvailableVersions(
    owner: String,
    name: String,
    githubAuthToken: String,
    githubEndpoint: String = "https://api.github.com",
): Result<List<Version>> =
    runCatching {
        val github = GitHubBuilder().withEndpoint(githubEndpoint).withOAuthToken(githubAuthToken).build()
        val repository = github.getRepository("$owner/$name")
        val apiTags = repository.getRefs("tags").refsStartingWithV().map { Version(it) }
        val apiHeads = repository.getRefs("heads").refsStartingWithV().map { Version(it) }

        apiTags + apiHeads
    }

private fun Array<GHRef>.refsStartingWithV() = map { it.ref.substringAfterLast('/') }.filter { it.startsWith("v") }
