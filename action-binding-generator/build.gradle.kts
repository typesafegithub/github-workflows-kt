import org.jmailen.gradle.kotlinter.tasks.ConfigurableKtLintTask

plugins {
    buildsrc.convention.`kotlin-jvm`
    buildsrc.convention.publishing
    kotlin("plugin.serialization")

    id("org.jetbrains.kotlinx.binary-compatibility-validator") version "0.16.0"
}

group = rootProject.group
version = rootProject.version

dependencies {
    implementation("com.squareup:kotlinpoet:1.18.1")
    implementation("com.charleskorn.kaml:kaml:0.60.0")
    implementation(projects.sharedInternal)

    testImplementation(projects.githubWorkflowsKt)
}

kotlin {
    explicitApi()
}

fun ConfigurableKtLintTask.kotlinterConfig() {
    exclude("**/bindingsfromunittests/**")
}

tasks.lintKotlinTest {
    kotlinterConfig()
}

tasks.formatKotlinTest {
    kotlinterConfig()
}
