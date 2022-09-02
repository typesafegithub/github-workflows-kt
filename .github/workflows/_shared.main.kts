#!/usr/bin/env kotlin
@file:DependsOn("it.krzeminski:github-actions-kotlin-dsl:0.25.0")

import it.krzeminski.githubactions.actions.actions.SetupJavaV3
import it.krzeminski.githubactions.actions.actions.SetupPythonV4
import it.krzeminski.githubactions.dsl.JobBuilder

fun JobBuilder.setupJava() =
    uses(
        name = "Set up JDK",
        action = SetupJavaV3(
            javaVersion = "11",
            distribution = SetupJavaV3.Distribution.Zulu,
        )
    )

fun JobBuilder.setupPython() =
    uses(SetupPythonV4(pythonVersion = "3.8"))
