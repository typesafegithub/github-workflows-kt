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
    implementation("com.charleskorn.kaml:kaml:0.43.0")
    implementation("com.squareup:kotlinpoet:1.11.0")

    testImplementation("io.kotest:kotest-assertions-core:5.2.1")
    testImplementation("io.kotest:kotest-runner-junit5:5.2.1")
    testImplementation(project(":library"))
}

tasks.withType<Test> {
    useJUnitPlatform()
}

application {
    mainClass.set("it.krzeminski.githubactions.wrappergenerator.GenerationEntryPointKt")
    tasks.run.get().workingDir = rootProject.projectDir
}

tasks.getByName("run") {
    finalizedBy(":library:ktlintFormat")
}

configure<KtlintExtension> {
    filter {
        exclude("**/wrappersfromunittests/**")
    }
}
