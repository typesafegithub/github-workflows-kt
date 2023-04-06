import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jmailen.gradle.kotlinter.tasks.ConfigurableKtLintTask

plugins {
    buildsrc.convention.`kotlin-jvm`

    kotlin("plugin.serialization")

    application
}

dependencies {
    implementation(projects.automation.typings)
    implementation(projects.automation.wrapperGenerator)
    implementation(projects.library)
    implementation("com.charleskorn.kaml:kaml:0.53.0")
    implementation("com.squareup:kotlinpoet:1.13.0")
    implementation(kotlin("reflect"))
}

application {
    mainClass.set("io.github.typesafegithub.workflows.scriptgenerator.MainKt")
    tasks.run.get().workingDir = rootProject.projectDir
}

fun ConfigurableKtLintTask.kotlinterConfig() {
    exclude("**/generated/**", "**/actual/**")
}

tasks.lintKotlinTest {
    kotlinterConfig()
}
tasks.formatKotlinTest {
    kotlinterConfig()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += listOf(
            "-opt-in=io.github.typesafegithub.workflows.internal.InternalGithubActionsApi"
        )
    }
}
