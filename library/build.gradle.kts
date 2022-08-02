import buildsrc.tasks.AwaitMavenCentralDeployTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    buildsrc.convention.`kotlin-jvm`
    buildsrc.convention.publishing

    kotlin("plugin.serialization")

    // Code quality.
    id("io.gitlab.arturbosch.detekt")
    id("org.jlleitschuh.gradle.ktlint")
}

dependencies {
    implementation("com.charleskorn.kaml:kaml:0.46.0")
    implementation("org.yaml:snakeyaml:1.30")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.3.3")

    testImplementation(platform("io.kotest:kotest-bom:5.4.1"))
    testImplementation("io.kotest:kotest-assertions-core")
    testImplementation("io.kotest:kotest-runner-junit5")
    testImplementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")
    testImplementation(kotlin("reflect"))
}

sourceSets {
    main {
        java {
            setSrcDirs(listOf("src/gen/kotlin"))
        }
    }
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        freeCompilerArgs += listOf(
            "-opt-in=it.krzeminski.githubactions.internal.InternalGithubActionsApi"
        )
    }
}

val waitUntilLibraryPresentInMavenCentral by tasks.registering(AwaitMavenCentralDeployTask::class) {
    queriedUrl.set("https://repo1.maven.org/maven2/it/krzeminski/github-actions-kotlin-dsl/$version/")
}

val validateDuplicatedVersion by tasks.registering {
    val mkdocsYml = rootProject.layout.projectDirectory.file("mkdocs.yml")
    val versionKt = rootProject.layout.projectDirectory.file(
        "script-generator/src/main/kotlin/it/krzeminski/githubactions/scriptgenerator/Version.kt"
    )

    inputs.file(mkdocsYml)
    inputs.file(versionKt)

    doLast {
        require(
            mkdocsYml.asFile.readText()
                .contains("  version: $version")
        ) { "Library version stated in the docs should be equal to $version!" }
        require(
            versionKt.asFile.readText()
                .contains("""val LIBRARY_VERSION = "$version"""")
        ) { "Library version stated in script-generator/.../Version.kt should be equal to $version!" }
    }
}

tasks.check {
    dependsOn(validateDuplicatedVersion)
}
