plugins {
    buildsrc.convention.`kotlin-jvm`
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-compiler")
    api(projects.actionBindingGenerator)
    runtimeOnly(projects.githubWorkflowsKt)
}
