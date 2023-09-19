plugins {
    buildsrc.convention.`kotlin-jvm`

    kotlin("plugin.serialization")

    application
}

dependencies {
    implementation("com.charleskorn.kaml:kaml:0.55.0")
    implementation("com.squareup:kotlinpoet:1.14.2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")

    implementation(platform("io.ktor:ktor-bom:2.3.4"))
    implementation("io.ktor:ktor-client-core")
    implementation("io.ktor:ktor-client-cio")
    implementation("io.ktor:ktor-client-content-negotiation")
    implementation("io.ktor:ktor-serialization-kotlinx-json")

    implementation(projects.automation.typings)
    implementation(projects.automation.wrapperGenerator)

    testImplementation(projects.library)
}

application {
    mainClass.set("io.github.typesafegithub.workflows.codegenerator.GenerationEntryPointKt")
}

tasks.run.configure {
    workingDir(rootProject.layout.projectDirectory)
}

tasks.register<JavaExec>("suggestVersions") {
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("io.github.typesafegithub.workflows.codegenerator.versions.SuggestVersionsKt")
    workingDir = rootDir
    dependsOn(tasks.compileKotlin)
}

tasks.register<JavaExec>("createActionUpdatePRs") {
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("io.github.typesafegithub.workflows.codegenerator.updating.CreateActionUpdatePRsKt")
    workingDir = rootDir
    dependsOn(tasks.compileKotlin)
}
