package io.github.typesafegithub.workflows

import io.github.typesafegithub.workflows.domain.actions.RegularAction
import io.github.typesafegithub.workflows.internal.GithubRef
import io.github.typesafegithub.workflows.internal.fetchAvailableVersions
import io.github.typesafegithub.workflows.internal.getGithubTokenOrNull
import io.github.typesafegithub.workflows.internal.model.Version
import io.github.typesafegithub.workflows.internal.versions
import kotlinx.coroutines.runBlocking

internal val githubAuthorization = getGithubTokenOrNull()

public fun RegularAction<*>.fetchAvailableVersionsAsRefs(): List<GithubRef> =
    runBlocking {
        fetchAvailableVersions(githubAuthorization)
    }

public fun RegularAction<*>.fetchAvailableVersions(): List<Version> =
    fetchAvailableVersionsAsRefs()
        .versions()
