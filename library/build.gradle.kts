import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    buildsrc.convention.`kotlin-jvm`
    buildsrc.convention.`dsl-publishing`

    kotlin("plugin.serialization")

    // Code quality.
    id("io.gitlab.arturbosch.detekt")
    id("org.jetbrains.kotlinx.binary-compatibility-validator") version "0.12.0"
}

group = "it.krzeminski"
version = "0.30.0"

dependencies {
    implementation("org.snakeyaml:snakeyaml-engine:2.5")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.4.1")

    testImplementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
    testImplementation(kotlin("reflect"))
}

sourceSets {
    main {
        java {
            setSrcDirs(listOf("src/gen/kotlin"))
        }
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += listOf(
            "-opt-in=it.krzeminski.githubactions.internal.InternalGithubActionsApi"
        )
    }
}

kotlin {
    explicitApi()
}

apiValidation {
    ignoredPackages.add("it.krzeminski.githubactions.actions")
}

ktlint {
    filter {
        exclude { it.file.invariantSeparatorsPath.contains("/gen/") }
    }
}

val validateDuplicatedVersion by tasks.creating<Task> {
    doLast {
        require(
            project.rootDir.resolve("mkdocs.yml").readText()
                .contains("  version: $version")
        ) { "Library version stated in the docs should be equal to $version!" }
        require(
            project.rootDir.resolve("script-generator/src/main/kotlin/it/krzeminski/githubactions/scriptgenerator/Version.kt").readText()
                .contains("val LIBRARY_VERSION = \"$version\"")
        ) { "Library version stated in script-generator/.../Version.kt should be equal to $version!" }
    }
}

tasks.check {
    dependsOn(validateDuplicatedVersion)
}
