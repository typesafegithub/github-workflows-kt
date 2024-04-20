package io.github.typesafegithub.workflows.updates

import java.io.File

public interface GithubStepSummary {
    public fun appendText(text: String)

    public fun appendLine(line: String) {
        appendText(line + "\n")
    }

    public companion object {
        public fun fromEnv(): GithubStepSummary? {
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
