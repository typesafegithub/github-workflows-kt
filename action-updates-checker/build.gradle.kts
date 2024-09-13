plugins {
    buildsrc.convention.`kotlin-jvm`
    buildsrc.convention.publishing

    id("org.jetbrains.kotlinx.binary-compatibility-validator") version "0.16.3"
}

group = rootProject.group
version = rootProject.version

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")

    implementation(projects.githubWorkflowsKt)
    implementation(projects.sharedInternal)
}

kotlin {
    explicitApi()
}
