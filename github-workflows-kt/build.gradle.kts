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
    id("org.jetbrains.kotlinx.binary-compatibility-validator") version "0.16.3"

    id("org.jetbrains.dokka") version "1.9.20"
}

group = rootProject.group
version = rootProject.version

dependencies {
    implementation("it.krzeminski:snakeyaml-engine-kmp:3.0.2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
    implementation(projects.sharedInternal)

    testImplementation("dev.zacsweers.kctfork:core:0.5.1")
    // Needed to use the right version of the compiler for the libraries that depend on it.
    testImplementation(kotlin("compiler"))
    testImplementation(kotlin("reflect"))

    // GitHub action bindings
    testImplementation("gradle:actions__setup-gradle:v4")
    testImplementation("actions:setup-java:v4")
    testImplementation("actions:upload-artifact:v3")
    testImplementation("aws-actions:configure-aws-credentials:v4")
    testImplementation("EndBug:add-and-commit:v9")
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

    // It's a workaround to be able to use action bindings provided by the server. They declare a dependency on
    // github-workflows-kt, and I think it causes some kind of version clash (e.g. between 2.3.0 and 2.3.1-SNAPSHOT).
    dependsOn(tasks.jar)
}

kotlin {
    explicitApi()
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
}

tasks.dokkaHtml {
    moduleName.set("github-workflows-kt")
}
