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
    abstract val queriedUrl: Property<String>

    init {
        group = PublishingPlugin.PUBLISH_TASK_GROUP
    }

    @TaskAction
    fun awaitDeployment(): Unit = runBlocking {
        logger.lifecycle("Querying URL: ${queriedUrl.get()}")

        while (!isPresent()) {
            logger.lifecycle("Library still not present...")
            delay(1.minutes)
        }

        if (isPresent()) {
            logger.lifecycle("Library present!")
        }
    }

    private fun isPresent(): Boolean {
        val request = HttpRequest.newBuilder()
            .uri(URI(queriedUrl.get()))
            .GET()
            .build()
        val response = HttpClient.newHttpClient()
            .send(request, HttpResponse.BodyHandlers.ofString())
        return response.statusCode() != 404
    }
}
