plugins {
    buildsrc.convention.`kotlin-jvm`
    application
}

dependencies {
    implementation(projects.scriptGenerator.logic)
}

application {
    mainClass.set("io.github.typesafegithub.workflows.scriptgenerator.MainKt")
    tasks.run.get().workingDir = rootProject.projectDir
}
