package io.github.typesafegithub.workflows.domain

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText

/**
 * Lists values allowed to be set in "runs-on" for a job, for GitHub-hosted runners.
 */
suspend fun listRunnerLabels(): List<String> {
    val html = HttpClient().get(GITHUB_RUNNER_LABELS_DOCS_URL).bodyAsText()
    return parse(html)
}

private const val GITHUB_RUNNER_LABELS_DOCS_URL: String =
    "https://docs.github.com/en/actions/using-workflows/workflow-syntax-for-github-actions"

private fun parse(html: String): List<String> =
    RUNNER_LABEL_REGEX
        .findAll(html)
        .map { it.groupValues[1] }
        .toList()

private val RUNNER_LABEL_REGEX =
    Regex(
        """<code><a href="[^"]*github\.com/actions/(?:partner-)?runner-images/[^"]*">([^<]+)</a></code>""",
    )
