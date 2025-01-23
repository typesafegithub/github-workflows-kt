import org.jmailen.gradle.kotlinter.tasks.ConfigurableKtLintTask

plugins {
    buildsrc.convention.`kotlin-jvm`
    buildsrc.convention.publishing
    kotlin("plugin.serialization")

    id("org.jetbrains.kotlinx.binary-compatibility-validator") version "0.17.0"
}

group = rootProject.group
version = rootProject.version

dependencies {
    implementation("com.squareup:kotlinpoet:2.0.0")
    implementation("com.charleskorn.kaml:kaml:0.67.0")
    implementation("io.github.oshai:kotlin-logging:7.0.3")
    implementation(projects.sharedInternal)

    testImplementation("io.kotest:kotest-framework-datatest")
    testImplementation(kotlin("reflect"))
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
