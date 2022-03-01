plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.6.10"
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.charleskorn.kaml:kaml:0.41.0")
    implementation("com.squareup:kotlinpoet:1.10.2")

    testImplementation("io.kotest:kotest-assertions-core:5.1.0")
    testImplementation("io.kotest:kotest-runner-junit5:5.1.0")
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