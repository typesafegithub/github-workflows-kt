#!/usr/bin/env kotlin
@file:Repository("https://s01.oss.sonatype.org/content/repositories/snapshots/")
@file:DependsOn("io.github.typesafegithub:github-workflows-kt:1.4.1-20231029.193015-11")
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
