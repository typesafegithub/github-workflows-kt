plugins {
    buildsrc.convention.`kotlin-jvm`

    kotlin("plugin.serialization")

    application
}

dependencies {
    implementation("com.squareup:kotlinpoet:1.18.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.1")
}

application {
    mainClass.set("io.github.typesafegithub.workflows.codegenerator.GenerationEntryPointKt")
}

tasks.run.configure {
    workingDir(rootProject.layout.projectDirectory)
}
