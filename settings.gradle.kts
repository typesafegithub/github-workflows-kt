rootProject.name = "github-actions-kotlin-dsl"

include(
    "library",
    "wrapper-generator",
    "script-generator"
)

plugins {
    `gradle-enterprise`
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
