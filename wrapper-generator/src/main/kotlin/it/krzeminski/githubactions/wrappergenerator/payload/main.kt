package it.krzeminski.githubactions.wrappergenerator.payload

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import it.krzeminski.githubactions.wrappergenerator.generation.toCamelCase
import it.krzeminski.githubactions.wrappergenerator.generation.toPascalCase
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import java.io.File

fun main() {
    println("See https://docs.github.com/en/developers/webhooks-and-events/webhooks/webhook-events-and-payloads#pull_request")
    val dir = File("wrapper-generator/src/main/resources/payload")
    val event = dir.resolve("pull_request.json").readText()
    val element: JsonObject = Json.parseToJsonElement(event) as JsonObject
    generateTypeSafePayload(element, "Event")

    val github = dir.resolve("github.json").readText()
    val githubElement: JsonObject = Json.parseToJsonElement(github) as JsonObject
    generateTypeSafePayload(githubElement, "Github")
}

fun generateTypeSafePayload(element: JsonObject, name: String) {
    val objects: Map<String, JsonObject> = findAllObjects(element, path = name.lowercase())
    println("size=${objects.size}")
    objects.forEach { (key, value) ->
        println("[$key] => ${value.toString().take(80)}")
    }
    generateObjectTypes(objects, name).writeTo(File("wrapper-generator/src/main/kotlin"))
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

const val PACKAGE = "it.krzeminski.githubactions.wrappergenerator.payload"

fun generateObjectTypes(objects: Map<String, JsonObject>, filename: String): FileSpec {
    val fileSpec = FileSpec.builder(PACKAGE, filename)
    fileSpec.addProperty(
        PropertySpec.builder(filename.lowercase(), ClassName(PACKAGE, payloadClassName(filename)))
            .initializer(filename)
            .build()
    )
    for ((key, value) in objects) {
        fileSpec.addType(generateObjectType(key, value))
    }
    return fileSpec.build().also { println(it) }
}

fun payloadClassName(key: String) = key.replace(".", "_").toPascalCase()

fun generateObjectType(key: String, value: JsonObject): TypeSpec {
    val builder = TypeSpec.objectBuilder(payloadClassName(key))
    value.forEach { child, element ->
        val property = when (element) {
            is JsonPrimitive ->
                PropertySpec.builder(child.toCamelCase(), String::class.asClassName()).initializer("%S", "$key.$child").build()
            is JsonObject ->
                PropertySpec.builder(child.toCamelCase(), ClassName(PACKAGE, payloadClassName("$key.$child")))
                    .initializer("%L", payloadClassName("$key.$child").toPascalCase())
                    .build()
            else -> { println("Warning: unhandled $child") ; null }
        }
        if (property != null) builder.addProperty(property)
    }
    return builder.build().also { println(it) }
}
