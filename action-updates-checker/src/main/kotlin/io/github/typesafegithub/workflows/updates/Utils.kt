package io.github.typesafegithub.workflows.updates

import arrow.core.getOrElse
import io.github.typesafegithub.workflows.domain.ActionStep
import io.github.typesafegithub.workflows.domain.Workflow
import io.github.typesafegithub.workflows.domain.actions.RegularAction
import io.github.typesafegithub.workflows.shared.internal.fetchAvailableVersions
import io.github.typesafegithub.workflows.shared.internal.getAppAccessTokenOrNull
import io.github.typesafegithub.workflows.shared.internal.model.Version
import io.github.typesafegithub.workflows.updates.model.RegularActionVersions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.readText

internal fun Workflow.availableVersionsForEachAction(
    reportWhenTokenUnset: Boolean = true,
    githubAppAccessToken: String? = getAppAccessTokenOrNull(),
): Flow<RegularActionVersions> {
    if (githubAppAccessToken == null && !reportWhenTokenUnset) {
        githubWarning("github app token is required, but not set, skipping api calls")
        return emptyFlow()
    }
    val groupedSteps = groupStepsByAction()
    return flow {
        groupedSteps.forEach { (action, steps) ->
            val availableVersions =
                action.fetchAvailableVersionsOrWarn(
                    githubToken = githubAppAccessToken,
                )
            val currentVersion = Version(action.actionVersion)
            if (availableVersions != null) {
                val newerVersions =
                    availableVersions
                        .filter { it > currentVersion }
                        .filter { it.isMajorVersion() == currentVersion.isMajorVersion() }
                val value =
                    RegularActionVersions(
                        action = action,
                        steps = steps,
                        newerVersions = newerVersions,
                        availableVersions = availableVersions,
                    )
                emit(value)
            }
        }
    }
}

internal suspend fun RegularAction<*>.fetchAvailableVersionsOrWarn(githubToken: String?): List<Version>? =
    try {
        fetchAvailableVersions(
            owner = actionOwner,
            name = actionName.substringBefore('/'),
            githubAppAccessToken = githubToken,
        ).getOrElse {
            throw Exception(it)
        }
    } catch (e: Exception) {
        githubError(
            "failed to fetch versions for $actionOwner/$actionName, skipping",
        )
        null
    }

internal fun Workflow.groupStepsByAction(): List<Pair<RegularAction<*>, List<ActionStep<*>>>> {
    val actionSteps =
        jobs
            .flatMap { job ->
                job.steps
            }.filterIsInstance<ActionStep<*>>()
    return actionSteps
        .groupBy { step ->
            when (val action = step.action) {
                is RegularAction<*> -> {
                    "${action.actionOwner}/${action.actionName}@${action.actionVersion}"
                }

                else -> {
                    null
                }
            }
        }.values
        .toList()
        .mapNotNull { list ->
            val action = list.firstNotNullOfOrNull { it.action as? RegularAction<*> }
            action?.let {
                it to list
            }
        }
}

internal fun RegularAction<*>.mavenCoordinatesForAction(version: Version? = null): String {
    val mavenGroupId = actionOwner.replace("/", "__")
    val mavenArtifactId = actionName.replace("/", "__")
    return "$mavenGroupId:$mavenArtifactId:${version?.version ?: actionVersion}"
}

internal fun Workflow.findDependencyDeclaration(action: RegularAction<*>): Pair<Path?, Int?> {
    val file = sourceFile?.toPath()?.takeIf { it.exists() }
    val line =
        file?.let { sourceFile ->
            val currentCoordinates = action.mavenCoordinatesForAction()
            val index =
                sourceFile.readText().lines().indexOfFirst { line ->
                    line.contains("\"$currentCoordinates\"")
                }
            index + 1
        }
    return file to line
}
