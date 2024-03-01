#!/usr/bin/env kotlin
@file:DependsOn("io.github.typesafegithub:github-workflows-kt:1.12.0")
@file:Import("release-common.main.kts")
@file:Import("setup-java.main.kts")
@file:Import("setup-python.main.kts")
@file:Import("generated/actions/checkout.kt")

import io.github.typesafegithub.workflows.annotations.ExperimentalClientSideBindings
import io.github.typesafegithub.workflows.domain.RunnerType.UbuntuLatest
import io.github.typesafegithub.workflows.domain.triggers.WorkflowDispatch
import io.github.typesafegithub.workflows.dsl.workflow
import io.github.typesafegithub.workflows.yaml.writeToFile

@OptIn(ExperimentalClientSideBindings::class)
workflow(
    name = "Release docs",
    on = listOf(WorkflowDispatch()),
    sourceFile = __FILE__.toPath(),
) {
    job(
        id = "release-docs",
        runsOn = UbuntuLatest,
    ) {
        uses(action = Checkout())
        setupJava()
        setupPython()

        deployDocs()
    }
}.writeToFile(generateActionBindings = true)
