package it.krzeminski.githubactions.dsl.expressions.contexts

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import it.krzeminski.githubactions.dsl.expressions.ExpressionContext
import it.krzeminski.githubactions.dsl.expressions.FakeList
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import java.io.File
import java.util.Locale

/*
 * Generate type-safe accessors for GitHub Event payloads
 * The payloads depend on the kind of the event: pull request, push, ...
 *
 * We read event payloads from library/src/test/resources/payloads
 * We generate it inside       library/src/gen/kotlin/it/krzeminski/githubactions/expr
 *
 * The JSONs come from https://docs.github.com/en/developers/webhooks-and-events/webhooks/webhook-events-and-payloads
 * Feel free to add any payload you might need from that page
 * */
class GenerateEventPayloadTest : FunSpec({

    test("event push payload") {
        EventParams("PushEvent", "event-push.json").generateTypesafePayload()
    }

    test("event pull request payload") {
        EventParams("PullRequestEvent", "event-pull-request.json").generateTypesafePayload()
    }

    test("event release") {
        EventParams("ReleaseEvent", "event-release.json").generateTypesafePayload()
    }

    test("event workflow dispatch") {
        EventParams("WorkflowDispatchEvent", "event-workflow-dispatch.json").generateTypesafePayload()
    }
})

data class EventParams(
    val className: String,
    val path: String,
) {
    val jsonFile = File("src/test/resources/payloads/$path")
    init {
        className shouldBe className.toPascalCase()
        withClue("Can't read ${jsonFile.canonicalPath}") {
            jsonFile.canRead() shouldBe true
        }
    }

    fun generateTypesafePayload() {
        val element: JsonObject = Json.parseToJsonElement(jsonFile.readText()) as JsonObject
        val objects: Map<String, JsonObject> = findAllObjects(element, "event")
        objects.forEach { (key, value) ->
            println("[$key] => ${value.toString().take(80)}")
        }
        generateObjectTypes(objects, className + "Context", packageName = GitHubContext::class.java.packageName)
            .writeTo(File("src/gen/kotlin"))
    }
}

fun findAllObjects(element: JsonObject, path: String): Map<String, JsonObject> {
    val result = mutableMapOf<String, JsonObject>()
    for ((subpath, entry) in element) {
        if (entry is JsonObject) {
            result += findAllObjects(entry, "$path.$subpath")
        } else if (entry is JsonArray) {
            val firtSchild = entry.firstOrNull() as? JsonObject ?: continue
            result += findAllObjects(firtSchild, "$path/$subpath")
        }
    }
    result[path] = element
    return result
}

fun generateObjectTypes(objects: Map<String, JsonObject>, filename: String, packageName: String): FileSpec {
    val fileSpec = FileSpec.builder(packageName, filename)
    for ((key, value) in objects) {
        fileSpec.addType(generateObjectType(key, value, packageName, filename))
    }
    return fileSpec.build().also { println(it) }
}

fun generateObjectType(key: String, value: JsonObject, packageName: String, filename: String): TypeSpec {
    fun payloadClassName(key: String) =
        key
            .replace("event", filename)
            .replace(".", "_")
            .toPascalCase()

    val builder = TypeSpec.objectBuilder(payloadClassName(key))

    builder
        .superclass(ExpressionContext::class.asClassName())
        .addSuperclassConstructorParameter("%S", "github.$key")

    value.forEach { child, element ->
        val propertyName = when (child) {
            "size" -> "length"
            else -> child
        }
        val property = when (element) {
            is JsonPrimitive ->
                PropertySpec.builder(propertyName, String::class.asClassName())
                    .initializer("%S", "github.$key.$child")
                    .build()
            is JsonObject ->
                PropertySpec.builder(child, ClassName(packageName, payloadClassName("$key.$child")))
                    .initializer("%L", payloadClassName("$key.$child"))
                    .build()
            is JsonArray ->
                PropertySpec.builder(child, List::class.asClassName().parameterizedBy(String::class.asClassName()))
                    .initializer("%T(%S)", FakeList::class.asClassName(), "github.$key.$child")
                    .build()
            else -> { println("Warning: unhandled $child") ; null }
        }
        if (property != null) builder.addProperty(property)
    }
    return builder.build().also { println(it) }
}

fun String.toPascalCase(): String {
    val hasOnlyUppercases = none { it in 'a'..'z' }
    val normalizedString = if (hasOnlyUppercases) lowercase() else this
    return normalizedString.replace("+", "-plus-")
        .split("-", "_", " ")
        .joinToString("") {
            it.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        }
}

fun String.toCamelCase(): String =
    toPascalCase().replaceFirstChar { it.lowercase() }
