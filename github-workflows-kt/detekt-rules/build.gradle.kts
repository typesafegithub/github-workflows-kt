plugins {
    buildsrc.convention.`kotlin-jvm`
}

dependencies {
    compileOnly("io.gitlab.arturbosch.detekt:detekt-api:1.23.8")

    testImplementation("io.gitlab.arturbosch.detekt:detekt-test:1.23.8")
    testImplementation("org.jetbrains.kotlin:kotlin-compiler-embeddable")
}
