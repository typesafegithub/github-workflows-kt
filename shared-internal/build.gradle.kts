plugins {
    buildsrc.convention.`kotlin-jvm`
    buildsrc.convention.publishing

    kotlin("plugin.serialization")
    id("io.gitlab.arturbosch.detekt")
}

group = rootProject.group
version = rootProject.version

dependencies {
    api("io.arrow-kt:arrow-core:2.0.1")
    // we cannot use a BOM due to limitation in kotlin scripting when resolving the transitive KMM variant dependencies
    // note: see https://youtrack.jetbrains.com/issue/KT-67618
    api("io.ktor:ktor-client-core:3.1.2")
    implementation("io.ktor:ktor-client-cio:3.1.2")
    implementation("io.ktor:ktor-client-content-negotiation:3.1.2")
    implementation("io.ktor:ktor-client-logging:3.1.2")
    implementation("io.ktor:ktor-serialization-kotlinx-json:3.1.2")
    implementation("io.github.oshai:kotlin-logging:7.0.6")
    implementation("com.auth0:java-jwt:4.5.0")
    implementation("org.kohsuke:github-api:2.0-rc.1")
    
    // It's a workaround for a problem with Kotlin Scripting, and how it resolves
    // conflicting versions: https://youtrack.jetbrains.com/issue/KT-69145
    // As of adding it, ktor-serialization-kotlinx-json depends on kotlinx-io-core:0.4.0,
    // and because of how Scripting resolves the libraries, 0.4.0 is used in the script,
    // leading to a runtime failure of not being able to find a class or a function.
    // I'm bumping kotlinx-io to 0.6.0 in kotlinx.serialization here: https://github.com/Kotlin/kotlinx.serialization/pull/2933
    // Here's a ticket to remember to remove this workaround: https://github.com/typesafegithub/github-workflows-kt/issues/1832
    runtimeOnly("org.jetbrains.kotlinx:kotlinx-io-core:0.7.0")

    testImplementation("io.ktor:ktor-client-mock:3.1.2")
}
