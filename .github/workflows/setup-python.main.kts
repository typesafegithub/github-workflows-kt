#!/usr/bin/env kotlin
@file:Repository("https://repo.maven.apache.org/maven2/")
@file:DependsOn("io.github.typesafegithub:github-workflows-kt:3.5.0")

@file:Repository("https://bindings.krzeminski.it")
@file:DependsOn("actions:setup-python:v6")

import io.github.typesafegithub.workflows.actions.actions.SetupPython
import io.github.typesafegithub.workflows.dsl.JobBuilder

fun JobBuilder<*>.setupPython() =
    uses(action = SetupPython(pythonVersion = "3.14"))
