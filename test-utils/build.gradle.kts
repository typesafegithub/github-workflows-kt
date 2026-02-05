plugins {
    buildsrc.convention.`kotlin-jvm`
}

dependencies {
    implementation(platform("io.kotest:kotest-bom:6.1.3"))
    implementation("io.kotest:kotest-assertions-core")
    implementation("io.kotest:kotest-common")
}
