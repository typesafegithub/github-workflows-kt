import io.ktor.plugin.features.*

plugins {
    buildsrc.convention.`kotlin-jvm-server`
    application
    id("io.ktor.plugin") version "3.3.1"
    id("io.gitlab.arturbosch.detekt")
}

dependencies {
    implementation(platform("io.ktor:ktor-bom:3.3.1"))
    implementation("io.ktor:ktor-server-core")
    implementation("io.ktor:ktor-server-netty")
    implementation("io.ktor:ktor-server-call-logging")
    implementation("io.ktor:ktor-server-call-id")
    implementation("io.ktor:ktor-server-metrics-micrometer")
    implementation("io.micrometer:micrometer-registry-prometheus:1.15.4")

    implementation("com.sksamuel.aedile:aedile-core:3.0.1")
    implementation("io.github.oshai:kotlin-logging:7.0.13")
    implementation(platform("org.apache.logging.log4j:log4j-bom:2.25.2"))
    implementation("org.apache.logging.log4j:log4j-jul")
    runtimeOnly("org.apache.logging.log4j:log4j-core")
    runtimeOnly("org.apache.logging.log4j:log4j-slf4j2-impl")
    runtimeOnly("org.apache.logging.log4j:log4j-jpl")

    implementation(projects.mavenBindingBuilder)
    implementation(projects.sharedInternal)

    testImplementation("io.ktor:ktor-server-test-host")
    testImplementation("io.mockk:mockk:1.14.6")
}

application {
    mainClass.set("io.github.typesafegithub.workflows.jitbindingserver.MainKt")
}

val dockerAppName = "github-workflows-kt-jit-binding-server"

ktor {
    docker {
        localImageName.set(dockerAppName)
        jreVersion.set(JavaVersion.VERSION_21)

        externalRegistry.set(
            DockerImageRegistry.dockerHub(
                appName = provider { dockerAppName },
                username = providers.environmentVariable("DOCKERHUB_USERNAME"),
                password = providers.environmentVariable("DOCKERHUB_PASSWORD"),
            ),
        )
    }
}
