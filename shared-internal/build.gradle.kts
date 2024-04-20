plugins {
    buildsrc.convention.`kotlin-jvm`
    buildsrc.convention.publishing

    kotlin("plugin.serialization")
}

group = rootProject.group
version = rootProject.version

dependencies {
    implementation(platform("io.ktor:ktor-bom:2.3.10"))
    implementation("io.ktor:ktor-client-core")
    implementation("io.ktor:ktor-client-cio")
    implementation("io.ktor:ktor-client-content-negotiation")
    implementation("io.ktor:ktor-serialization-kotlinx-json")
}
