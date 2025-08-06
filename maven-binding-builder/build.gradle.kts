plugins {
    buildsrc.convention.`kotlin-jvm`
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-compiler")
    api("io.arrow-kt:arrow-core:2.1.2")
    api(projects.actionBindingGenerator)
    implementation(projects.sharedInternal)
    implementation("io.github.oshai:kotlin-logging:7.0.12")

    runtimeOnly(projects.githubWorkflowsKt)
}
