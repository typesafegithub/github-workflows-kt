plugins {
    buildsrc.convention.`kotlin-jvm`
    buildsrc.convention.publishing

    id("org.jetbrains.kotlinx.binary-compatibility-validator") version "0.16.2"
}

group = rootProject.group
version = rootProject.version

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")

    implementation(projects.githubWorkflowsKt)
    implementation(projects.sharedInternal)
    testImplementation("EndBug:add-and-commit:v9")
}

kotlin {
    explicitApi()
}
