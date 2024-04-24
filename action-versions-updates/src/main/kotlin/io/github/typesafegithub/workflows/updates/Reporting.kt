package io.github.typesafegithub.workflows.updates

import io.github.typesafegithub.workflows.domain.Workflow
import io.github.typesafegithub.workflows.shared.internal.findGitRoot
import io.github.typesafegithub.workflows.shared.internal.getGithubTokenOrNull
import kotlin.io.path.absolute
import kotlin.io.path.name
import kotlin.io.path.pathString
import kotlin.io.path.relativeTo

/**
 * will report all available updates in the terminal output and the github step summary
 * looks up the github token from env `GITHUB_TOKEN` by default
 * when no github token is present, reporting will be skipped
 *
 * @param reportWhenTokenUnset enable to use github api without a token
 * @param githubToken if not set, will try to load from the environment variable `GITHUB_TOKEN`
 */
public fun Workflow.reportAvailableUpdates(
    reportWhenTokenUnset: Boolean = false,
    githubToken: String? = null,
): Unit =
    reportAvailableUpdatesInternal(
        reportWhenTokenUnset = reportWhenTokenUnset,
        githubToken = githubToken,
    )

internal fun Workflow.reportAvailableUpdatesInternal(
    reportWhenTokenUnset: Boolean = false,
    githubToken: String? = null,
    stepSummary: GithubStepSummary? = GithubStepSummary.fromEnv(),
) {
    availableVersionsForEachAction(
        reportWhenTokenUnset = reportWhenTokenUnset,
        githubToken = githubToken ?: getGithubTokenOrNull(),
    ).onEach { regularActionVersions ->
        val usesString =
            with(regularActionVersions.action) {
                "$actionOwner/$actionName@$actionVersion"
            }

        val stepNames = regularActionVersions.steps.map { it.name ?: it.id }

        if (regularActionVersions.newerVersions.isEmpty()) {
            return@onEach
        }

        githubGroup("new version available for $usesString") {
            val (file, line) = findDependencyDeclaration(regularActionVersions.action)

            if (file != null && line != null) {
                githubNotice(
                    message = "dependency notation at ${file.pathString}:$line",
                    file = file.name,
                    line = line,
                )
            }

            if (stepSummary != null && file != null) {
                stepSummary.appendLine("## available updates for `$usesString`")
                stepSummary.appendLine("used by steps: ${stepNames.joinToString { "`$it`" }}")
                val githubRepo = System.getenv("GITHUB_REPOSITORY") ?: "\$GITHUB_REPOSITORY"
                val refName = System.getenv("GITHUB_REF_NAME") ?: "\$GITHUB_REF_NAME"
                val baseUrl = "https://github.com/$githubRepo/tree/$refName"
                val lineAnchor = line?.let { "#L$line" }.orEmpty()
                val gitRoot = file.findGitRoot()
                val relativeToRepositoryRoot =
                    file.absolute().relativeTo(gitRoot.absolute())
                        .joinToString("/")
                stepSummary.appendLine(
                    "\n[${file.name}$lineAnchor]($baseUrl/${relativeToRepositoryRoot}$lineAnchor)",
                )
            }

            stepSummary?.appendLine("\n```kotlin")
            regularActionVersions.newerVersions.forEach { version ->
                val mavenCoordinates = regularActionVersions.action.mavenCoordinatesForAction(version)
                println(mavenCoordinates)
                stepSummary?.appendLine("@file:DependsOn(\"$mavenCoordinates\")")
            }
            stepSummary?.appendLine("```\n")
        }
    }
}
