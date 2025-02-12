plugins {
    buildsrc.convention.`kotlin-jvm`
    buildsrc.convention.publishing

    kotlin("plugin.serialization")
}

group = rootProject.group
version = rootProject.version

dependencies {
    // we cannot use a BOM due to limitation in kotlin scripting when resolving the transitive KMM variant dependencies
    // note: see https://youtrack.jetbrains.com/issue/KT-67618
    implementation("io.ktor:ktor-client-core:3.1.0")
    implementation("io.ktor:ktor-client-cio:3.1.0")
    implementation("io.ktor:ktor-client-content-negotiation:3.1.0")
    implementation("io.ktor:ktor-serialization-kotlinx-json:3.1.0")
}
