#!/usr/bin/env kotlin
@file:Repository("https://repo1.maven.org/maven2/")
@file:DependsOn("io.github.typesafegithub:github-workflows-kt:1.14.0")

@file:Repository("https://github-workflows-kt-bindings.colman.com.br/binding/")
@file:DependsOn("actions:setup-python:v5")

import io.github.typesafegithub.workflows.actions.actions.SetupPython
import io.github.typesafegithub.workflows.dsl.JobBuilder

fun JobBuilder<*>.setupPython() =
    uses(action = SetupPython(pythonVersion = "3.8"))
