package io.github.typesafegithub.workflows.internal

import java.io.File

private val GITHUB_STEP_SUMMARY =
    System.getenv("GITHUB_STEP_SUMMARY")
        ?.let { File(it) }
        ?.takeIf { it.exists() }

public fun githubStepSummaryAppendLine(message: String) {
    GITHUB_STEP_SUMMARY?.appendText(message + "\n")
}

public fun githubNotice(
    message: String,
    title: String? = null,
    file: String? = null,
    col: Int? = null,
    endColumn: Int? = null,
    line: Int? = null,
    endLine: Int? = null,
) {
    val parameters =
        listOfNotNull(
            title?.let { "title=$it" },
            file?.let { "file=$it" },
            col?.let { "col=$it" },
            endColumn?.let { "endColumn=$it" },
            line?.let { "line=$it" },
            endLine?.let { "endLine=$it" },
        ).joinToString(",")
    println("echo ::notice $parameters::$message")
}

public fun githubWarning(
    message: String,
    title: String? = null,
    file: String? = null,
    col: Int? = null,
    endColumn: Int? = null,
    line: Int? = null,
    endLine: Int? = null,
) {
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
}

public fun githubError(
    message: String,
    title: String? = null,
    file: String? = null,
    col: Int? = null,
    endColumn: Int? = null,
    line: Int? = null,
    endLine: Int? = null,
) {
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
}

public inline fun githubGroup(
    title: String,
    block: () -> Unit,
) {
    println("::group::$title")
    block()
    println("::endgroup::")
}
