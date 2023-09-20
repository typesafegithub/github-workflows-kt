plugins {
    buildsrc.convention.`kotlin-jvm`
    kotlin("plugin.serialization")
}

dependencies {
    implementation(projects.automation.typings)
    implementation("com.charleskorn.kaml:kaml:0.55.0")
}
