#!/usr/bin/env kotlin
@file:DependsOn("it.krzeminski:github-actions-kotlin-dsl:0.26.0")

import it.krzeminski.githubactions.actions.actions.SetupJavaV3
import it.krzeminski.githubactions.actions.actions.SetupPythonV4
import it.krzeminski.githubactions.dsl.expressions.expr
import it.krzeminski.githubactions.dsl.JobBuilder

fun JobBuilder.setupJava() =
    uses(
        name = "Set up JDK",
        action = SetupJavaV3(
            javaVersion = "17",
            distribution = SetupJavaV3.Distribution.Zulu,
        )
    )

fun JobBuilder.setupPython() =
    uses(SetupPythonV4(pythonVersion = "3.8"))

val disableScheduledJobInForks =
    expr { "${github.repository_owner} == 'krzema13' || ${github.event_name} == 'schedule'" }
