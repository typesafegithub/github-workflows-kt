package io.github.typesafegithub.workflows.yaml

import io.github.typesafegithub.workflows.domain.Job
import io.github.typesafegithub.workflows.domain.RunnerType
import io.github.typesafegithub.workflows.domain.RunnerType.Custom
import io.github.typesafegithub.workflows.domain.RunnerType.Labelled
import io.github.typesafegithub.workflows.domain.RunnerType.MacOS1015
import io.github.typesafegithub.workflows.domain.RunnerType.MacOS11
import io.github.typesafegithub.workflows.domain.RunnerType.MacOSLatest
import io.github.typesafegithub.workflows.domain.RunnerType.Ubuntu1804
import io.github.typesafegithub.workflows.domain.RunnerType.Ubuntu2004
import io.github.typesafegithub.workflows.domain.RunnerType.UbuntuLatest
import io.github.typesafegithub.workflows.domain.RunnerType.Windows2016
import io.github.typesafegithub.workflows.domain.RunnerType.Windows2019
import io.github.typesafegithub.workflows.domain.RunnerType.Windows2022
import io.github.typesafegithub.workflows.domain.RunnerType.Windows2025
import io.github.typesafegithub.workflows.domain.RunnerType.WindowsLatest
import io.github.typesafegithub.workflows.internal.InternalGithubActionsApi

internal fun List<Job<*>>.jobsToYaml(): Map<String, Map<String, Any?>> =
    this.associateBy(
        keySelector = { it.id },
        valueTransform = { it.toYaml() },
    )

@Suppress("SpreadOperator")
private fun Job<*>.toYaml(): Map<String, Any?> =
    mapOfNotNullValues(
        "name" to name,
        "runs-on" to runsOn.toYaml(),
        "permissions" to permissions?.mapToYaml(),
        "concurrency" to
            concurrency?.let {
                mapOf(
                    "group" to it.group,
                    "cancel-in-progress" to it.cancelInProgress,
                )
            },
        "needs" to needs.ifEmpty { null }?.map { it.id },
        "env" to env.ifEmpty { null },
        "if" to condition,
        "strategy" to
            strategyMatrix?.let {
                mapOf(
                    "matrix" to it,
                )
            },
        "timeout-minutes" to timeoutMinutes,
        "outputs" to outputs.outputMapping.ifEmpty { null },
        "container" to container?.toYaml(),
        "environment" to
            environment?.let {
                mapOfNotNullValues(
                    "name" to it.name,
                    "url" to it.url,
                )
            },
        "services" to services.ifEmpty { null }?.mapValues { it.value.toYaml() },
    ) + _customArguments +
        mapOf("steps" to steps.stepsToYaml())

@InternalGithubActionsApi
@Suppress("CyclomaticComplexMethod")
public fun RunnerType.toYaml(): Any =
    when (this) {
        is Custom -> {
            runsOn
        }

        is Labelled -> {
            labels.toList()
        }

        is RunnerType.Group -> {
            mapOfNotNullValues(
                "group" to name,
                "labels" to labels?.toList(),
            )
        }

        UbuntuLatest -> {
            "ubuntu-latest"
        }

        WindowsLatest -> {
            "windows-latest"
        }

        MacOSLatest -> {
            "macos-latest"
        }

        Windows2025 -> {
            "windows-2025"
        }

        Windows2022 -> {
            "windows-2022"
        }

        Windows2019 -> {
            "windows-2019"
        }

        Windows2016 -> {
            "windows-2016"
        }

        Ubuntu2004 -> {
            "ubuntu-20.04"
        }

        Ubuntu1804 -> {
            "ubuntu-18.04"
        }

        MacOS11 -> {
            "macos-11"
        }

        MacOS1015 -> {
            "macos-10.15"
        }
    }
