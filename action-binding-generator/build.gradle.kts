import org.jmailen.gradle.kotlinter.tasks.ConfigurableKtLintTask
import org.jmailen.gradle.kotlinter.tasks.FormatTask
import org.jmailen.gradle.kotlinter.tasks.LintTask

plugins {
    buildsrc.convention.`kotlin-jvm`
    buildsrc.convention.publishing
    kotlin("plugin.serialization")

    id("org.jetbrains.kotlinx.binary-compatibility-validator") version "0.18.1"
}

group = rootProject.group
version = rootProject.version

dependencies {
    implementation("com.squareup:kotlinpoet:2.2.0")
    implementation("com.charleskorn.kaml:kaml:0.104.0")
    implementation("io.ktor:ktor-client-core:3.4.0")
    implementation("io.ktor:ktor-client-cio:3.4.0")
    implementation("io.github.oshai:kotlin-logging:8.0.01")
    implementation(projects.sharedInternal)

    testImplementation("io.ktor:ktor-client-mock:3.4.0")
    testImplementation(projects.githubWorkflowsKt)
}

kotlin {
    explicitApi()
}

fun ConfigurableKtLintTask.kotlinterConfig() {
    exclude("**/bindingsfromunittests/**")
}

tasks.withType<LintTask> {
    kotlinterConfig()
}

tasks.withType<FormatTask> {
    kotlinterConfig()
}
