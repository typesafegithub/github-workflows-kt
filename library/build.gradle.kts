import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jmailen.gradle.kotlinter.tasks.ConfigurableKtLintTask

plugins {
    buildsrc.convention.`kotlin-jvm`
    buildsrc.convention.`dsl-publishing`

    kotlin("plugin.serialization")

    // Code quality.
    id("io.gitlab.arturbosch.detekt")
    id("info.solidsoft.pitest") version "1.9.11"
    id("org.jetbrains.kotlinx.binary-compatibility-validator") version "0.13.0"

    id("org.jetbrains.dokka") version "1.8.10"
}

group = "io.github.typesafegithub"
version = "0.40.1"

dependencies {
    implementation("org.snakeyaml:snakeyaml-engine:2.6")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.5.0")

    testImplementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
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
            "-opt-in=io.github.typesafegithub.workflows.internal.InternalGithubActionsApi"
        )
    }
}

kotlin {
    explicitApi()
}

apiValidation {
    ignoredPackages.add("io.github.typesafegithub.workflows.actions")
}

fun ConfigurableKtLintTask.kotlinterConfig() {
    exclude { it.file.invariantSeparatorsPath.contains("/gen/") }
}

tasks.lintKotlinMain {
    kotlinterConfig()
}
tasks.formatKotlinMain {
    kotlinterConfig()
}

val validateDuplicatedVersion by tasks.creating<Task> {
    doLast {
        require(
            project.rootDir.resolve("mkdocs.yml").readText()
                .contains("  version: $version")
        ) { "Library version stated in the docs should be equal to $version!" }
        require(
            project.rootDir.resolve("script-generator/src/main/kotlin/io/github/typesafegithub/workflows/scriptgenerator/Version.kt").readText()
                .contains("val LIBRARY_VERSION = \"$version\"")
        ) { "Library version stated in script-generator/.../Version.kt should be equal to $version!" }
        require(
            project.file("src/test/kotlin/io/github/typesafegithub/workflows/docsnippets/GettingStartedSnippets.kt").readText()
                .contains("\"io.github.typesafegithub:github-workflows-kt:$version\"")
        ) { "Library version stated in library/src/test/.../GettingStarted.kt should be equal to $version!" }
    }
}

tasks.check {
    dependsOn(validateDuplicatedVersion)
}

pitest {
    junit5PluginVersion.set("1.1.0")
    excludedClasses.set(
        // Generated action wrappers.
        listOf("io.github.typesafegithub.workflows.actions.*"),
    )
}

tasks.dokkaHtml {
    moduleName.set("GitHub Workflows Kt")

    dokkaSourceSets.configureEach {
        perPackageOption {
            matchingRegex.set("io.github.typesafegithub.workflows.actions.*")
            suppress.set(true)
        }
    }
}
