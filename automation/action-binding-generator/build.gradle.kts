import org.jmailen.gradle.kotlinter.tasks.ConfigurableKtLintTask

plugins {
    buildsrc.convention.`kotlin-jvm`

    id("org.jetbrains.kotlinx.binary-compatibility-validator") version "0.13.2"
}

dependencies {
    implementation(projects.automation.typings)
    implementation(projects.automation.metadataReading)
    implementation(projects.automation.textUtils)
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
