plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

dependencies {
    commonMainImplementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
}

kotlin {
    jvm()
    js(IR) {
        browser()
    }
}
