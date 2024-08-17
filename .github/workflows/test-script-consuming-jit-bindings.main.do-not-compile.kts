#!/usr/bin/env kotlin
@file:Repository("https://repo.maven.apache.org/maven2/")
@file:DependsOn("io.github.typesafegithub:github-workflows-kt:1.13.0")

@file:Repository("http://localhost:8080")

// Regular, top-level action.
@file:DependsOn("actions:checkout:v4")

// Nested action.
@file:DependsOn("gradle:actions__setup-gradle:v3")

// Using specific version.
@file:DependsOn("actions:cache:v3.3.3")

import io.github.typesafegithub.workflows.actions.actions.Cache
import io.github.typesafegithub.workflows.actions.actions.Checkout
import io.github.typesafegithub.workflows.actions.gradle.ActionsSetupGradle

println(Checkout_Untyped(fetchTags = "false"))
println(Checkout())
println(ActionsSetupGradle())
println(Cache(path = listOf("some-path"), key = "some-key"))
