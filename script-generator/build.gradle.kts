import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    buildsrc.convention.`kotlin-jvm`

    kotlin("plugin.serialization")

    application
}

dependencies {
    implementation(projects.wrapperGenerator)
    implementation(projects.library)
    implementation("com.charleskorn.kaml:kaml:0.47.0")
    implementation("com.squareup:kotlinpoet:1.12.0")
    implementation(kotlin("reflect"))
}

application {
    mainClass.set("it.krzeminski.githubactions.scriptgenerator.MainKt")
    tasks.run.get().workingDir = rootProject.projectDir
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
