package it.krzeminski.githubactions.yaml

import it.krzeminski.githubactions.actions.fullName
import it.krzeminski.githubactions.domain.CommandStep
import it.krzeminski.githubactions.domain.ExternalActionStep
import it.krzeminski.githubactions.domain.Shell
import it.krzeminski.githubactions.domain.Shell.Bash
import it.krzeminski.githubactions.domain.Shell.Cmd
import it.krzeminski.githubactions.domain.Shell.Custom
import it.krzeminski.githubactions.domain.Shell.PowerShell
import it.krzeminski.githubactions.domain.Shell.Pwsh
import it.krzeminski.githubactions.domain.Shell.Python
import it.krzeminski.githubactions.domain.Shell.Sh
import it.krzeminski.githubactions.domain.Step

internal fun List<Step>.stepsToYaml(): List<Map<String, Any>> =
    this.map { it.toYaml() }

private fun Step.toYaml() =
    when (this) {
        is ExternalActionStep -> toYaml()
        is CommandStep -> toYaml()
    }

@Suppress("SpreadOperator")
private fun ExternalActionStep.toYaml(): Map<String, Any> =
    mapOfNotNullValues(
        "id" to id,
        "name" to name,
        "continue-on-error" to continueOnError,
        "timeout-minutes" to timeoutMinutes,
        "uses" to action.fullName,
        "with" to action.toYamlArguments().ifEmpty { null },
        "env" to env.ifEmpty { null },
        "if" to condition,
        *_customArguments.toList().toTypedArray(),
    )

@Suppress("SpreadOperator")
private fun CommandStep.toYaml(): Map<String, Any> =
    mapOfNotNullValues(
        "id" to id,
        "name" to name,
        "env" to env.ifEmpty { null },
        "continue-on-error" to continueOnError,
        "timeout-minutes" to timeoutMinutes,
        "shell" to shell?.toYaml(),
        "working-directory" to workingDirectory,
        "run" to command,
        "if" to condition,
        *_customArguments.toList().toTypedArray(),
    )

private fun Shell.toYaml() =
    when (this) {
        Bash -> "bash"
        Cmd -> "cmd"
        Pwsh -> "pwsh"
        PowerShell -> "powershell"
        Python -> "python"
        Sh -> "sh"
        is Custom -> this.value
    }
