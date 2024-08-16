import io.ktor.plugin.features.*

plugins {
    buildsrc.convention.`kotlin-jvm-server`
    application
    id("io.ktor.plugin") version "3.0.3"
}

dependencies {
    implementation(platform("io.ktor:ktor-bom:3.0.3"))
    implementation("io.ktor:ktor-server-core")
    implementation("io.ktor:ktor-server-netty")
    implementation("io.ktor:ktor-client-core")
    implementation("io.ktor:ktor-client-cio")
    implementation("io.ktor:ktor-client-content-negotiation")
    implementation("io.ktor:ktor-serialization-kotlinx-json")
    implementation("io.ktor:ktor-server-call-logging")
    implementation("io.ktor:ktor-server-call-id")
    implementation("it.krzeminski:snakeyaml-engine-kmp:3.0.2")
    implementation("io.opentelemetry.instrumentation:opentelemetry-ktor-3.0:2.10.0-alpha")
    implementation("io.opentelemetry:opentelemetry-sdk:1.46.0")
    implementation("io.opentelemetry:opentelemetry-exporter-otlp:1.46.0")
    implementation("io.opentelemetry:opentelemetry-exporter-logging:1.46.0")
    implementation("io.github.reactivecircus.cache4k:cache4k:0.14.0")
    implementation("io.github.oshai:kotlin-logging:7.0.3")
    implementation(platform("org.apache.logging.log4j:log4j-bom:2.24.3"))
    implementation("org.apache.logging.log4j:log4j-jul")
    runtimeOnly("org.apache.logging.log4j:log4j-core")
    runtimeOnly("org.apache.logging.log4j:log4j-slf4j2-impl")
    runtimeOnly("org.apache.logging.log4j:log4j-jpl")

    implementation(projects.mavenBindingBuilder)
    implementation(projects.sharedInternal)
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
