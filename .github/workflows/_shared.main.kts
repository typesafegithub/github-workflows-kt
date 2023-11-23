#!/usr/bin/env kotlin
@file:DependsOn("io.github.typesafegithub:github-workflows-kt:1.6.0")

import io.github.typesafegithub.workflows.dsl.expressions.expr

val disableScheduledJobInForks =
    expr { "${github.repository_owner} == 'typesafegithub' || ${github.event_name} != 'schedule'" }

val libraries = listOf(
    ":library",
    ":automation:action-binding-generator",
)
