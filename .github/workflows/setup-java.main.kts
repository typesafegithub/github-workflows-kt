#!/usr/bin/env kotlin
@file:DependsOn("io.github.typesafegithub:github-workflows-kt:1.13.0")
@file:Import("generated/actions/setup-java.kt")

import io.github.typesafegithub.workflows.annotations.ExperimentalClientSideBindings
import io.github.typesafegithub.workflows.dsl.JobBuilder

@OptIn(ExperimentalClientSideBindings::class)
fun JobBuilder<*>.setupJava() =
    uses(
        name = "Set up JDK",
        action = SetupJava(
            javaVersion = "11",
            distribution = SetupJava.Distribution.Zulu,
        )
    )
