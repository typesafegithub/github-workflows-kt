#!/usr/bin/env kotlin
@file:DependsOn("it.krzeminski:github-actions-kotlin-dsl:0.12.0")

@file:Import("build.main.kts")
@file:Import("check-if-wrappers-up-to-date.main.kts")
@file:Import("gradle-wrapper-validation.main.kts")
@file:Import("update-gradle-wrapper.main.kts")
@file:Import("actions-versions.main.kts")
@file:Import("github-payloads.main.kts")

import it.krzeminski.githubactions.yaml.writeToFile



listOf(
    buildWorkflow,
    checkIfWrappersUpToDateWorkflow,
    gradleWrapperValidationWorkflow,
    updateGradleWrapperWorkflow,
    checkIfNewActionVersionsWorkflow,
    updateGradleWrapperWorkflow,
    githubPayloadsWorkflow,
).forEach { it.writeToFile() }
