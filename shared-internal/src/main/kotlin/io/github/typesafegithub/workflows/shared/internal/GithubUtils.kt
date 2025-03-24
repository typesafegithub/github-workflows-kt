package io.github.typesafegithub.workflows.shared.internal

import io.github.typesafegithub.workflows.shared.internal.app.GitHubApp
import kotlinx.coroutines.runBlocking

private val githubApp = GitHubApp()

fun getAppAccessToken(): String = runBlocking { githubApp.accessToken() }

fun getAppAccessTokenOrNull(): String? = runCatching { getAppAccessToken() }.getOrNull()
