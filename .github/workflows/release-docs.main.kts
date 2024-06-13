#!/usr/bin/env kotlin
@file:Repository("https://repo1.maven.org/maven2/")
@file:DependsOn("io.github.typesafegithub:github-workflows-kt:2.1.0")

@file:Repository("https://bindings.krzeminski.it")
@file:DependsOn("actions:checkout:v4")

@file:Import("release-common.main.kts")
@file:Import("setup-java.main.kts")
@file:Import("setup-python.main.kts")

import io.github.typesafegithub.workflows.actions.actions.Checkout
import io.github.typesafegithub.workflows.domain.RunnerType.UbuntuLatest
import io.github.typesafegithub.workflows.domain.triggers.WorkflowDispatch
import io.github.typesafegithub.workflows.dsl.workflow

workflow(
    name = "Release docs",
    on = listOf(WorkflowDispatch()),
    sourceFile = __FILE__,
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
}
