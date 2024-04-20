plugins {
    buildsrc.convention.`kotlin-jvm`
    buildsrc.convention.publishing
    kotlin("plugin.serialization")

    id("org.jetbrains.kotlinx.binary-compatibility-validator") version "0.14.0"
}

group = rootProject.group
version = rootProject.version

dependencies {
    implementation(platform("org.jetbrains.kotlinx:kotlinx-coroutines-bom:1.8.0"))
    implementation(("org.jetbrains.kotlinx:kotlinx-coroutines-core"))

    implementation(projects.githubWorkflowsKt)
    implementation(projects.sharedInternal)
}

kotlin {
    explicitApi()
}
