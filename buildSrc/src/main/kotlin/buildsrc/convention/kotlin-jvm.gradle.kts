package buildsrc.convention

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("buildsrc.convention.subproject")
    kotlin("jvm")
    `java-library`

    kotlin("plugin.serialization")

    // Code quality.
    id("org.jlleitschuh.gradle.ktlint")
}

kotlin {
    jvmToolchain {
        (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of(11))
    }
}

java {
    withJavadocJar()
    withSourcesJar()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "11"
        apiVersion = "1.6"
        languageVersion = "1.6"
    }

    kotlinOptions.freeCompilerArgs += listOf(
        "-opt-in=kotlin.RequiresOptIn",
        "-opt-in=kotlin.ExperimentalStdlibApi",
        "-opt-in=kotlin.time.ExperimentalTime",
    )
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
