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
    implementation("com.charleskorn.kaml:kaml:0.45.0")
    implementation("com.squareup:kotlinpoet:1.11.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")
    implementation("com.squareup.okhttp3:okhttp:4.10.0")

    testImplementation("io.kotest:kotest-assertions-core:5.3.1")
    testImplementation("io.kotest:kotest-runner-junit5:5.3.1")
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

tasks.register<JavaExec>("suggestVersions") {
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("it.krzeminski.githubactions.wrappergenerator.versions.SuggestVersionsKt")
    dependsOn("compileKotlin")
}

configure<KtlintExtension> {
    filter {
        exclude("**/wrappersfromunittests/**")
    }
}
