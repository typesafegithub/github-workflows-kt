#!/usr/bin/env kotlin
@file:Repository("https://repo.maven.apache.org/maven2/")
@file:DependsOn("io.github.typesafegithub:github-workflows-kt:4.0.0")

@file:Repository("https://bindings.krzeminski.it")
@file:DependsOn("actions:checkout:v7")
@file:DependsOn("gradle:actions__setup-gradle:v6")
@file:DependsOn("JamesIves:github-pages-deploy-action:v4")
@file:DependsOn("fwilhe2:setup-kotlin:v1")

@file:Import("_shared.main.kts")
@file:Import("setup-java.main.kts")
@file:Import("setup-python.main.kts")

import io.github.typesafegithub.workflows.actions.actions.Checkout
import io.github.typesafegithub.workflows.actions.fwilhe2.SetupKotlin
import io.github.typesafegithub.workflows.actions.gradle.ActionsSetupGradle
import io.github.typesafegithub.workflows.actions.jamesives.GithubPagesDeployAction
import io.github.typesafegithub.workflows.domain.RunnerType.UbuntuLatest
import io.github.typesafegithub.workflows.domain.triggers.Push
import io.github.typesafegithub.workflows.dsl.JobBuilder
import io.github.typesafegithub.workflows.dsl.expressions.expr
import io.github.typesafegithub.workflows.dsl.workflow
import io.github.typesafegithub.workflows.yaml.DEFAULT_CONSISTENCY_CHECK_JOB_CONFIG

workflow(
    name = "Release",
    on = listOf(Push(tags = listOf("v*.*.*"))),
    consistencyCheckJobConfig = DEFAULT_CONSISTENCY_CHECK_JOB_CONFIG.copy(
        useLocalBindingsServerAsFallback = true,
        additionalSteps = {
            uses(
                name = "Downgrade Kotlin",
                action = SetupKotlin(
                    // One version before 2.4.0 that contains a bug leading to this failure:
                    //   While analysing .github/workflows/build.main.kts:53:13:
                    //   org.jetbrains.kotlin.utils.exceptions.KotlinIllegalArgumentExceptionWithAttachments:
                    //   Expected FirResolvedTypeRef with ConeKotlinType but was FirUserTypeRefImpl
                    // This downgrade is meant to be a temporary workaround.
                    // See https://github.com/typesafegithub/github-workflows-kt/issues/2348
                    version = "2.3.21",
                ),
            )
        },
    ),
    sourceFile = __FILE__,
    env = mapOf(
        "SIGNING_KEY" to expr("secrets.SIGNING_KEY"),
        "SIGNING_PASSWORD" to expr("secrets.SIGNING_PASSWORD"),
        "ORG_GRADLE_PROJECT_sonatypeUsername" to expr("secrets.ORG_GRADLE_PROJECT_SONATYPEUSERNAME"),
        "ORG_GRADLE_PROJECT_sonatypePassword" to expr("secrets.ORG_GRADLE_PROJECT_SONATYPEPASSWORD"),
    ),
) {
    job(
        id = "release",
        runsOn = UbuntuLatest,
    ) {
        uses(action = Checkout())
        setupJava()
        uses(action = ActionsSetupGradle())
        run(
            name = "Build",
            command = "./gradlew build",
        )

        setupPython()

        // From here, there are steps performing deployments. Before, it's only about building and testing.

        libraries.forEach { library ->
            run(
                name = "Publish '$library' to Sonatype",
                command = "./gradlew $library:publishToSonatype closeAndReleaseSonatypeStagingRepository --no-configuration-cache",
            )
        }

        libraries.forEach { library ->
            run(
                name = "Wait until '$library' present in Maven Central",
                command = "./gradlew $library:waitUntilLibraryPresentInMavenCentral",
            )
        }

        deployDocs()
    }
}

private fun JobBuilder<*>.deployDocs() {
    run(command = "pip install -r docs/requirements.txt")

    val directoryToDeploy = "to-gh-pages"
    run(
        name = "Build Mkdocs docs",
        command = "mkdocs build --site-dir $directoryToDeploy",
    )
    uses(action = ActionsSetupGradle())
    run(
        name = "Generate API docs",
        command = "./gradlew :github-workflows-kt:dokkaGenerate",
    )
    run(
        name = "Prepare target directory for API docs",
        command = "mkdir -p $directoryToDeploy/api-docs",
    )
    run(
        name = "Copy Dokka output to Mkdocs output",
        command = "cp -r github-workflows-kt/build/dokka/html/* $directoryToDeploy/api-docs",
    )
    run(
        name = "Copy teaser image",
        command = "cp images/teaser-with-newest-version.svg $directoryToDeploy"
    )
    uses(
        name = "Deploy merged docs to GitHub Pages",
        action = GithubPagesDeployAction(
            folder = "$directoryToDeploy",
        ),
    )
}
