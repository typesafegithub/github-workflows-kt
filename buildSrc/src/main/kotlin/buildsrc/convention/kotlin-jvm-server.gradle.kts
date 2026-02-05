package buildsrc.convention

import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    `java-library`

    // Code quality.
    id("org.jmailen.kotlinter")
}

dependencies {
    testImplementation(platform("io.kotest:kotest-bom:6.1.3"))
    testImplementation("io.kotest:kotest-assertions-core")
    testImplementation("io.kotest:kotest-runner-junit5")
}

java {
    withJavadocJar()
    withSourcesJar()
}

tasks.withType<KotlinCompile> {
    compilerOptions {
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
