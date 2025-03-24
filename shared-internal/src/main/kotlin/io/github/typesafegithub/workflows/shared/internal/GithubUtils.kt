package io.github.typesafegithub.workflows.shared.internal

import kotlinx.coroutines.runBlocking

fun getAppAccessToken(): String = runBlocking { System.getenv("GITHUB_TOKEN") ?: getInstallationAccessToken() }

fun getAppAccessTokenOrNull(): String? = runCatching { getAppAccessToken() }.getOrNull()
