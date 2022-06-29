package it.krzeminski.githubactions.dsl.expressions

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import it.krzeminski.githubactions.dsl.expressions.PayloadEventParams.Companion.kotlinGenDir
import it.krzeminski.githubactions.wrappergenerator.generation.toPascalCase
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import java.io.File

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
fun generateEventPayloads() {
    println("== generateEventPayloads()")
    println("GitHubContext subclass  will be generated in ${PayloadEventParams.kotlinGenDir.absolutePath}")
    val params = listOf(
        PayloadEventParams("PushEvent", "event-push.json"),
        PayloadEventParams("PullRequestEvent", "event-pull-request.json"),
        PayloadEventParams("ReleaseEvent", "event-release.json"),
        PayloadEventParams("WorkflowDispatchEvent", "event-workflow-dispatch.json"),
    )
    params
        .map { it.generateTypesafePayload() }
        .forEach { fileSpec -> fileSpec.writeTo(PayloadEventParams.kotlinGenDir) }
    println("\n")
}

data class PayloadEventParams(
    val className: String,
    val path: String,
) {
    companion object {
        val EXPRESSIONS = "it.krzeminski.githubactions.dsl.expressions"
        val PACKAGE = "$EXPRESSIONS.contexts"
        val resourcesDir = File("wrapper-generator/src/main/resources/payloads")

        // TODO: doesn't work for library/src/main/kotlin ???
        val kotlinGenDir = File("library/src/main/kotlin")

        // ClassNames
        val expressionContext = ClassName(EXPRESSIONS, "ExpressionContext")
        val fakeList = ClassName(EXPRESSIONS, "FakeList")
        val listOfStrings = List::class.asClassName().parameterizedBy(String::class.asClassName())

        val annotationFileSuppress = AnnotationSpec.builder(Suppress::class)
            .addMember("%S", "ObjectPropertyNaming")
            .build()
    }

    val jsonFile = resourcesDir.resolve(path)

    init {
        check(className == className.toPascalCase())
        check(jsonFile.canRead()) { "Can't read ${jsonFile.canonicalPath}" }
    }

    fun generateTypesafePayload(): FileSpec {
        println("Parsing ${jsonFile.canonicalPath}")
        val element: JsonObject = Json.parseToJsonElement(jsonFile.readText()) as JsonObject
        val objects: Map<String, JsonObject> = findAllObjects(element, "event")
        val fileSpec = generateObjectTypes(objects, className + "Context", packageName = PACKAGE)
        return fileSpec
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
            .addAnnotation(annotationFileSuppress)

        for ((key, value) in objects) {
            fileSpec.addType(generateObjectType(key, value, packageName, filename))
        }
        return fileSpec.build()
    }

    fun payloadClassName(key: String, filename: String) =
        key
            .replace("event", filename)
            .replace(".", "_")
            .toPascalCase()

    fun generateObjectType(key: String, value: JsonObject, packageName: String, filename: String): TypeSpec {
        val builder = TypeSpec.objectBuilder(payloadClassName(key, filename))
        println("Generating class ${payloadClassName(key, filename)} : ExpressionContext(\"github.$key\")")
        builder
            .superclass(expressionContext)
            .addSuperclassConstructorParameter("%S", "github.$key")

        value.forEach { child, element ->
            val propertyName = when (child) {
                "size" -> "length"
                else -> child
            }
            val property = when (element) {
                is JsonPrimitive ->
                    PropertySpec.builder(propertyName, String::class.asClassName())
                        .addModifiers(KModifier.CONST)
                        .initializer("%S", "github.$key.$child")
                        .build()
                is JsonObject ->
                    PropertySpec.builder(child, ClassName(packageName, payloadClassName("$key.$child", filename)))
                        .initializer("%L", payloadClassName("$key.$child", filename))
                        .build()
                is JsonArray -> {
                    PropertySpec.builder(child, listOfStrings)
                        .initializer("%T(%S)", fakeList, "github.$key.$child")
                        .build()
                }
                else -> {
                    println("Warning: unhandled $child"); null
                }
            }
            if (property != null) builder.addProperty(property)
        }
        return builder.build()
    }
}
