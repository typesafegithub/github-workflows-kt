#!/usr/bin/env kotlin
@file:DependsOn("io.github.typesafegithub:github-workflows-kt:1.6.0")
@file:Import("generated/actions/setup-python.kt")

import io.github.typesafegithub.workflows.dsl.JobBuilder

fun JobBuilder<*>.setupPython() =
    uses(action = SetupPython(pythonVersion = "3.8"))
