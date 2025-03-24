package io.github.typesafegithub.workflows.shared.internal

import kotlinx.coroutines.runBlocking


fun getAppAccessToken(): String = runBlocking { getInstallationAccessToken() }

fun getAppAccessTokenOrNull(): String? = runCatching { getAppAccessToken() }.getOrNull()
