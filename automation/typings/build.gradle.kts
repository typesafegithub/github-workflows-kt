plugins {
    buildsrc.convention.`kotlin-jvm`
    kotlin("plugin.serialization")
}

dependencies {
    implementation("com.charleskorn.kaml:kaml:0.49.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
}
