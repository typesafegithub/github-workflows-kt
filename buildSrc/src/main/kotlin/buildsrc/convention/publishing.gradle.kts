package buildsrc.convention

import buildsrc.config.createGitHubActionsKotlinDslPom

plugins {
    `maven-publish`
    signing
}

val githubUser = "krzema12"
val libraryName = "github-actions-kotlin-dsl"

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = libraryName
            from(components["java"])

            createGitHubActionsKotlinDslPom(
                githubUser = githubUser,
                libraryName = libraryName,
            )
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

signing {
    val signingKey = providers.environmentVariable("SIGNING_KEY")
    val signingPassword = providers.environmentVariable("SIGNING_PASSWORD")

    if (signingKey.isPresent() && signingPassword.isPresent()) {
        useInMemoryPgpKeys(signingKey.get(), signingPassword.get())
    }

    sign(publishing.publications["mavenJava"])
}
