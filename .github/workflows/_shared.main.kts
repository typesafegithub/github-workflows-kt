#!/usr/bin/env kotlin
@file:DependsOn("io.github.typesafegithub:github-workflows-kt:0.44.0")

import io.github.typesafegithub.workflows.actions.actions.SetupJavaV3
import io.github.typesafegithub.workflows.actions.actions.SetupPythonV4
import io.github.typesafegithub.workflows.dsl.expressions.expr
import io.github.typesafegithub.workflows.dsl.JobBuilder

fun JobBuilder<*>.setupJava() =
    uses(
        name = "Set up JDK",
        action = SetupJavaV3(
            javaVersion = "11",
            distribution = SetupJavaV3.Distribution.Zulu,
            cache = SetupJavaV3.BuildPlatform.Gradle,
        )
    )

fun JobBuilder<*>.setupPython() =
    uses(SetupPythonV4(pythonVersion = "3.8"))

val disableScheduledJobInForks =
    expr { "${github.repository_owner} == 'typesafegithub' || ${github.event_name} != 'schedule'" }
