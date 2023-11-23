#!/usr/bin/env kotlin
@file:DependsOn("io.github.typesafegithub:github-workflows-kt:1.6.0")
@file:Import("generated/actions/setup-java.kt")

import io.github.typesafegithub.workflows.dsl.JobBuilder

fun JobBuilder<*>.setupJava() =
    uses(
        name = "Set up JDK",
        action = SetupJava(
            javaVersion = "11",
            distribution = SetupJava.Distribution.Zulu,
        )
    )
