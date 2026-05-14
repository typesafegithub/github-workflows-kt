plugins {
    buildsrc.convention.`kotlin-jvm`
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-compiler")
    api("io.arrow-kt:arrow-core:2.2.2.1")
    api("io.ktor:ktor-client-core:3.5.0")
    api(projects.actionBindingGenerator)
    implementation(projects.sharedInternal)
    implementation("io.github.oshai:kotlin-logging:8.0.02")

    runtimeOnly(projects.githubWorkflowsKt)

    testImplementation("io.ktor:ktor-client-cio:3.5.0")
    testImplementation(projects.testUtils)
}
