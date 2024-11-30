import org.jmailen.gradle.kotlinter.tasks.ConfigurableKtLintTask

plugins {
    buildsrc.convention.`kotlin-jvm`
    buildsrc.convention.publishing
    kotlin("plugin.serialization")

    id("org.jetbrains.kotlinx.binary-compatibility-validator") version "0.16.3"
}

group = rootProject.group
version = rootProject.version

dependencies {
    implementation("com.squareup:kotlinpoet:2.0.0")
    implementation("it.krzeminski:snakeyaml-engine-kmp:3.0.3")
    implementation("com.charleskorn.kaml:kaml:0.66.0")
    implementation("io.github.oshai:kotlin-logging:7.0.3")
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
