#!/usr/bin/env kotlin
@file:DependsOn("it.krzeminski:github-actions-kotlin-dsl:0.10.0")

@file:Import("build.main.kts")
@file:Import("check-if-wrappers-up-to-date.main.kts")
@file:Import("generate-wrappers.main.kts")
@file:Import("gradle-wrapper-validation.main.kts")
@file:Import("update-gradle-wrapper.main.kts")

import it.krzeminski.githubactions.yaml.writeToFile

listOf(
    buildWorkflow,
    checkIfWrappersUpToDateWorkflow,
    generateWrappersWorkflow,
    gradleWrapperValidationWorkflow,
    updateGradleWrapperWorkflow,
).forEach { it.writeToFile() }
