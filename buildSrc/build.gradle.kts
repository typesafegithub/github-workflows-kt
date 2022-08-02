plugins {
    `kotlin-dsl`
    kotlin("jvm") version embeddedKotlinVersion
}

object Versions {
    const val kotlin = "1.7.10"
    const val detekt = "1.21.0"
    const val ktlint = "10.3.0"
    const val nexusPublishPlugin = "1.1.0"
}

dependencies {
    implementation(enforcedPlatform("org.jetbrains.kotlin:kotlin-bom:${Versions.kotlin}"))
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}")
    implementation("org.jetbrains.kotlin:kotlin-serialization")

    implementation("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:${Versions.detekt}")
    implementation("org.jlleitschuh.gradle:ktlint-gradle:${Versions.ktlint}")
    implementation("io.github.gradle-nexus:publish-plugin:${Versions.nexusPublishPlugin}")

    implementation(platform("org.jetbrains.kotlinx:kotlinx-coroutines-bom:1.6.4"))
    implementation(("org.jetbrains.kotlinx:kotlinx-coroutines-core"))
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        freeCompilerArgs += listOf(
            "-opt-in=kotlin.time.ExperimentalTime",
        )
    }
}
