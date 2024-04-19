package io.github.typesafegithub.workflows.dsl

import io.github.typesafegithub.workflows.domain.ActionStep
import io.github.typesafegithub.workflows.domain.Workflow
import io.github.typesafegithub.workflows.domain.actions.RegularAction
import io.github.typesafegithub.workflows.internal.fetchAvailableVersions
import io.github.typesafegithub.workflows.internal.getGithubTokenOrNull
import io.github.typesafegithub.workflows.internal.githubGroup
import io.github.typesafegithub.workflows.internal.githubStepSummaryAppendLine
import io.github.typesafegithub.workflows.internal.githubWarning
import io.github.typesafegithub.workflows.internal.model.Version
import io.github.typesafegithub.workflows.internal.versions
import java.io.File
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.name
import kotlin.io.path.pathString
import kotlin.io.path.readText
import kotlin.io.path.relativeTo

internal val githubAuthorization = getGithubTokenOrNull()

public suspend fun Workflow.printActionVersionUpdates() {
    val actionSteps =
        jobs
            .flatMap { job ->
                job.steps
            }
            .filterIsInstance<ActionStep<*>>()

    actionSteps.forEach { step ->
        val action = step.action
        if (action is RegularAction<*>) {
            action.printActionVersionUpdates(
                step = step,
                file = sourceFile?.relativeTo(File(".").toPath()),
            )
        }
    }
}

internal suspend fun RegularAction<*>.printActionVersionUpdates(
    step: ActionStep<*>,
    file: Path?,
) {
    val currentVersion = Version(actionVersion)
    val availableVersions = fetchAvailableVersions(githubAuthorization).versions()

    val newerVersions = availableVersions.filter { it > currentVersion }

    if (newerVersions.isNotEmpty()) {
//        val allmajorVersions = newerVersions.map { it.major }
//        val majorVersions = newerVersions.filter { it.isMajorVersion() }
//        val otherVersions = newerVersions.filterNot { it.isMajorVersion() }
//        val latestVersions = newerVersions.sorted().takeLast(3)

        val mavenGroupId = actionOwner.replace(":", "__")
        val mavenArtifactId = actionName.replace(":", "__")
        githubGroup(
            "'$actionOwner/$actionName:$actionVersion' has ${newerVersions.size} newer versions available" +
                ", step '${step.name ?: step.id}'",
        ) {
            file?.let {
                val line =
                    file.takeIf { it.exists() }
                        ?.let {
                            it.readText().lines().indexOfFirst {
                                it.contains("\"$mavenGroupId:$mavenArtifactId:")
                            }
                        }
                githubWarning(
                    message = "this should point to ${file.pathString.substringAfterLast("../")}:$line",
                    file = file.name,
                    line = line,
                )
            }

            githubStepSummaryAppendLine("## available versions for $actionOwner/$actionName:$actionVersion")
            githubStepSummaryAppendLine("")
            githubStepSummaryAppendLine("```kotlin")
//            println("echo \"newer versions for $actionOwner/$actionName:$actionVersion\" >> \$GITHUB_STEP_SUMMARY")
//            println("echo \"```\" >> \$GITHUB_STEP_SUMMARY")
            newerVersions.forEach {
                val mavenVersion = it.version

                println("version `$mavenGroupId:$mavenArtifactId:$mavenVersion\"`")

                githubStepSummaryAppendLine(
                    "@file:DependsOn(\"$mavenGroupId:$mavenArtifactId:$mavenVersion\")",
                )
            }
        }
    }
}
