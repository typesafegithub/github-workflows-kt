#!/usr/bin/env kotlin
@file:Repository("https://s01.oss.sonatype.org/content/repositories/snapshots/")
@file:DependsOn("io.github.typesafegithub:github-workflows-kt:1.4.1-20231029.193015-11")

import io.github.typesafegithub.workflows.dsl.expressions.expr

val disableScheduledJobInForks =
    expr { "${github.repository_owner} == 'typesafegithub' || ${github.event_name} != 'schedule'" }

val libraries = listOf(
    ":library",
    ":automation:action-binding-generator",
)
