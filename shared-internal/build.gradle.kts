plugins {
    buildsrc.convention.`kotlin-jvm`
    buildsrc.convention.publishing

    kotlin("plugin.serialization")
    id("io.gitlab.arturbosch.detekt")
}

group = rootProject.group
version = rootProject.version

dependencies {
    api("io.arrow-kt:arrow-core:2.2.1.1")
    // we cannot use a BOM due to limitation in kotlin scripting when resolving the transitive KMM variant dependencies
    // note: see https://youtrack.jetbrains.com/issue/KT-67618
    api("io.ktor:ktor-client-core:3.4.0")
    api("io.micrometer:micrometer-core:1.16.2")
    implementation("io.ktor:ktor-client-cio:3.4.0")
    implementation("io.ktor:ktor-client-content-negotiation:3.4.0")
    implementation("io.ktor:ktor-client-logging:3.4.0")
    implementation("io.ktor:ktor-serialization-kotlinx-json:3.4.0")
    implementation("io.github.oshai:kotlin-logging:7.0.14")
    implementation("com.auth0:java-jwt:4.5.0")
    implementation("org.kohsuke:github-api:1.330")

    testImplementation("io.kotest:kotest-extensions-mockserver:6.1.3")
    testImplementation("org.slf4j:slf4j-simple:2.0.17")
}
