package buildsrc.convention

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    `java-library`

    // Code quality.
    id("org.jmailen.kotlinter")
}

dependencies {
    testImplementation(platform("io.kotest:kotest-bom:5.7.2"))
    testImplementation("io.kotest:kotest-assertions-core")
    testImplementation("io.kotest:kotest-runner-junit5")
}

java {
    withJavadocJar()
    withSourcesJar()

    toolchain {
        requiredJdkVersion()
    }
}

kotlin {
    jvmToolchain {
        requiredJdkVersion()
    }
}

fun JavaToolchainSpec.requiredJdkVersion() {
    languageVersion.set(JavaLanguageVersion.of(11))
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        // It's available without extra setup on GitHub Actions runners.
        jvmTarget = "11"

        allWarningsAsErrors = true
    }

    compilerOptions {
        freeCompilerArgs.addAll(
            "-opt-in=kotlin.ExperimentalStdlibApi",
            "-opt-in=kotlin.time.ExperimentalTime",
        )
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
