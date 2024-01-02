#!/usr/bin/env kotlin
@file:Repository("https://s01.oss.sonatype.org/content/repositories/snapshots/")
@file:DependsOn("io.github.typesafegithub:github-workflows-kt:1.8.1-20240102.194124-25")
@file:ExperimentalKotlinLogicStep

import io.github.typesafegithub.workflows.actions.actions.CheckoutV4
import io.github.typesafegithub.workflows.domain.RunnerType
import io.github.typesafegithub.workflows.domain.triggers.PullRequest
import io.github.typesafegithub.workflows.dsl.workflow
import io.github.typesafegithub.workflows.yaml.writeToFile
import io.github.typesafegithub.workflows.annotations.ExperimentalKotlinLogicStep

workflow(
    name = "Test workflow",
    on = listOf(
        PullRequest(),
    ),
    sourceFile = __FILE__.toPath(),
) {
    job(
        id = "test_job",
        runsOn = RunnerType.UbuntuLatest,
    ) {
        uses(action = CheckoutV4())
        run(
            name = "Regular command",
            command = "echo 'Hello from regular command!'",
        )
        run(name = "Kotlin command") {
            println("Hello from Kotlin command!")
        }
        run(name = "Another kotlin command") {
            println("This should run faster because the script is already compiled")
            println(('a'..'z').joinToString())
        }
    }
}.writeToFile()
