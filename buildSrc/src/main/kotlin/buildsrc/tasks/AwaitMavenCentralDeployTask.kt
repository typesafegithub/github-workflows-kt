package buildsrc.tasks

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import kotlin.time.minutes
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.publish.plugins.PublishingPlugin
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

abstract class AwaitMavenCentralDeployTask : DefaultTask() {

    @get:Input
    abstract val groupId: Property<String>
    @get:Input
    abstract val artifactId: Property<String>
    @get:Input
    abstract val version: Property<String>

    init {
        group = PublishingPlugin.PUBLISH_TASK_GROUP
    }

    @TaskAction
    fun awaitDeployment(): Unit = runBlocking {
        val queriedUrl = "https://repo1.maven.org/maven2/${groupId.get().replace(".", "/")}/${artifactId.get()}/${version.get()}/"
        logger.lifecycle("Querying URL: $queriedUrl")

        while (!isPresent(queriedUrl)) {
            logger.lifecycle("Library still not present...")
            delay(1.minutes)
        }

        if (isPresent(queriedUrl)) {
            logger.lifecycle("Library present!")
        }
    }

    private fun isPresent(queriedUrl: String): Boolean {
        val request = HttpRequest.newBuilder()
            .uri(URI(queriedUrl))
            .GET()
            .build()
        val response = HttpClient.newHttpClient()
            .send(request, HttpResponse.BodyHandlers.ofString())
        return response.statusCode() != 404
    }
}
