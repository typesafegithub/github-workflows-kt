plugins {
    buildsrc.convention.`kotlin-jvm`
    kotlin("plugin.serialization")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-compiler")
    api(projects.actionBindingGenerator)
    implementation(platform("io.ktor:ktor-bom:2.3.10"))
    implementation("io.ktor:ktor-client-core")
    implementation("io.ktor:ktor-client-cio")
    implementation("io.ktor:ktor-client-content-negotiation")
    implementation("io.ktor:ktor-serialization-kotlinx-json")
    implementation("io.opentelemetry.instrumentation:opentelemetry-ktor-2.0:2.3.0-alpha")
    implementation("io.opentelemetry:opentelemetry-sdk:1.37.0")

    runtimeOnly(projects.githubWorkflowsKt)
}
