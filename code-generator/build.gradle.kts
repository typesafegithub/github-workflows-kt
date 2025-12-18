plugins {
    buildsrc.convention.`kotlin-jvm`

    kotlin("plugin.serialization")
}

dependencies {
    implementation("com.google.devtools.ksp:symbol-processing-api:2.3.4")
    implementation("com.squareup:kotlinpoet:2.2.0")
    implementation("com.squareup:kotlinpoet-ksp:2.2.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")
}
