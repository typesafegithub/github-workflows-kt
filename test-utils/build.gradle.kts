plugins {
    buildsrc.convention.`kotlin-jvm`
}

dependencies {
    implementation(platform("io.kotest:kotest-bom:5.9.1"))
    implementation("io.kotest:kotest-assertions-core")
    implementation("io.kotest:kotest-common")
}
