package io.github.typesafegithub.workflows.dsl.expressions

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import java.io.File

fun main() {
    generateEventPayloads()
}

/*
 * Generate type-safe accessors for GitHub Event payloads
 * The payloads depend on the kind of the event: pull request, push, ...
 *
 * We read event payloads from binding-generate/src/test/resources/payloads
 * We generate it inside       library/src/main/kotlin/io/github/typesafegithub/workflows/expressions/contexts
 *
 * The JSONs come from https://docs.github.com/en/developers/webhooks-and-events/webhooks/webhook-events-and-payloads
 * Feel free to add any payload you might need from that page
 * */
fun generateEventPayloads() {
    println("== generateEventPayloads()")
    println("GitHubContext subclass  will be generated in ${kotlinGenDir.absolutePath}")
    val params =
        listOf(
            PayloadEventParams("PushEvent", "event-push.json"),
            PayloadEventParams("PullRequestEvent", "event-pull-request.json"),
            PayloadEventParams("ReleaseEvent", "event-release.json"),
            PayloadEventParams("WorkflowDispatchEvent", "event-workflow-dispatch.json"),
        )
    params
        .map { it.generateTypesafePayload() }
        .forEach { fileSpec -> fileSpec.writeTo(kotlinGenDir) }
    println("\n")
}

private val EXPRESSIONS = "io.github.typesafegithub.workflows.dsl.expressions"
private val PACKAGE = "$EXPRESSIONS.contexts"
private val resourcesDir = File("automation/code-generator/src/main/resources/payloads")

private val kotlinGenDir = File("library/src/gen/kotlin")

// ClassNames
private val fakeList = ClassName(EXPRESSIONS, "FakeList")
private val listOfStrings = List::class.asClassName().parameterizedBy(String::class.asClassName())

private val annotationFileSuppress =
    AnnotationSpec.builder(Suppress::class)
        .addMember("%S", "ObjectPropertyNaming")
        .build()

private val fileComment = "File auto-generated by :gradlew code-generator:run"

data class PayloadEventParams(
    val className: String,
    val path: String,
) {
    val jsonFile = resourcesDir.resolve(path)

    init {
        check(className == className.toPascalCase())
        check(jsonFile.canRead()) { "Can't read ${jsonFile.canonicalPath}" }
    }
}

fun PayloadEventParams.generateTypesafePayload(): FileSpec {
    println("Parsing ${jsonFile.canonicalPath}")
    val element: JsonObject = Json.parseToJsonElement(jsonFile.readText()) as JsonObject
    val objects: Map<String, JsonObject> = findAllObjects(element, "event")
    val fileSpec = generateObjectTypes(objects, className)
    return fileSpec
}

fun PayloadEventParams.findAllObjects(
    element: JsonObject,
    path: String,
): Map<String, JsonObject> {
    val nothing = emptyMap<String, JsonObject>()

    val result =
        element.flatMap { (subpath, entry) ->
            when (entry) {
                is JsonObject -> findAllObjects(entry, "$path.$subpath")
                is JsonArray -> {
                    (entry.firstOrNull() as? JsonObject)
                        ?.let { firtSchild -> findAllObjects(firtSchild, "$path/$subpath") }
                        ?: nothing
                }
                else -> nothing
            }.toList()
        }.toMap()
    return result + Pair(path, element)
}

fun generateObjectTypes(
    objects: Map<String, JsonObject>,
    filename: String,
): FileSpec {
    val types: List<TypeSpec> =
        objects.map { (key, jsonObject) ->
            jsonObject.generateObjectType(key, filename)
        }
    return FileSpec.builder(PACKAGE, filename)
        .addFileComment(fileComment)
        .addAnnotation(annotationFileSuppress)
        .addTypes(types)
        .build()
}

fun payloadClassName(
    key: String,
    filename: String,
) = key
    .replace("event", filename)
    .replace(".", "_")
    .toPascalCase()

fun JsonObject.generateObjectType(
    key: String,
    filename: String,
): TypeSpec {
    println("Generating class ${payloadClassName(key, filename)} : ExpressionContext(\"github.$key\")")
    val properties = mapNotNull { it.generatePropertySpec(key, filename) }
    return TypeSpec.objectBuilder(payloadClassName(key, filename))
        .addProperties(properties)
        .build()
}

fun Map.Entry<String, JsonElement>.generatePropertySpec(
    key: String,
    filename: String,
): PropertySpec? {
    val (child, element) = this

    return when (element) {
        is JsonPrimitive -> {
            PropertySpec.builder(child, String::class.asClassName())
                .addModifiers(KModifier.CONST)
                .initializer("%S", "github.$key.$child")
                .build()
        }
        is JsonObject ->
            PropertySpec.builder(child, ClassName(PACKAGE, payloadClassName("$key.$child", filename)))
                .initializer("%L", payloadClassName("$key.$child", filename))
                .build()
        is JsonArray -> {
            PropertySpec.builder(child, listOfStrings)
                .initializer("%T(%S)", fakeList, "github.$key.$child")
                .build()
        }
        else -> {
            println("Warning: unhandled $child")
            null
        }
    }
}
