package io.github.typesafegithub.workflows.updates

import io.github.typesafegithub.workflows.domain.ActionStep
import io.github.typesafegithub.workflows.domain.Workflow
import io.github.typesafegithub.workflows.domain.actions.RegularAction
import io.github.typesafegithub.workflows.internal.githubGroup
import io.github.typesafegithub.workflows.internal.githubNotice
import io.github.typesafegithub.workflows.internal.githubStepSummaryAppendLine
import io.github.typesafegithub.workflows.internal.model.Version
import io.github.typesafegithub.workflows.internal.model.VersionUpdateAvailable
import java.io.File
import kotlin.io.path.exists
import kotlin.io.path.name
import kotlin.io.path.pathString
import kotlin.io.path.readText
import kotlin.io.path.relativeTo

public fun Workflow.getAvailableActionVersionUpdates(): List<VersionUpdateAvailable> {
    val actionSteps =
        jobs
            .flatMap { job ->
                job.steps
            }
            .filterIsInstance<ActionStep<*>>()

    return actionSteps.mapNotNull { step ->
        val action = step.action
        if (action is RegularAction<*>) {
            val availableVersions = action.fetchAvailableVersions()
            val currentVersion = Version(action.actionVersion)
            val newerVersions = availableVersions.filter { it > currentVersion }

            val line =
                sourceFile
                    ?.takeIf { it.exists() }
                    ?.let { sourceFile ->
                        val currentCoordinates =
                            VersionUpdateAvailable.mavenCoordinatesForAction(action, currentVersion)
                        sourceFile.readText().lines().indexOfFirst { line ->
                            line.contains("\"$currentCoordinates\"")
                        }
                    }

            if (newerVersions.isNotEmpty()) {
                VersionUpdateAvailable(
                    action = action,
                    stepName = step.name ?: step.id,
                    file = sourceFile,
                    line = line,
                    newVersions = newerVersions,
                )
            } else {
                null
            }
        } else {
            null
        }
    }
}

public fun Workflow.printActionVersionUpdates(stepSummary: Boolean = true) {
    val availableUpdates = getAvailableActionVersionUpdates()

    availableUpdates.forEach { update ->
        val action = update.action
        githubGroup(
            "${action.actionOwner}/${action.actionName}@${action.actionVersion} " +
                "has ${update.newVersions.size} new versions available",
        ) {
            githubNotice(
                "${action.actionOwner}/${action.actionName}@${action.actionVersion} " +
                    "has ${update.newVersions.size} new versions available",
            )

            if (update.file != null && update.line != null) {
                githubNotice(
                    message = "file ${update.file.pathString}:${update.line}",
                    file = update.file.name,
                    line = update.line,
                )
            }
            if (stepSummary) {
                githubStepSummaryAppendLine(
                    "## available updates for `${action.actionOwner}/${action.actionName}@${action.actionVersion}`",
                )
                update.file?.let { file ->
                    val githubRepo = System.getenv("GITHUB_REPOSITORY") ?: return@let
                    val refName = System.getenv("GITHUB_REF_NAME") ?: return@let
                    val baseUrl = "https://github.com/$githubRepo/tree/$refName"
                    val line = update.line?.let { "$#L$it" }.orEmpty()
                    val relativePath =
                        file
                            .relativeTo(File(".").toPath())
                            .joinToString("/")
                    githubStepSummaryAppendLine("")
                    githubStepSummaryAppendLine("[${file.name}$line]($baseUrl/$${relativePath}$line)")
                }

                githubStepSummaryAppendLine("")
                githubStepSummaryAppendLine("```kotlin")
            }
            update.newVersions.forEach { version ->
                val mavenCoordinates = update.mavenCoordinatesFor(version)
                println(mavenCoordinates)
                if (stepSummary) {
                    githubStepSummaryAppendLine("@file:DependsOn(\"$mavenCoordinates\")")
                }
            }
            if (stepSummary) {
                githubStepSummaryAppendLine("```")
                githubStepSummaryAppendLine("")
            }
        }
    }
}
