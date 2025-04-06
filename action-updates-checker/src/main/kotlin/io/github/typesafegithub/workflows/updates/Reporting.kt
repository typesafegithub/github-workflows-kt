package io.github.typesafegithub.workflows.updates

import io.github.typesafegithub.workflows.domain.Workflow
import io.github.typesafegithub.workflows.shared.internal.findGitRoot
import io.github.typesafegithub.workflows.shared.internal.getGithubAuthTokenOrNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlin.io.path.absolute
import kotlin.io.path.name
import kotlin.io.path.pathString
import kotlin.io.path.relativeTo

/**
 * will report all available updates in the terminal output and the github step summary
 * when no github auth token is present, reporting will be skipped
 *
 * @param reportWhenTokenUnset enable to use github api without a token
 * @param githubAuthToken if not set, will try to use one configured in the environment
 */
public fun Workflow.reportAvailableUpdates(
    reportWhenTokenUnset: Boolean = false,
    githubAuthToken: String? = null,
): Unit =
    runBlocking {
        if (System.getenv("GHWKT_RUN_STEP") == null) {
            reportAvailableUpdatesInternal(
                reportWhenTokenUnset = reportWhenTokenUnset,
                githubAuthToken = githubAuthToken,
            )
        }
    }

internal suspend fun Workflow.reportAvailableUpdatesInternal(
    reportWhenTokenUnset: Boolean = false,
    githubAuthToken: String? = null,
    stepSummary: GithubStepSummary? = GithubStepSummary.fromEnv(),
) {
    availableVersionsForEachAction(
        reportWhenTokenUnset = reportWhenTokenUnset,
        githubAuthToken = githubAuthToken ?: getGithubAuthTokenOrNull()?.first,
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
                    message = "updates available for ${file.pathString}:$line",
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
                    file
                        .absolute()
                        .relativeTo(gitRoot.absolute())
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
    }.onEmpty {
        githubNotice(
            message = "action-version-checker found no actions or skipped running",
        )
    }.toList()
        .also { regularActionVersions ->
            if (regularActionVersions.isNotEmpty()) {
                val hasOutdatedVersions =
                    regularActionVersions
                        .any { regularActionVersion ->
                            regularActionVersion.newerVersions.isNotEmpty()
                        }

                if (!hasOutdatedVersions) {
                    githubNotice(
                        message = "action-version-checker found no outdated actions",
                    )

                    stepSummary?.appendLine("action-version-checker found no outdated actions")
                }
            }
        }
}
