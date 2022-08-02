package buildsrc.config

import org.gradle.api.publish.maven.MavenPom
import org.gradle.api.publish.maven.MavenPublication

/**
 * Create the default GitHub Actions Kotlin DSL Maven POM.
 *
 * @param[configure] additional configuration. Can also be used to override the defaults.
 */
fun MavenPublication.createGitHubActionsKotlinDslPom(
    libraryName: String,
    githubUser: String,
    configure: MavenPom.() -> Unit = {},
) = pom {
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

    configure()
}
