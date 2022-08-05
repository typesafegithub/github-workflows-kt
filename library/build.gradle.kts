import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers
import kotlin.time.ExperimentalTime
import kotlin.time.minutes

plugins {
    buildsrc.convention.`kotlin-jvm`

    kotlin("plugin.serialization")

    // Code quality.
    id("io.gitlab.arturbosch.detekt")

    // Publishing.
    `maven-publish`
    signing
}

group = "it.krzeminski"
version = "0.23.0"

dependencies {
    implementation("com.charleskorn.kaml:kaml:0.46.0")
    implementation("org.yaml:snakeyaml:1.30")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.3.3")

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

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += listOf(
            "-opt-in=it.krzeminski.githubactions.internal.InternalGithubActionsApi"
        )
    }
}

ktlint {
    filter {
        exclude { it.file.invariantSeparatorsPath.contains("/gen/") }
    }
}

val githubUser = "krzema12"
val libraryName = "github-actions-kotlin-dsl"

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = libraryName
            from(components["java"])

            pom {
                name.set(libraryName)
                description.set("Authoring GitHub Actions workflows in Kotlin.")
                url.set("https://github.com/$githubUser/$libraryName")

                licenses {
                    license {
                        name.set("Apache License, version 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }

                scm {
                    connection.set("scm:git:git://github.com/$githubUser/$libraryName.git/")
                    developerConnection.set("scm:git:ssh://github.com:$githubUser/$libraryName.git")
                    url.set("https://github.com/$githubUser/$libraryName.git")
                }

                developers {
                    developer {
                        id.set("krzema12")
                        name.set("Piotr Krzemiński")
                        email.set("git@krzeminski.it")
                    }
                }
            }
        }
    }

    val ossrhUsername: String? by project
    val ossrhPassword: String? by project

    repositories {
        maven(url = "https://oss.sonatype.org/service/local/staging/deploy/maven2/") {
            credentials {
                username = ossrhUsername
                password = ossrhPassword
            }
        }
    }
}

tasks {
    signing {
        sign(publishing.publications["mavenJava"])

        val signingKey = System.getenv("SIGNING_KEY")
        val signingPassword = System.getenv("SIGNING_PASSWORD")
        useInMemoryPgpKeys(signingKey, signingPassword)
    }
}

@OptIn(ExperimentalTime::class)
val waitUntilLibraryPresentInMavenCentral by tasks.creating<Task> {
    group = "publishing"
    doLast {
        val queriedUrl = "https://repo1.maven.org/maven2/it/krzeminski/github-actions-kotlin-dsl/$version/"
        println("Querying URL: $queriedUrl")

        fun isPresent(): Boolean {
            val request = HttpRequest.newBuilder()
                .uri(URI(queriedUrl))
                .GET()
                .build()
            val response = HttpClient.newHttpClient()
                .send(request, BodyHandlers.ofString())
            return response.statusCode() != 404
        }

        runBlocking {
            while (!isPresent()) {
                println("Library still not present...")
                delay(1.minutes)
            }

            if (isPresent()) {
                println("Library present!")
            }
        }
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
