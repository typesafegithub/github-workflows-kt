import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    buildsrc.convention.`kotlin-jvm`

    kotlin("plugin.serialization")

    application
}

dependencies {
    implementation(projects.wrapperGenerator)
    implementation(projects.library)
    implementation("com.charleskorn.kaml:kaml:0.46.0")
    implementation("com.squareup:kotlinpoet:1.12.0")
    implementation(kotlin("reflect"))

    testImplementation(platform("io.kotest:kotest-bom:5.4.1"))
    testImplementation("io.kotest:kotest-assertions-core")
    testImplementation("io.kotest:kotest-runner-junit5")
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

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        freeCompilerArgs += listOf(
            "-opt-in=it.krzeminski.githubactions.internal.InternalGithubActionsApi"
        )
    }
}
