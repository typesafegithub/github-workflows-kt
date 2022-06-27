import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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
    implementation(projects.wrapperGenerator)
    implementation(projects.library)
    implementation("com.charleskorn.kaml:kaml:0.46.0")
    implementation("com.squareup:kotlinpoet:1.12.0")
    implementation(kotlin("reflect"))

    testImplementation(platform("io.kotest:kotest-bom:5.3.2"))
    testImplementation("io.kotest:kotest-assertions-core")
    testImplementation("io.kotest:kotest-runner-junit5")
}

tasks.withType<Test> {
    useJUnitPlatform()
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
        jvmTarget = "11"
        allWarningsAsErrors = true
        freeCompilerArgs += listOf(
            "-opt-in=kotlin.RequiresOptIn",
            "-opt-in=it.krzeminski.githubactions.internal.InternalGithubActionsApi"
        )
    }
}
