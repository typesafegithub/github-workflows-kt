#!/usr/bin/env kotlin
@file:Repository("https://repo.maven.apache.org/maven2/")
@file:DependsOn("io.github.typesafegithub:github-workflows-kt:3.0.0")

@file:Repository("http://localhost:8080/v2")

// Regular, top-level action.
@file:DependsOn("actions:checkout:v4")

// Nested action.
@file:DependsOn("gradle:actions__setup-gradle:v3")

// Using specific version.
@file:DependsOn("actions:cache:v3.3.3")

// Always untyped action.
@file:DependsOn("typesafegithub:always-untyped-action-for-tests:v1")

import io.github.typesafegithub.workflows.actions.actions.Cache
import io.github.typesafegithub.workflows.actions.actions.Checkout
import io.github.typesafegithub.workflows.actions.actions.Checkout_Untyped
import io.github.typesafegithub.workflows.actions.gradle.ActionsSetupGradle
import io.github.typesafegithub.workflows.actions.typesafegithub.AlwaysUntypedActionForTests_Untyped

println(Checkout_Untyped(fetchTags_Untyped = "false"))
println(Checkout(fetchTags = false))
println(Checkout(fetchTags_Untyped = "false"))
println(AlwaysUntypedActionForTests_Untyped(foobar_Untyped = "baz"))
println(ActionsSetupGradle())
println(Cache(path = listOf("some-path"), key = "some-key"))

// Ensure that 'copy(...)' method is exposed.
Checkout(fetchTags = false).copy(fetchTags = true)
