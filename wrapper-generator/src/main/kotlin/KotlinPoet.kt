import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asTypeName
import java.io.File

/**
 * Documentation about KotlinPoet is available at https://square.github.io/kotlinpoet/
 */


fun ActionCoords.generateKotlinPoet() {
    val manifest = fetchManifest()
    val fileSpec = manifest.generateFileSpec()
    fileSpec.writeTo(File("library/src/main/kotlin"))
    val testSpec = manifest.generateTestFileSpec()
    testSpec.writeTo(File("library/src/test/kotlin"))

    val allInputs = manifest.inputs.toList().joinToString("") { "\n    ${it.first} => ${it.second}" }
    println("""
        |Inputs:$allInputs
        |URL:          ${manifest.coords.manifestUri}
        |Coords:       ${manifest.coords}
        |Manifest:     name='${manifest.name}' author='${manifest.author}' description='${manifest.description}'
        |Action Class: ${manifest.coords.className()}     written to library/src/main/kotlin
        |Test   Class: ${manifest.coords.className()}Test written to library/src/test/kotlin
        """.trimMargin())
}

fun Manifest.generateFileSpec() =
    FileSpec.builder(coords.ownerPackage(), coords.className())
        .allowPublicMethods()
        .indent(DEFAULT_INDENT)
        .addType(generateActionClass())
        .build()

const val DEFAULT_INDENT = "    "

/**
 * @file:Suppress("RedundantVisibilityModifier")
 */
fun FileSpec.Builder.allowPublicMethods() =
    addAnnotation(AnnotationSpec.builder(Suppress::class).addMember("%S", "RedundantVisibilityModifier").build())


fun Manifest.generateActionClass(): TypeSpec = TypeSpec.classBuilder(coords.className())
    .addKdoc(actionKdoc())
    .addModifiers(KModifier.DATA)
    .inheritsFromAction(this)
    .addProperties(properties())
    .primaryConstructor(primaryConstructor())
    .addFunction(toYamlFunction())
    .addType( // companion object
        TypeSpec.companionObjectBuilder()
            .addModifiers(KModifier.INTERNAL)
            .addProperty(exampleFullAction())
            .addProperty(exampleFullMap())
            .build()
    )
    .build()


fun Manifest.actionKdoc() = """
                    Action $name
                    
                    $description
                    
                    http://github.com/${coords.owner}/${coords.name}
                """.trimIndent()

fun Manifest.toYamlFunction(): FunSpec {
    return FunSpec.builder("toYamlArguments")
        .returns(LinkedHashMap::class.parameterizedBy(String::class, String::class))
        .addModifiers(KModifier.OVERRIDE)
        .addCode(toYamlFunctionParameters())
        .build()
}

fun Manifest.toYamlFunctionParameters(): CodeBlock {
    val builder = CodeBlock.Builder()
        .add("return yamlOf(\n")
        .indent()
    inputs.forEach {
        builder.add("%S to %L,\n", it.key, camelCase(it.key))
    }
    return builder.unindent().add(")").build()
}

fun TypeSpec.Builder.inheritsFromAction(manifest: Manifest): TypeSpec.Builder = this
    .superclass(ClassName("it.krzeminski.githubactions.actions", "Action"))
    .addSuperclassConstructorParameter("%S", manifest.coords.owner)
    .addSuperclassConstructorParameter("%S", manifest.coords.name)
    .addSuperclassConstructorParameter("%S", manifest.coords.version)

fun Manifest.primaryConstructor(): FunSpec {
    val parameters = inputs.map { (key, input) ->
        ParameterSpec.Companion.builder(camelCase(key), input.guessPropertyType())
            .defaultValue(CodeBlock.of("null"))
            .addKdoc(input.description).build()
    }
    return FunSpec.constructorBuilder()
        .addParameters(parameters)
        .build()
}

// TODO: fix unit tests
fun Input.guessPropertyType(): TypeName {
    val tryConvertInt = (default ?: "").toIntOrNull()

    return when (default) {
        null -> typeNullableString
        "false", "true" -> typeNullableBoolean
        else -> if (tryConvertInt == null) typeNullableString else typeNullableInteger
    }
}

val typeNullableString = String::class.asTypeName().copy(nullable = true)
val typeNullableInteger = Integer::class.asTypeName().copy(nullable = true)
val typeNullableBoolean = Boolean::class.asTypeName().copy(nullable = true)


fun Manifest.properties(): List<PropertySpec> {
    val parameters = inputs.map { (key, input) ->
        PropertySpec.Companion.builder(camelCase(key), input.guessPropertyType())
            .initializer(camelCase(key))
            .build()
    }
    return parameters
}

fun Manifest.exampleFullMap(): PropertySpec {
    val mapType = Map::class.parameterizedBy(String::class, String::class)
    val builder = CodeBlock.builder().add("mapOf(\n").indent()
    inputs.forEach {
        builder.add("%S to %S,\n", it.key, escapeDefaultValue(it.value.default ?: it.key))
    }
    builder.unindent().add(")")
    return PropertySpec.Companion.builder("example_full_map", mapType)
        .initializer(builder.build())
        .build()
}

private fun escapeDefaultValue(s: String): String {
    return s.trim().removePrefix("\${{").removeSuffix("}}").trim()
}

fun Manifest.exampleFullAction(): PropertySpec {
    val classname = ClassName(coords.ownerPackage(), coords.className())
    val builder = CodeBlock.builder()
        .add("%T(\n", classname).indent()
    inputs.forEach { (key, input) ->
        val litteralOrString = if (input.guessPropertyType() == typeNullableString) "%S" else "%L"
        builder.add(
            "%L = $litteralOrString,\n",
            camelCase(key),
            escapeDefaultValue(input.default ?: key)
        )
    }
    builder.unindent().add(")")

    return PropertySpec.builder("example_full_action", classname)
        .initializer(builder.build())
        .build()
}

// TODO: make unit tests pass
fun camelCase(property: String) =
    property.replace("-", "").replace("_", "")