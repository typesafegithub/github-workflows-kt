#!/usr/bin/env kotlin
@file:Repository("https://repo.maven.apache.org/maven2/")
@file:DependsOn("io.github.typesafegithub:github-workflows-kt:3.5.0")

@file:Repository("https://bindings.krzeminski.it")
@file:DependsOn("actions:setup-java:v4")

import io.github.typesafegithub.workflows.actions.actions.SetupJava
import io.github.typesafegithub.workflows.dsl.JobBuilder

fun JobBuilder<*>.setupJava() =
    uses(
        name = "Set up JDK",
        action = SetupJava(
            javaVersion = "22",
            distribution = SetupJava.Distribution.Zulu,
            cache = SetupJava.BuildPlatform.Gradle,
        )
    )
