#!/usr/bin/env kotlin
@file:Repository("https://s01.oss.sonatype.org/content/repositories/snapshots/")
@file:DependsOn("io.github.typesafegithub:github-workflows-kt:1.8.1-20231226.125215-6")

import io.github.typesafegithub.workflows.domain.RunnerType
import io.github.typesafegithub.workflows.domain.triggers.PullRequest
import io.github.typesafegithub.workflows.dsl.workflow
import io.github.typesafegithub.workflows.yaml.writeToFile

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
        run(
            name = "Regular command",
            command = "echo 'Hello from regular command!'",
        )
        run(name = "Kotlin command") {
            println("Hello from Kotlin command!")
        }
    }
}.writeToFile()
