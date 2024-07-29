import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jmailen.gradle.kotlinter.tasks.ConfigurableKtLintTask

plugins {
    buildsrc.convention.`kotlin-jvm`
    buildsrc.convention.publishing
    buildsrc.convention.`duplicate-versions`

    kotlin("plugin.serialization")

    // Code quality.
    id("io.gitlab.arturbosch.detekt")
    id("info.solidsoft.pitest") version "1.15.0"
    id("org.jetbrains.kotlinx.binary-compatibility-validator") version "0.16.2"

    id("org.jetbrains.dokka") version "1.9.20"
}

group = rootProject.group
version = rootProject.version

dependencies {
    implementation("it.krzeminski:snakeyaml-engine-kmp:3.0.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.7.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.1")
    implementation(projects.sharedInternal)

    testImplementation("dev.zacsweers.kctfork:core:0.5.1")
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
