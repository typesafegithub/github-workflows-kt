plugins {
    buildsrc.convention.`kotlin-jvm`
    buildsrc.convention.publishing

    kotlin("plugin.serialization")
    id("io.gitlab.arturbosch.detekt")
}

group = rootProject.group
version = rootProject.version

dependencies {
    api("io.arrow-kt:arrow-core:2.1.2")
    // we cannot use a BOM due to limitation in kotlin scripting when resolving the transitive KMM variant dependencies
    // note: see https://youtrack.jetbrains.com/issue/KT-67618
    api("io.ktor:ktor-client-core:3.2.3")
    implementation("io.ktor:ktor-client-cio:3.2.3")
    implementation("io.ktor:ktor-client-content-negotiation:3.2.3")
    implementation("io.ktor:ktor-client-logging:3.2.3")
    implementation("io.ktor:ktor-serialization-kotlinx-json:3.2.3")
    implementation("io.github.oshai:kotlin-logging:7.0.11")
    implementation("com.auth0:java-jwt:4.5.0")
    implementation("org.kohsuke:github-api:1.329")

    testImplementation("io.kotest.extensions:kotest-extensions-mockserver:1.3.0")
    testImplementation("org.slf4j:slf4j-simple:2.0.17")
}
