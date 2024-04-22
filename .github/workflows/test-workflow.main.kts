#!/usr/bin/env kotlin
@file:Repository("https://s01.oss.sonatype.org/content/repositories/snapshots/")
@file:DependsOn("io.github.typesafegithub:github-workflows-kt:1.14.1-20240422.101551-42")
@file:OptIn(ExperimentalKotlinLogicStep::class)

import io.github.typesafegithub.workflows.actions.actions.CheckoutV4
import io.github.typesafegithub.workflows.annotations.ExperimentalKotlinLogicStep
import io.github.typesafegithub.workflows.domain.RunnerType
import io.github.typesafegithub.workflows.domain.triggers.Push
import io.github.typesafegithub.workflows.dsl.workflow
import io.github.typesafegithub.workflows.yaml.writeToFile


workflow(
    name = "TEST WORKFLOW",
    on = listOf(Push()),
    sourceFile = __FILE__.toPath(),
) {
    job(
        id = "test-job",
        runsOn = RunnerType.UbuntuLatest,
    ) {
        uses(action = CheckoutV4())
        run(name = "Step with Kotlin logic") { github ->
            println("Hello from Kotlin!")
            println("github.sha: ${github.sha}")
            println(
                "Can do any sort of transformations: ${github.repository
                    .filter { it !in setOf('a', 'e', 'i', 'o', 'u', 'y') }
                    .uppercase()}",
            )
            println("Can access nested values: ${github.event.after}")
        }
    }
}.writeToFile()
