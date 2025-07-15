rootProject.name = "github-workflows-kt-monorepo"

apply(from = "./buildSrc/repositories.settings.gradle.kts")

include(
    "github-workflows-kt",
    "action-binding-generator",
    "action-updates-checker",
    "maven-binding-builder",
    "jit-binding-server",
    "shared-internal",
    "code-generator",
)

plugins {
    id("com.gradle.develocity") version "4.1"
}

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage") // Central declaration of repositories is an incubating feature
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
}

develocity {
    buildScan {
        termsOfUseUrl = "https://gradle.com/terms-of-service"
        termsOfUseAgree = "yes"
        publishing.onlyIf {
            System.getenv("GITHUB_ACTIONS") == "true" && it.buildResult.failures.isNotEmpty()
        }
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
