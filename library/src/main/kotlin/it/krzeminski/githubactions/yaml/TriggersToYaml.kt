package it.krzeminski.githubactions.yaml

import it.krzeminski.githubactions.domain.Trigger

fun List<Trigger>.triggersToYaml(): String =
    this
        .map { it.toYamlString() }
        .joinToString(separator = "\n") { it }

private fun Trigger.toYamlString() =
    when (this) {
        Trigger.WorkflowDispatch -> "workflow_dispatch:"
        Trigger.Push -> "push:"
        Trigger.PullRequest -> "pull_request:"
        is Trigger.Schedule -> toYaml()
    }

private fun Trigger.Schedule.toYaml() = buildString {
    appendLine("schedule:")
    this@toYaml.triggers.forEach {
        appendLine(" - cron: '${it.expression}'")
    }
}.removeSuffix("\n")
