plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.6.10"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.charleskorn.kaml:kaml:0.40.0")

    testImplementation("io.kotest:kotest-assertions-core:5.1.0")
    testImplementation("io.kotest:kotest-runner-junit5:5.1.0")
    testImplementation("io.mockk:mockk:1.12.2")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
