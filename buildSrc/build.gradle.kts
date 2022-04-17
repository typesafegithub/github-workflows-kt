import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
    kotlin("jvm") version "1.6.20"
}

object Versions {
    const val jvmTarget = "11"
    const val kotlinTarget = "1.6"

    const val kotlin = "1.6.20"
    const val detekt = "1.20.0"
    const val ktlint = "10.2.1"
}

dependencies {
    implementation(enforcedPlatform("org.jetbrains.kotlin:kotlin-bom:${Versions.kotlin}"))
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}")
    implementation("org.jetbrains.kotlin:kotlin-serialization")

    implementation("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:${Versions.detekt}")
    implementation("org.jlleitschuh.gradle:ktlint-gradle:${Versions.ktlint}")
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = Versions.jvmTarget
        apiVersion = Versions.kotlinTarget
        languageVersion = Versions.kotlinTarget
    }
}

kotlin {
    jvmToolchain {
        (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of(Versions.jvmTarget))
    }

    kotlinDslPluginOptions {
        jvmTarget.set(Versions.jvmTarget)
    }
}
