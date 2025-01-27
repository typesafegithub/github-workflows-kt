package io.github.typesafegithub.workflows.jitbindingserver

import io.github.oshai.kotlinlogging.KotlinLogging.logger
import io.ktor.http.content.PartData
import io.ktor.utils.io.readRemaining
import io.ktor.utils.io.readText

private val logger = logger { }

internal suspend fun PartData.asString() =
    runCatching {
        when (this) {
            is PartData.FileItem -> provider().readRemaining().readText()
            is PartData.FormItem -> value
            else -> {
                logger.error { "Unexpected part data ${this::class.simpleName}" }
                error("Unexpected part data ${this::class.simpleName}")
            }
        }
    }
