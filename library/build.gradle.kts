import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jmailen.gradle.kotlinter.tasks.ConfigurableKtLintTask

plugins {
    buildsrc.convention.`kotlin-jvm`
    buildsrc.convention.`dsl-publishing`

    kotlin("plugin.serialization")

    // Code quality.
    id("io.gitlab.arturbosch.detekt")
    id("info.solidsoft.pitest") version "1.9.11"
    id("org.jetbrains.kotlinx.binary-compatibility-validator") version "0.13.2"

    id("org.jetbrains.dokka") version "1.9.0"
}

group = "io.github.typesafegithub"
version = "1.1.0"

dependencies {
    implementation("org.snakeyaml:snakeyaml-engine:2.7")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.6.0")

    testImplementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
    testImplementation("com.github.tschuchortdev:kotlin-compile-testing:1.5.0")
    testImplementation(kotlin("reflect"))
}

sourceSets {
    main {
        java {
            setSrcDirs(listOf("src/gen/kotlin"))
        }
    }
    test {
        java {
            // The integration tests read from and write to there.
            setSrcDirs(listOf("$rootDir/.github/workflows"))
        }
    }
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        freeCompilerArgs.addAll(
            "-opt-in=io.github.typesafegithub.workflows.internal.InternalGithubActionsApi",
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
        // Generated action bindings.
        listOf("io.github.typesafegithub.workflows.actions.*"),
    )
}

tasks.dokkaHtml {
    moduleName.set("github-workflows-kt")

    dokkaSourceSets.configureEach {
        perPackageOption {
            matchingRegex.set("io.github.typesafegithub.workflows.actions.*")
            suppress.set(true)
        }
    }
}
