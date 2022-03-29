import org.jlleitschuh.gradle.ktlint.KtlintExtension

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.6.10"
    application

    // Code quality.
    id("org.jlleitschuh.gradle.ktlint") version "10.2.1"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":wrapper-generator"))
    implementation(project(":library"))
    implementation("com.charleskorn.kaml:kaml:0.43.0")
    implementation("com.squareup:kotlinpoet:1.11.0")

    testImplementation("io.kotest:kotest-assertions-core:5.2.1")
    testImplementation("io.kotest:kotest-runner-junit5:5.2.1")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

application {
    mainClass.set("it.krzeminski.githubactions.scriptgenerator.MainKt")
    tasks.run.get().workingDir = rootProject.projectDir
}

configure<KtlintExtension> {
    filter {
        exclude("**/generated/**", "**/actual/**")
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "11"
        freeCompilerArgs += listOf(
            "-Xopt-in=kotlin.RequiresOptIn",
            "-Xopt-in=it.krzeminski.githubactions.internal.InternalGithubActionsApi"
        )
    }
}
