import io.ktor.plugin.features.*

plugins {
    buildsrc.convention.`kotlin-jvm`
    application
    id("io.ktor.plugin") version "2.3.9"
}

dependencies {
    implementation(platform("io.ktor:ktor-bom:2.3.9"))
    implementation("io.ktor:ktor-server-core")
    implementation("io.ktor:ktor-server-netty")
    implementation("ch.qos.logback:logback-classic:1.5.3")

    implementation(projects.mavenBindingBuilder)
}

application {
    mainClass.set("io.github.typesafegithub.workflows.jitbindingserver.MainKt")
}

val dockerAppName = "github-workflows-kt-jit-binding-server"

ktor {
    docker {
        localImageName.set(dockerAppName)

        externalRegistry.set(
            DockerImageRegistry.dockerHub(
                appName = provider { dockerAppName },
                username = providers.environmentVariable("DOCKERHUB_USERNAME"),
                password = providers.environmentVariable("DOCKERHUB_PASSWORD"),
            ),
        )
    }
}
