rootProject.name = "github-workflows-kt"

apply(from = "./buildSrc/repositories.settings.gradle.kts")

include(
    "library",
    "wrapper-generator",
    "script-generator"
)

plugins {
    id("com.gradle.enterprise") version "3.12"
}

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage") // Central declaration of repositories is an incubating feature
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
}

gradleEnterprise {
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
        publishAlwaysIf(System.getenv("GITHUB_ACTIONS") == "true")
        publishOnFailure()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
