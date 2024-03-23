plugins {
    kotlin("jvm")
    buildsrc.convention.publishing
}

version = "0.0.1"

dependencies {
    implementation("io.github.typesafegithub:github-workflows-kt:1.12.0")
}
