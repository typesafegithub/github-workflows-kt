plugins {
    buildsrc.convention.`kotlin-jvm`
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-compiler")
    api("io.arrow-kt:arrow-core:2.2.2.1")
    api("io.ktor:ktor-client-core:3.4.3")
    api(projects.actionBindingGenerator)
    implementation(projects.sharedInternal)
    implementation("io.github.oshai:kotlin-logging:8.0.01")

    runtimeOnly(projects.githubWorkflowsKt)

    testImplementation("io.ktor:ktor-client-cio:3.4.3")
    testImplementation(projects.testUtils)
}
