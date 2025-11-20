import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jmailen.gradle.kotlinter.tasks.ConfigurableKtLintTask
import org.jmailen.gradle.kotlinter.tasks.FormatTask
import org.jmailen.gradle.kotlinter.tasks.LintTask

plugins {
    buildsrc.convention.`kotlin-jvm`
    buildsrc.convention.publishing
    buildsrc.convention.`duplicate-versions`

    kotlin("plugin.serialization")
    id("com.google.devtools.ksp") version "2.3.3"

    // Code quality.
    id("io.gitlab.arturbosch.detekt")
    id("org.jetbrains.kotlinx.binary-compatibility-validator") version "0.18.1"

    id("org.jetbrains.dokka") version "2.1.0"
}

group = rootProject.group
version = rootProject.version

dependencies {
    implementation("it.krzeminski:snakeyaml-engine-kmp:4.0.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")
    implementation(projects.sharedInternal)
    ksp(projects.codeGenerator)

    testImplementation("dev.zacsweers.kctfork:core:0.11.1")
    // Needed to use the right version of the compiler for the libraries that depend on it.
    testImplementation(kotlin("compiler"))
    testImplementation(kotlin("reflect"))
    testImplementation(projects.testUtils)

    // GitHub action bindings
    testImplementation("actions:checkout:v4")
    testImplementation("actions:deploy-pages:v4")
    testImplementation("actions:setup-java:v4")
    testImplementation("actions:upload-artifact:v3")
    testImplementation("aws-actions:configure-aws-credentials:v4")
    testImplementation("EndBug:add-and-commit:v9")
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

ksp {
    arg("library-version", project.version.toString())
}

fun ConfigurableKtLintTask.kotlinterConfig() {
    exclude { it.file.invariantSeparatorsPath.contains("/generated/") }
}

tasks.withType<LintTask> {
    kotlinterConfig()
}

tasks.withType<FormatTask> {
    kotlinterConfig()
}

dokka {
    moduleName.set("github-workflows-kt")
}
