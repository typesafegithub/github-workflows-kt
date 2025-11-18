package buildsrc.convention

import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    `java-library`
    id("io.kotest")

    // Code quality.
    id("org.jmailen.kotlinter")
}

dependencies {
    testImplementation(platform("io.kotest:kotest-bom:6.0.5"))
    testImplementation("io.kotest:kotest-assertions-core")
    testImplementation("io.kotest:kotest-assertions-table")
    testImplementation("io.kotest:kotest-runner-junit5")
}

java {
    // It's available without extra setup on GitHub Actions runners.
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11

    withJavadocJar()
    withSourcesJar()
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        // It's available without extra setup on GitHub Actions runners.
        jvmTarget.set(JvmTarget.JVM_11)

        allWarningsAsErrors.set(true)

        freeCompilerArgs.addAll(
            "-opt-in=kotlin.ExperimentalStdlibApi",
            "-opt-in=kotlin.time.ExperimentalTime",
        )
    }
}



tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
