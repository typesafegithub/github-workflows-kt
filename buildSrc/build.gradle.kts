import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
    kotlin("jvm") version "2.3.10"
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom:2.3.10"))

    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:2.3.10")
    implementation("org.jetbrains.kotlin:kotlin-serialization:2.3.10")
    implementation("io.kotest:kotest-framework-plugin-gradle:6.1.3")

    implementation("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.23.8")
    implementation("org.jmailen.gradle:kotlinter-gradle:5.4.2")

    implementation(platform("org.jetbrains.kotlinx:kotlinx-coroutines-bom:1.10.2"))
    implementation(("org.jetbrains.kotlinx:kotlinx-coroutines-core"))
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        freeCompilerArgs.addAll(
            "-opt-in=kotlin.time.ExperimentalTime",
        )
    }
}
