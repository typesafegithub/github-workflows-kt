import org.jlleitschuh.gradle.ktlint.KtlintExtension

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    application

    // Code quality.
    id("org.jlleitschuh.gradle.ktlint")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":wrapper-generator"))
    implementation(project(":library"))
    implementation("com.charleskorn.kaml:kaml:0.45.0")
    implementation("com.squareup:kotlinpoet:1.12.0")

    testImplementation("io.kotest:kotest-assertions-core:5.3.1")
    testImplementation("io.kotest:kotest-runner-junit5:5.3.1")
    implementation(kotlin("reflect"))
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
            "-opt-in=kotlin.RequiresOptIn",
            "-opt-in=it.krzeminski.githubactions.internal.InternalGithubActionsApi"
        )
    }
}
