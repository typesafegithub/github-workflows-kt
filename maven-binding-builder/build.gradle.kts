plugins {
    buildsrc.convention.`kotlin-jvm`
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-compiler")
    api("io.arrow-kt:arrow-core:2.2.0")
    api(projects.actionBindingGenerator)
    implementation(projects.sharedInternal)
    implementation("io.github.oshai:kotlin-logging:7.0.13")

    runtimeOnly(projects.githubWorkflowsKt)

    testImplementation(projects.testUtils)
}
