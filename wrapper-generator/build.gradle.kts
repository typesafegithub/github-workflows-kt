plugins {
    buildsrc.convention.`kotlin-jvm`
    application
}

dependencies {
    implementation("com.charleskorn.kaml:kaml:0.43.0")
    implementation("com.squareup:kotlinpoet:1.11.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
    implementation("com.squareup.okhttp3:okhttp:4.9.3")

    testImplementation("io.kotest:kotest-assertions-core:5.2.3")
    testImplementation("io.kotest:kotest-runner-junit5:5.2.3")
    testImplementation(projects.library)
}

application {
    mainClass.set("it.krzeminski.githubactions.wrappergenerator.GenerationEntryPointKt")
}

tasks.run.configure {
    workingDir(rootProject.layout.projectDirectory)
}

tasks.register<JavaExec>("suggestVersions") {
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("it.krzeminski.githubactions.wrappergenerator.versions.SuggestVersionsKt")
    dependsOn(tasks.compileKotlin)
}

ktlint {
    filter {
        exclude("**/wrappersfromunittests/**")
    }
}
