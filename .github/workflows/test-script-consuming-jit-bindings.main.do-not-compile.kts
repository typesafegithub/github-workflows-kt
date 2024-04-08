#!/usr/bin/env kotlin
@file:Repository("https://repo1.maven.org/maven2/")
@file:DependsOn("io.github.typesafegithub:github-workflows-kt:1.13.0")

@file:Repository("http://localhost:8080/binding/")

// Regular, top-level action.
@file:DependsOn("actions:checkout:v4")

// Nested action.
@file:DependsOn("gradle:actions__setup-gradle:v3")

import io.github.typesafegithub.workflows.actions.actions.Checkout
import io.github.typesafegithub.workflows.actions.gradle.ActionsSetupGradle

println(Checkout())
println(ActionsSetupGradle())
