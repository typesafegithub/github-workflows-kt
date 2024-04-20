package io.github.typesafegithub.workflows.updates.model

import io.github.typesafegithub.workflows.domain.ActionStep
import io.github.typesafegithub.workflows.domain.actions.RegularAction
import io.github.typesafegithub.workflows.shared.internal.model.Version

public data class RegularActionVersions(
    val action: RegularAction<*>,
    val steps: List<ActionStep<*>>,
    val newerVersions: List<Version>,
    val availableVersions: List<Version>,
)
