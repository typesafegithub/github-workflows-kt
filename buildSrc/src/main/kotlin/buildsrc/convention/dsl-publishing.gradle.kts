package buildsrc.convention

import buildsrc.tasks.AwaitMavenCentralDeployTask

plugins {
    `maven-publish`
    signing
}

val githubUser = "typesafegithub"
val repositoryName = "github-workflows-kt"
val mavenLibraryName = repositoryName

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = mavenLibraryName
            from(components["java"])

            pom {
                name.set(mavenLibraryName)
                description.set("Authoring GitHub Actions workflows in Kotlin.")
                url.set("https://github.com/$githubUser/$repositoryName")

                licenses {
                    license {
                        name.set("Apache License, version 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }

                scm {
                    connection.set("scm:git:git://github.com/$githubUser/$repositoryName.git/")
                    developerConnection.set("scm:git:ssh://github.com:$githubUser/$repositoryName.git")
                    url.set("https://github.com/$githubUser/$repositoryName.git")
                }

                developers {
                    developer {
                        id.set(githubUser)
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
        maven(url = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/") {
            credentials {
                username = ossrhUsername
                password = ossrhPassword
            }
        }
    }
}

signing {
    sign(publishing.publications["mavenJava"])

    val signingKey = System.getenv("SIGNING_KEY")
    val signingPassword = System.getenv("SIGNING_PASSWORD")
    useInMemoryPgpKeys(signingKey, signingPassword)
}

val waitUntilLibraryPresentInMavenCentral by tasks.registering(AwaitMavenCentralDeployTask::class) {
    this.groupId.set(project.group.toString())
    this.artifactId.set(mavenLibraryName)
    this.version.set(project.version.toString())
}
