import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    buildsrc.convention.`kotlin-jvm`
    buildsrc.convention.publishing

    // Code quality.
    id("io.gitlab.arturbosch.detekt")
}

dependencies {
    testImplementation("io.kotest:kotest-assertions-core:5.2.3")
    testImplementation("io.kotest:kotest-runner-junit5:5.2.3")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.3.2")
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
        allWarningsAsErrors = true
        freeCompilerArgs += listOf(
            "-opt-in=it.krzeminski.githubactions.internal.InternalGithubActionsApi"
        )
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
