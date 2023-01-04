#!/usr/bin/env kotlin
@file:Repository("https://jitpack.io")
@file:DependsOn("com.github.krzema12:github-actions-kotlin-dsl:install-jdk-17-in-consistency-check-SNAPSHOT")

import it.krzeminski.githubactions.actions.actions.SetupJavaV3
import it.krzeminski.githubactions.actions.actions.SetupPythonV4
import it.krzeminski.githubactions.dsl.expressions.expr
import it.krzeminski.githubactions.dsl.JobBuilder

fun JobBuilder<*>.setupJava() =
    uses(
        name = "Set up JDK",
        action = SetupJavaV3(
            javaVersion = "17",
            distribution = SetupJavaV3.Distribution.Zulu,
            cache = SetupJavaV3.BuildPlatform.Gradle,
        )
    )

fun JobBuilder<*>.setupPython() =
    uses(SetupPythonV4(pythonVersion = "3.8"))

val disableScheduledJobInForks =
    expr { "${github.repository_owner} == 'krzema12' || ${github.event_name} != 'schedule'" }
