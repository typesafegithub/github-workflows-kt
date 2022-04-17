import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    buildsrc.convention.`kotlin-jvm`

    kotlin("plugin.serialization")
    application
}

dependencies {
    implementation(projects.wrapperGenerator)
    implementation(projects.library)
    implementation("com.charleskorn.kaml:kaml:0.43.0")
    implementation("com.squareup:kotlinpoet:1.11.0")

    testImplementation("io.kotest:kotest-assertions-core:5.2.3")
    testImplementation("io.kotest:kotest-runner-junit5:5.2.3")
}

application {
    mainClass.set("it.krzeminski.githubactions.scriptgenerator.MainKt")
}

tasks.run.configure {
    workingDir(rootProject.layout.projectDirectory)
}

ktlint {
    filter {
        exclude("**/generated/**", "**/actual/**")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += listOf(
            "-opt-in=it.krzeminski.githubactions.internal.InternalGithubActionsApi"
        )
    }
}
