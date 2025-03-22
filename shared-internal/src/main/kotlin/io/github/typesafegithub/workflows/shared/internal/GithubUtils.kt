package io.github.typesafegithub.workflows.shared.internal

import io.github.typesafegithub.workflows.shared.internal.app.GitHubApp
import kotlinx.coroutines.runBlocking

private val githubApp = GitHubApp()

fun getGithubToken(): String = runBlocking { githubApp.accessToken() }

fun getGithubTokenOrNull(): String? = runCatching { getGithubToken() }.getOrNull()
