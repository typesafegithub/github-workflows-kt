#!/usr/bin/env kotlin
@file:DependsOn("io.github.typesafegithub:github-workflows-kt:0.48.0")
@file:Import("_shared.main.kts")

import io.github.typesafegithub.workflows.actions.actions.CheckoutV3
import io.github.typesafegithub.workflows.domain.RunnerType.UbuntuLatest
import io.github.typesafegithub.workflows.domain.triggers.WorkflowDispatch
import io.github.typesafegithub.workflows.dsl.workflow
import io.github.typesafegithub.workflows.yaml.writeToFile

workflow(
    name = "Release docs",
    on = listOf(WorkflowDispatch()),
    sourceFile = __FILE__.toPath(),
) {
    job(
        id = "release-docs",
        runsOn = UbuntuLatest,
    ) {
        uses(action = CheckoutV3())
        setupJava()
        setupPython()

        deployDocs()
    }
}.writeToFile()
