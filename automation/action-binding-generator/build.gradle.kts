import org.jmailen.gradle.kotlinter.tasks.ConfigurableKtLintTask

plugins {
    buildsrc.convention.`kotlin-jvm`
    buildsrc.convention.`action-binding-generator-publishing`
    kotlin("plugin.serialization")

    id("org.jetbrains.kotlinx.binary-compatibility-validator") version "0.13.2"
}

group = "io.github.typesafegithub"
version = "1.3.2-SNAPSHOT"

dependencies {
    implementation("com.squareup:kotlinpoet:1.14.2")
    implementation("com.charleskorn.kaml:kaml:0.55.0")

    testImplementation(projects.library)
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
