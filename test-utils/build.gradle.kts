plugins {
    buildsrc.convention.`kotlin-jvm`
}

dependencies {
    implementation(platform("io.kotest:kotest-bom:6.0.4"))
    implementation("io.kotest:kotest-assertions-core")
    implementation("io.kotest:kotest-common")
}
