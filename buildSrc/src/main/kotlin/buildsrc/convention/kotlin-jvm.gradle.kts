package buildsrc.convention

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    `java-library`

    // Code quality.
    id("org.jlleitschuh.gradle.ktlint")
}

dependencies {
    testImplementation(platform("io.kotest:kotest-bom:5.4.1"))
    testImplementation("io.kotest:kotest-assertions-core")
    testImplementation("io.kotest:kotest-runner-junit5")
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

java {
    withJavadocJar()
    withSourcesJar()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        apiVersion = "1.5"
        languageVersion = "1.7"
        jvmTarget = "11"

        allWarningsAsErrors = true

        freeCompilerArgs += listOf(
            "-opt-in=kotlin.ExperimentalStdlibApi",
            "-opt-in=kotlin.time.ExperimentalTime",
        )
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
