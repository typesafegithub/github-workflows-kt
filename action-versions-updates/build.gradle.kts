plugins {
    buildsrc.convention.`kotlin-jvm`
    buildsrc.convention.publishing
    kotlin("plugin.serialization")

    id("org.jetbrains.kotlinx.binary-compatibility-validator") version "0.14.0"
}

group = rootProject.group
version = rootProject.version

dependencies {
    // jitpack (or scripting?) seems to have issues when dependencies are coming from a bom
    implementation("io.ktor:ktor-client-core:2.3.10")
    implementation("io.ktor:ktor-client-cio:2.3.10")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.10")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.10")

    implementation(projects.githubWorkflowsKt)
}

kotlin {
    explicitApi()
}
