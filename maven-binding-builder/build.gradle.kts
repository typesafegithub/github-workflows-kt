plugins {
    buildsrc.convention.`kotlin-jvm`
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-compiler")
    implementation(projects.actionBindingGenerator)
    runtimeOnly(projects.githubWorkflowsKt)
}
