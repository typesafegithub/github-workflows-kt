plugins {
    buildsrc.convention.`kotlin-jvm`
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-compiler")
    api(projects.actionBindingGenerator)
    implementation(projects.sharedInternal)

    runtimeOnly(projects.githubWorkflowsKt)
}
