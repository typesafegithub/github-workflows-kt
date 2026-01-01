plugins {
    buildsrc.convention.`kotlin-jvm`
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-compiler")
    api("io.arrow-kt:arrow-core:2.2.1.1")
    api("io.ktor:ktor-client-core:3.3.3")
    api(projects.actionBindingGenerator)
    implementation(projects.sharedInternal)
    implementation("io.github.oshai:kotlin-logging:7.0.14")

    runtimeOnly(projects.githubWorkflowsKt)

    testImplementation("io.ktor:ktor-client-cio:3.3.3")
    testImplementation(projects.testUtils)
}
