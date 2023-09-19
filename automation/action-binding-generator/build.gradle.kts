import org.jmailen.gradle.kotlinter.tasks.ConfigurableKtLintTask

plugins {
    buildsrc.convention.`kotlin-jvm`

    kotlin("plugin.serialization")
}

dependencies {
    implementation(projects.automation.typings)
    implementation("com.squareup:kotlinpoet:1.14.2")
    implementation("com.charleskorn.kaml:kaml:0.55.0")

    testImplementation(projects.library)
}

fun ConfigurableKtLintTask.kotlinterConfig() {
    exclude("**/wrappersfromunittests/**")
}

tasks.lintKotlinTest {
    kotlinterConfig()
}

tasks.formatKotlinTest {
    kotlinterConfig()
}
