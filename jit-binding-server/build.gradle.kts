import io.ktor.plugin.features.*

plugins {
    buildsrc.convention.`kotlin-jvm-server`
    application
    id("io.ktor.plugin") version "3.4.0"
    id("io.gitlab.arturbosch.detekt")
}

dependencies {
    implementation(platform("io.ktor:ktor-bom:3.4.0"))
    implementation("io.ktor:ktor-server-core")
    implementation("io.ktor:ktor-server-netty")
    implementation("io.ktor:ktor-server-call-logging")
    implementation("io.ktor:ktor-server-call-id")
    implementation("io.ktor:ktor-server-metrics-micrometer")
    implementation("io.ktor:ktor-client-core")
    implementation("io.ktor:ktor-client-cio")
    implementation("io.micrometer:micrometer-registry-prometheus:1.16.3")

    implementation("com.sksamuel.aedile:aedile-core:3.0.2")
    implementation("io.github.oshai:kotlin-logging:8.0.01")
    implementation(platform("org.apache.logging.log4j:log4j-bom:2.25.3"))
    implementation("org.apache.logging.log4j:log4j-jul")
    runtimeOnly("org.apache.logging.log4j:log4j-core")
    runtimeOnly("org.apache.logging.log4j:log4j-slf4j2-impl")
    runtimeOnly("org.apache.logging.log4j:log4j-jpl")

    implementation(projects.mavenBindingBuilder)
    implementation(projects.sharedInternal)

    testImplementation("io.ktor:ktor-server-test-host")
    testImplementation("io.mockk:mockk:1.14.9")
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
