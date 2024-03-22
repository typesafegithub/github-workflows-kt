#!/usr/bin/env kotlin
@file:DependsOn("org.jetbrains.kotlinx:kotlinx-datetime-jvm:0.6.0-RC")
@file:Repository("https://s01.oss.sonatype.org/content/repositories/snapshots/")
@file:Repository("https://repo.maven.apache.org/maven2/")
@file:DependsOn("io.github.typesafegithub:github-workflows-kt:1.12.0")
@file:OptIn(ExperimentalKotlinLogicStep::class)

import io.github.typesafegithub.workflows.actions.actions.CheckoutV4
import io.github.typesafegithub.workflows.annotations.ExperimentalKotlinLogicStep
import io.github.typesafegithub.workflows.domain.RunnerType.UbuntuLatest
import io.github.typesafegithub.workflows.domain.triggers.PullRequest
import io.github.typesafegithub.workflows.domain.triggers.Push
import io.github.typesafegithub.workflows.dsl.expressions.expr
import io.github.typesafegithub.workflows.dsl.workflow
import io.github.typesafegithub.workflows.yaml.writeToFile
import kotlinx.datetime.Clock

workflow(
    name = "Integration test with Kotlin logic",
    on = listOf(
        Push(branches = listOf("main")),
        PullRequest(),
    ),
    sourceFile = __FILE__.toPath(),
) {
    job(
        id = "main-job",
        runsOn = UbuntuLatest,
    ) {
        uses(action = CheckoutV4())
        run {
            println("Hello from Kotlin!")
            println("Current time: ${Clock.System.now()}")
            println("github.event: ${expr { github.event_name }}")
        }
    }
}.writeToFile()
