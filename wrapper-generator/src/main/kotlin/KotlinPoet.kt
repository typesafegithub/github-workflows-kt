import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asTypeName
import java.io.File

/**
 * Documentation about KotlinPoet is available at https://square.github.io/kotlinpoet/
 */


fun ActionCoords.generateKotlinPoet() {
    val manifest = fetchManifest()
    println(manifest)
    val fileSpec = manifest.generateFileSpec()
    println(fileSpec)
    fileSpec.writeTo(File("library/src/main/kotlin"))
}

fun Manifest.generateFileSpec() =
    FileSpec.builder(coords.ownerPackage(), coords.className())
        .addType(
            TypeSpec.classBuilder(coords.className())
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
        )
        // @file:Suppress("RedundantVisibilityModifier")
        .addAnnotation(AnnotationSpec.builder(Suppress::class).addMember("%S", "RedundantVisibilityModifier").build())
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
        .addCode("return TODO(%S)", "You need to implement toYamlArguments()")
        .build()
}

fun TypeSpec.Builder.inheritsFromAction(manifest: Manifest): TypeSpec.Builder = this
    .superclass(ClassName("it.krzeminski.githubactions.actions", "Action"))
    .addSuperclassConstructorParameter("%S", manifest.coords.owner)
    .addSuperclassConstructorParameter("%S", manifest.coords.name)
    .addSuperclassConstructorParameter("%S", manifest.coords.version)

fun Manifest.primaryConstructor(): FunSpec {
    val parameters = inputs.map {
        ParameterSpec.Companion.builder(camelCase(it.key), typeNullableString)
            .defaultValue(CodeBlock.of("null"))
            .addKdoc(it.value.description).build()
    }
    return FunSpec.constructorBuilder()
        .addParameters(parameters)
        .build()
}

fun Manifest.properties(): List<PropertySpec> {
    val parameters = inputs.map {
        PropertySpec.Companion.builder(camelCase(it.key), typeNullableString)
            .initializer(camelCase(it.key))
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

private fun escapeDefaultValue(s: String) =
    s.trim().removePrefix("\${{").removeSuffix("}}").trim()

fun Manifest.exampleFullAction(): PropertySpec {
    val classname = ClassName(coords.ownerPackage(), coords.className())
    val builder = CodeBlock.builder().add("%T(\n", classname).indent()
    inputs.forEach {
        builder.add(
            "%L = %S,\n",
            camelCase(it.key),
            escapeDefaultValue(it.value.default ?: it.key)
        )
    }
    builder.unindent().add(")")

    return PropertySpec.builder("example_full_action", classname)
        .initializer(builder.build())
        .build()
}

val typeNullableString = String::class.asTypeName().copy(nullable = true)

// TODO: make unit tests pass
fun camelCase(property: String) =
    property.replace("-", "").replace("_", "")