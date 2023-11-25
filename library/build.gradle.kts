import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jmailen.gradle.kotlinter.tasks.ConfigurableKtLintTask

plugins {
    buildsrc.convention.`kotlin-jvm`
    buildsrc.convention.`dsl-publishing`

    kotlin("plugin.serialization")

    // Code quality.
    id("io.gitlab.arturbosch.detekt")
    id("info.solidsoft.pitest") version "1.15.0"
    id("org.jetbrains.kotlinx.binary-compatibility-validator") version "0.13.2"

    id("org.jetbrains.dokka") version "1.9.10"
}

group = rootProject.group
version = rootProject.version

dependencies {
    implementation("org.snakeyaml:snakeyaml-engine:2.7")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.6.1")

    testImplementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.1")
    testImplementation("dev.zacsweers.kctfork:core:0.4.0")
    testImplementation("com.lemonappdev:konsist:0.13.0")
    // Needed to use the right version of the compiler for the libraries that depend on it.
    testImplementation(kotlin("compiler"))
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
    compilerOptions {
        freeCompilerArgs.addAll(
            "-opt-in=io.github.typesafegithub.workflows.internal.InternalGithubActionsApi",
        )
    }
}

tasks.test {
    // The integration tests read from and write to there.
    inputs.dir("$rootDir/.github/workflows")
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
    // These properties of a project need to be resolved before the 'doLast' block because otherwise, Gradle
    // configuration cache cannot be used.
    val rootDir = rootDir
    val version = version

    doLast {
        require(
            rootDir.resolve("mkdocs.yml").readText()
                .contains("  version: $version")
        ) { "Library version stated in the docs should be equal to $version!" }
        require(
            rootDir.resolve("automation/action-binding-generator/src/main/kotlin/io/github/typesafegithub/workflows/actionbindinggenerator/LibraryVersion.kt").readText()
                .contains("internal const val LIBRARY_VERSION = \"$version\"")
        ) { "Library version stated in automation/action-binding-generator/src/main/.../LibraryVersion.kt should be equal to $version!" }
        require(
            rootDir.resolve("library/src/test/kotlin/io/github/typesafegithub/workflows/docsnippets/GettingStartedSnippets.kt").readText()
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
