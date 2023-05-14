import org.jmailen.gradle.kotlinter.tasks.ConfigurableKtLintTask

plugins {
    buildsrc.convention.`kotlin-jvm`

    kotlin("plugin.serialization")

    application
}

dependencies {
    implementation("com.charleskorn.kaml:kaml:0.53.0")
    implementation("com.squareup:kotlinpoet:1.13.2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation(projects.automation.typings)

    testImplementation(projects.library)
}

application {
    mainClass.set("io.github.typesafegithub.workflows.wrappergenerator.GenerationEntryPointKt")
}

tasks.run.configure {
    workingDir(rootProject.layout.projectDirectory)
}

tasks.register<JavaExec>("suggestVersions") {
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("io.github.typesafegithub.workflows.wrappergenerator.versions.SuggestVersionsKt")
    workingDir = rootDir
    dependsOn(tasks.compileKotlin)
}

tasks.register<JavaExec>("updateCommitHashes") {
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("io.github.typesafegithub.workflows.wrappergenerator.updating.UpdateCommitHashesKt")
    workingDir = rootDir
    dependsOn(tasks.compileKotlin)
}

fun ConfigurableKtLintTask.kotlinterConfig() {
    exclude("**/wrappersfromunittests/**")
}

tasks.lintKotlinTest {
    kotlinterConfig()
}

tasks.formatKotlinTest {
    kotlinterConfig()
}
