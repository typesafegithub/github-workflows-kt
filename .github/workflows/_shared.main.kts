#!/usr/bin/env kotlin
@file:DependsOn("io.github.typesafegithub:github-workflows-kt:3.6.0")

import io.github.typesafegithub.workflows.dsl.expressions.expr

val disableScheduledJobInForks =
    expr { "${github.repository_owner} == 'typesafegithub' || ${github.event_name} != 'schedule'" }

// The order is deliberate here. First, libraries with no dependencies are published.
// Then, libraries that depend on already published libraries, and so on. Thanks to
// such order, newly released artifacts already have their dependencies in place and
// are ready to be used.
val libraries = listOf(
    ":shared-internal",
    ":github-workflows-kt",
    ":action-binding-generator",
    ":action-updates-checker",
)
