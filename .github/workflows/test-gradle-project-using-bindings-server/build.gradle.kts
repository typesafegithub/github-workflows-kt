import java.net.URI

plugins {
    kotlin("jvm") version "2.2.0"
}

repositories {
    mavenCentral()
    maven {
        url = URI("http://localhost:8080/")
        isAllowInsecureProtocol = true
    }
}

dependencies {
    // Regular, top-level action.
    implementation("actions:checkout:v4")

    // Nested action.
    implementation("gradle:actions__setup-gradle:v3")

    // Using specific version.
    implementation("actions:cache:v3.3.3")

    // Always untyped action.
    implementation("typesafegithub:always-untyped-action-for-tests:v1")
}
