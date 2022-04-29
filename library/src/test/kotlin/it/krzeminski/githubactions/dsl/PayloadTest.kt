package it.krzeminski.githubactions.dsl

import io.kotest.assertions.fail
import io.kotest.core.spec.style.FunSpec
import it.krzeminski.githubactions.expr.GitHubContext
import it.krzeminski.githubactions.expr.JobContext
import it.krzeminski.githubactions.expr.RunnerContext
import it.krzeminski.githubactions.expr.StrategyContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import java.io.File
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties

class PayloadTest : FunSpec({
    val payloads = File("src/test/resources/payloads")

    fun KClass<*>.properties(): List<String> =
        declaredMemberProperties.map { it.name }.sorted()

    test("Runner context") {
        val context = RunnerContext::class
        val file = payloads.resolve("runner.json")
        val jsonObject = Json.parseToJsonElement(file.readText()).jsonObject

        context.properties() shouldMatch jsonObject.keys.sorted()
    }

    test("Job context") {
        val optionalProperties = listOf("container", "services")

        val file = payloads.resolve("job.json")
        val context = JobContext::class
        val jsonObject = Json.parseToJsonElement(file.readText()).jsonObject

        context.properties() shouldMatch (jsonObject.keys.sorted() + optionalProperties)
    }

    test("Strategy context") {
        val file = payloads.resolve("strategy.json")
        val context = StrategyContext::class

        val jsonObject = Json.parseToJsonElement(file.readText()).jsonObject
        context.properties() shouldMatch jsonObject.keys.sorted()
    }

    test("github context - without github.event") {
        val unsupportedProperties = listOf("event")

        val context = GitHubContext::class
        val file = payloads.resolve("github-push.json")
        val jsonObject = Json.parseToJsonElement(file.readText()).jsonObject

        context.properties() shouldMatch (jsonObject.keys.sorted() - unsupportedProperties)
    }
})

infix fun List<String>.shouldMatch(other: List<String>) {
    if (this.toSet() != other.toSet()) {
        fail("""
                |The lists don't match
                |Missing:    ${(other - toSet()).distinct().sorted()}
                |Unexpected: ${(this - other.toSet()).distinct().sorted()}
            """.trimMargin())
    }
}