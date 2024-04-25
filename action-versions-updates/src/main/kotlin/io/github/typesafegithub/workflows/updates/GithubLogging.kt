package io.github.typesafegithub.workflows.updates

import java.io.File
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

internal interface GithubStepSummary {
    fun appendText(text: String)

    fun appendLine(line: String) {
        appendText(line + "\n")
    }

    companion object {
        fun fromEnv(): GithubStepSummary? {
            val path = System.getenv("GITHUB_STEP_SUMMARY")
            return path
                ?.let { File(it) }
                ?.takeIf { it.exists() }
                ?.let { githubStepSummaryFile ->
                    object : GithubStepSummary {
                        override fun appendText(text: String) {
                            githubStepSummaryFile.appendText(text)
                        }
                    }
                }
        }
    }
}

private val isGithubCi: Boolean by lazy {
    System.getenv("GITHUB_ACTIONS") == "true"
}

internal fun logSimple(
    level: String,
    message: String,
    title: String? = null,
    file: String? = null,
    col: Int? = null,
    endColumn: Int? = null,
    line: Int? = null,
    endLine: Int? = null,
) {
    val context =
        listOfNotNull(
            title?.let { "title=$it" },
            file?.let { "file=$it" },
            col?.let { "col=$it" },
            endColumn?.let { "endColumn=$it" },
            line?.let { "line=$it" },
            endLine?.let { "endLine=$it" },
        ).joinToString(",")
    println("$level: $context $message")
}

internal fun githubNotice(
    message: String,
    title: String? = null,
    file: String? = null,
    col: Int? = null,
    endColumn: Int? = null,
    line: Int? = null,
    endLine: Int? = null,
) {
    if (isGithubCi) {
        val parameters =
            listOfNotNull(
                title?.let { "title=$it" },
                file?.let { "file=$it" },
                col?.let { "col=$it" },
                endColumn?.let { "endColumn=$it" },
                line?.let { "line=$it" },
                endLine?.let { "endLine=$it" },
            ).joinToString(",")
        println("::notice $parameters::$message")
    } else {
        logSimple(
            level = "notice",
            message = message,
            title = title,
            file = file,
            col = col,
            endColumn = endColumn,
            line = line,
            endLine = endLine,
        )
    }
}

internal fun githubWarning(
    message: String,
    title: String? = null,
    file: String? = null,
    col: Int? = null,
    endColumn: Int? = null,
    line: Int? = null,
    endLine: Int? = null,
) {
    if (isGithubCi) {
        val parameters =
            listOfNotNull(
                title?.let { "title=$it" },
                file?.let { "file=$it" },
                col?.let { "col=$it" },
                endColumn?.let { "endColumn=$it" },
                line?.let { "line=$it" },
                endLine?.let { "endLine=$it" },
            ).joinToString(",")
        println("::warning $parameters::$message")
    } else {
        logSimple(
            level = "warning",
            message = message,
            title = title,
            file = file,
            col = col,
            endColumn = endColumn,
            line = line,
            endLine = endLine,
        )
    }
}

internal fun githubError(
    message: String,
    title: String? = null,
    file: String? = null,
    col: Int? = null,
    endColumn: Int? = null,
    line: Int? = null,
    endLine: Int? = null,
) {
    if (isGithubCi) {
        val parameters =
            listOfNotNull(
                title?.let { "title=$it" },
                file?.let { "file=$it" },
                col?.let { "col=$it" },
                endColumn?.let { "endColumn=$it" },
                line?.let { "line=$it" },
                endLine?.let { "endLine=$it" },
            ).joinToString(",")
        println("::error $parameters::$message")
    } else {
        logSimple(
            level = "error",
            message = message,
            title = title,
            file = file,
            col = col,
            endColumn = endColumn,
            line = line,
            endLine = endLine,
        )
    }
}

@OptIn(ExperimentalContracts::class)
internal fun githubGroup(
    title: String,
    block: () -> Unit,
) {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    if (isGithubCi) {
        println("::group::$title")
    } else {
        println(title)
    }
    block()
    if (isGithubCi) {
        println("::endgroup::")
    }
}
