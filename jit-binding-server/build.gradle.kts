import io.ktor.plugin.features.*

plugins {
    buildsrc.convention.`kotlin-jvm-server`
    application
    id("io.ktor.plugin") version "2.3.10"
}

dependencies {
    implementation(platform("io.ktor:ktor-bom:2.3.10"))
    implementation("io.ktor:ktor-server-core")
    implementation("io.ktor:ktor-server-netty")
    implementation("io.ktor:ktor-client-core")
    implementation("io.ktor:ktor-client-cio")
    implementation("io.ktor:ktor-client-content-negotiation")
    implementation("io.ktor:ktor-serialization-kotlinx-json")
    implementation("io.opentelemetry.instrumentation:opentelemetry-ktor-2.0:2.3.0-alpha")
    implementation("io.opentelemetry:opentelemetry-sdk:1.37.0")
    implementation("io.opentelemetry:opentelemetry-exporter-otlp:1.37.0")
    implementation("io.opentelemetry:opentelemetry-exporter-logging:1.37.0")
    implementation("io.github.reactivecircus.cache4k:cache4k:0.13.0")
    implementation("ch.qos.logback:logback-classic:1.5.5")

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
