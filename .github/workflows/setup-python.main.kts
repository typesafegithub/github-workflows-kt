#!/usr/bin/env kotlin
@file:DependsOn("io.github.typesafegithub:github-workflows-kt:1.9.0")
@file:Import("generated/actions/setup-python.kt")

import io.github.typesafegithub.workflows.annotations.ExperimentalClientSideBindings
import io.github.typesafegithub.workflows.dsl.JobBuilder

@OptIn(ExperimentalClientSideBindings::class)
fun JobBuilder<*>.setupPython() =
    uses(action = SetupPython(pythonVersion = "3.8"))
