#!/usr/bin/env kotlin
@file:Repository("https://repo.maven.apache.org/maven2/")
@file:DependsOn("io.github.typesafegithub:github-workflows-kt:3.7.0")
@file:DependsOn("io.kotest:kotest-assertions-core:5.9.1")

@file:Repository("http://localhost:8080")

// Regular, top-level action.
@file:DependsOn("actions:checkout:v4")

// Nested action.
@file:DependsOn("gradle:actions__setup-gradle:v3")

// Using specific version.
@file:DependsOn("actions:cache:v3.3.3")

// Using version ranges.
@file:DependsOn("gradle:actions__dependency-submission___major:[v3.3.1,v4-alpha)")
@file:DependsOn("gradle:actions__wrapper-validation___minor:[v4.2.1,v4.3-alpha)")

// To test case-insensitivity when referring to action names/owners.
@file:DependsOn("Actions:Setup-Node:v4")

// Always untyped action.
@file:DependsOn("typesafegithub:always-untyped-action-for-tests:v1")

import io.github.typesafegithub.workflows.actions.actions.Cache
import io.github.typesafegithub.workflows.actions.actions.Checkout
import io.github.typesafegithub.workflows.actions.actions.SetupNode
import io.github.typesafegithub.workflows.actions.actions.Checkout_Untyped
import io.github.typesafegithub.workflows.actions.gradle.ActionsSetupGradle
import io.github.typesafegithub.workflows.actions.gradle.ActionsDependencySubmission_Untyped
import io.github.typesafegithub.workflows.actions.gradle.ActionsWrapperValidation
import io.github.typesafegithub.workflows.actions.typesafegithub.AlwaysUntypedActionForTests_Untyped
import io.kotest.matchers.shouldBe

println(Checkout_Untyped(fetchTags_Untyped = "false"))
println(Checkout(fetchTags = false))
println(Checkout(fetchTags_Untyped = "false"))
println(AlwaysUntypedActionForTests_Untyped(foobar_Untyped = "baz"))
println(ActionsSetupGradle())
println(Cache(path = listOf("some-path"), key = "some-key"))
println(SetupNode())

ActionsDependencySubmission_Untyped().actionVersion shouldBe "v3"
ActionsWrapperValidation().actionVersion shouldBe "v4.2"

// Ensure that 'copy(...)' method is exposed.
Checkout(fetchTags = false).copy(fetchTags = true)
