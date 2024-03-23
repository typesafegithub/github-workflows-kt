plugins {
    kotlin("jvm")
    buildsrc.convention.publishing
}

version = "v4"

dependencies {
    implementation("io.github.typesafegithub:github-workflows-kt:1.12.0")
}
