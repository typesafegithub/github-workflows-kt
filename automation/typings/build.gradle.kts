plugins {
    buildsrc.convention.`kotlin-jvm`
    kotlin("plugin.serialization")
}

dependencies {
    implementation("com.charleskorn.kaml:kaml:0.54.0")
}
