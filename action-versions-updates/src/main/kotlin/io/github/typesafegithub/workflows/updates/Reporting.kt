package io.github.typesafegithub.workflows.updates

import io.github.typesafegithub.workflows.domain.Workflow
import io.github.typesafegithub.workflows.shared.internal.findGitRoot
import io.github.typesafegithub.workflows.shared.internal.getGithubTokenOrNull
import kotlin.io.path.absolute
import kotlin.io.path.name
import kotlin.io.path.pathString
import kotlin.io.path.relativeTo

public fun Workflow.reportAvailableUpdates(
    skipAlreadyUpToDate: Boolean = true,
    stepSummary: GithubStepSummary? = GithubStepSummary.fromEnv(),
    githubToken: String? = null,
) {
    availableVersionsForEachAction(
        githubToken = githubToken ?: getGithubTokenOrNull(),
    ) {
        val usesString =
            with(action) {
                "$actionOwner/$actionName@$actionVersion"
            }

        val stepNames = steps.map { it.name ?: it.id }

        if (newerVersions.isEmpty()) {
            if (!skipAlreadyUpToDate) {
                stepSummary?.appendLine("\n## action `$usesString` is up to date")
                stepSummary?.appendLine("used by steps: ${stepNames.joinToString { "`$it`" }}")
                githubNotice("action $usesString is up to date (used by steps: $stepNames)")
            }
            return@availableVersionsForEachAction
        }

        val (file, line) = findDependencyDeclaration(action)

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
                file.absolute()
                    .relativeTo(gitRoot.absolute())
                    .joinToString("/")
            stepSummary.appendLine(
                "\n[${file.name}$lineAnchor]($baseUrl/${relativeToRepositoryRoot}$lineAnchor)",
            )
        }

        stepSummary?.appendLine("\n```kotlin")
        newerVersions.forEach { version ->
            val mavenCoordinates = action.mavenCoordinatesForAction(version)
            println(mavenCoordinates)
            stepSummary?.appendLine("@file:DependsOn(\"$mavenCoordinates\")")
        }
        stepSummary?.appendLine("```\n")
    }
}
