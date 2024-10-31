plugins {
    buildsrc.convention.`kotlin-jvm`

    kotlin("plugin.serialization")

    application
}

dependencies {
    implementation("com.google.devtools.ksp:symbol-processing-api:2.0.21-1.0.26")
    implementation("com.squareup:kotlinpoet:2.0.0")
    implementation("com.squareup:kotlinpoet-ksp:2.0.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
}

application {
    mainClass.set("io.github.typesafegithub.workflows.codegenerator.GenerationEntryPointKt")
}

tasks.run.configure {
    workingDir(rootProject.layout.projectDirectory)
}
