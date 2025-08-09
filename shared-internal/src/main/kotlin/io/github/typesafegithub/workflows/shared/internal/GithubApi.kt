package io.github.typesafegithub.workflows.shared.internal

import arrow.core.Either
import arrow.core.raise.either
import io.github.typesafegithub.workflows.shared.internal.model.Version
import org.kohsuke.github.GHRef
import org.kohsuke.github.GitHubBuilder

fun fetchAvailableVersions(
    owner: String,
    name: String,
    githubAuthToken: String?,
    githubEndpoint: String = "https://api.github.com",
): Either<String, List<Version>> =
    either {
        val github =
            GitHubBuilder()
                .withEndpoint(githubEndpoint)
                .also {
                    if (githubAuthToken != null) {
                        it.withOAuthToken(githubAuthToken)
                    }
                }.build()
        val repository = github.getRepository("$owner/$name")
        val apiTags = repository.getRefs("tags").refsStartingWithV().map { Version(it) }
        val apiHeads = repository.getRefs("heads").refsStartingWithV().map { Version(it) }

        apiTags + apiHeads
    }

private fun Array<GHRef>.refsStartingWithV() = map { it.ref.substringAfterLast('/') }.filter { it.startsWith("v") }
