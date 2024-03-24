plugins {
    kotlin("jvm")
    application
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-compiler:1.9.23")
    implementation(projects.actionBindingGenerator)
    runtimeOnly(projects.githubWorkflowsKt)
}

application {
    mainClass.set("io.github.typesafegithub.workflows.mavenbinding.MavenBindingBuildingKt")
}
