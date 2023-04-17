plugins {
    kotlin("plugin.serialization")
    buildsrc.convention.`kotlin-jvm`
    application
}

dependencies {
    implementation(projects.scriptGenerator.logic)

    implementation(platform("io.ktor:ktor-bom:2.2.4"))
    implementation("io.ktor:ktor-server-core")
    implementation("io.ktor:ktor-server-netty")
    implementation("io.ktor:ktor-server-content-negotiation")
    implementation("io.ktor:ktor-serialization-kotlinx-json")
    implementation("org.slf4j:slf4j-api:2.0.6")
    implementation("org.slf4j:slf4j-simple:2.0.6")
}

application {
    mainClass.set("io.github.typesafegithub.workflows.scriptgenerator.rest.MainKt")
}
