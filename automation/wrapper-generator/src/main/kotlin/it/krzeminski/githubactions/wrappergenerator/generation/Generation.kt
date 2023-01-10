package it.krzeminski.githubactions.wrappergenerator.generation

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.plusParameter
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.asTypeName
import it.krzeminski.githubactions.wrappergenerator.domain.ActionCoords
import it.krzeminski.githubactions.wrappergenerator.domain.typings.StringTyping
import it.krzeminski.githubactions.wrappergenerator.domain.typings.Typing
import it.krzeminski.githubactions.wrappergenerator.generation.Properties.CUSTOM_INPUTS
import it.krzeminski.githubactions.wrappergenerator.generation.Properties.CUSTOM_VERSION
import it.krzeminski.githubactions.wrappergenerator.metadata.Input
import it.krzeminski.githubactions.wrappergenerator.metadata.Metadata
import it.krzeminski.githubactions.wrappergenerator.metadata.fetchMetadata
import it.krzeminski.githubactions.wrappergenerator.metadata.prettyPrint

data class Wrapper(
    val kotlinCode: String,
    val filePath: String,
)

object Types {
    val mapStringString = Map::class.asTypeName().parameterizedBy(String::class.asTypeName(), String::class.asTypeName())
    val nullableString = String::class.asTypeName().copy(nullable = true)
    val mapToList = MemberName("kotlin.collections", "toList")
    val listToArray = MemberName("kotlin.collections", "toTypedArray")
}

object Properties {
    val CUSTOM_INPUTS = "_customInputs"
    val CUSTOM_VERSION = "_customVersion"
}

fun ActionCoords.generateWrapper(
    inputTypings: Map<String, Typing> = emptyMap(),
    fetchMetadataImpl: ActionCoords.() -> Metadata = { fetchMetadata() },
): Wrapper {
    require(this.version.removePrefix("v").toIntOrNull() != null) {
        "Only major versions are supported, and '${this.version}' was given!"
    }

    val metadata = fetchMetadataImpl().removeDeprecatedInputsIfNameClash()
    checkPropertiesAreValid(metadata, inputTypings)
    metadata.suggestAdditionalTypings(inputTypings.keys)?.let { formatSuggestions ->
        println("$prettyPrint I suggest the following typings:\n$formatSuggestions")
    }

    val actionWrapperSourceCode = generateActionWrapperSourceCode(metadata, this, inputTypings)
    return Wrapper(
        kotlinCode = actionWrapperSourceCode,
        filePath = "library/src/gen/kotlin/it/krzeminski/githubactions/actions/${owner.toKotlinPackageName()}/${this.buildActionClassName()}.kt",
    )
}

private fun Metadata.removeDeprecatedInputsIfNameClash(): Metadata {
    val newInputs = this.inputs.entries
        .groupBy { (originalKey, _) -> originalKey.toCamelCase() }
        .mapValues { (_, clashingInputs) ->
            clashingInputs
                .find { it.value.deprecationMessage == null }
                ?: clashingInputs.first()
        }.values.associateBy({ it.key }, { it.value })
    return this.copy(inputs = newInputs)
}

private fun checkPropertiesAreValid(metadata: Metadata, inputTypings: Map<String, Typing>) {
    val invalidProperties = inputTypings.keys - metadata.inputs.keys
    require(invalidProperties.isEmpty()) {
        """
            Request contains invalid properties:
            Available: ${metadata.inputs.keys}
            Invalid:   $invalidProperties
        """.trimIndent()
    }
}

private fun generateActionWrapperSourceCode(metadata: Metadata, coords: ActionCoords, inputTypings: Map<String, Typing>): String {
    val fileSpec = FileSpec.builder("it.krzeminski.githubactions.actions.${coords.owner.toKotlinPackageName()}", coords.buildActionClassName())
        .addFileComment(
            """
            This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
            be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
            generator itself.
            """.trimIndent(),
        )
        .addType(generateActionClass(metadata, coords, inputTypings))
        .annotateSuppressDeprecation(metadata, coords)
        .indent("    ")
        .build()
    return buildString {
        fileSpec.writeTo(this)
    }
}

private fun FileSpec.Builder.annotateSuppressDeprecation(metadata: Metadata, coords: ActionCoords) = apply {
    val isDeprecatedInputUsed = metadata.inputs.values.any { it.deprecationMessage.isNullOrBlank().not() }
    if (isDeprecatedInputUsed || coords.deprecatedByVersion != null) {
        addAnnotation(
            AnnotationSpec.builder(Suppress::class.asClassName())
                .addMember(CodeBlock.of("%S", "DEPRECATION"))
                .build(),
        )
    }
}

private fun generateActionClass(metadata: Metadata, coords: ActionCoords, inputTypings: Map<String, Typing>): TypeSpec {
    val actionClassName = coords.buildActionClassName()
    return TypeSpec.classBuilder(actionClassName)
        .addKdoc(actionKdoc(metadata, coords))
        .addMaybeDeprecated(coords)
        .inheritsFromAction(coords, metadata)
        .primaryConstructor(metadata.primaryConstructor(inputTypings, coords))
        .properties(metadata, coords, inputTypings)
        .addFunction(metadata.buildToYamlArgumentsFunction(inputTypings))
        .addCustomTypes(inputTypings.values.toSet(), coords)
        .addOutputClassIfNecessary(metadata)
        .addBuildOutputObjectFunctionIfNecessary(metadata)
        .build()
}

private fun TypeSpec.Builder.addCustomTypes(typings: Set<Typing>, coords: ActionCoords): TypeSpec.Builder {
    typings
        .mapNotNull { it.buildCustomType(coords) }
        .forEach { addType(it) }
    return this
}

private fun TypeSpec.Builder.properties(metadata: Metadata, coords: ActionCoords, inputTypings: Map<String, Typing>): TypeSpec.Builder {
    metadata.inputs.forEach { (key, input) ->
        addProperty(
            PropertySpec.builder(key.toCamelCase(), inputTypings.getInputType(key, input, coords))
                .initializer(key.toCamelCase())
                .annotateDeprecated(input)
                .build(),
        )
    }
    addProperty(PropertySpec.builder(CUSTOM_INPUTS, Types.mapStringString).initializer(CUSTOM_INPUTS).build())
    return this
}

private fun TypeSpec.Builder.addOutputClassIfNecessary(metadata: Metadata): TypeSpec.Builder {
    if (metadata.outputs.isEmpty()) {
        return this
    }

    val stepIdConstructorParameter = ParameterSpec.builder("stepId", String::class)
        .build()
    val stepIdProperty = PropertySpec.builder("stepId", String::class)
        .initializer("stepId")
        .addModifiers(KModifier.PRIVATE)
        .build()
    val propertiesFromOutputs = metadata.outputs.map { (key, value) ->
        PropertySpec.builder(key.toCamelCase(), String::class)
            .initializer("\"steps.\$stepId.outputs.$key\"")
            // Replacing: working around a bug in Kotlin: https://youtrack.jetbrains.com/issue/KT-52940
            .addKdoc(value.description.replace("/*", "/ *"))
            .build()
    }
    addType(
        TypeSpec.classBuilder("Outputs")
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameter(stepIdConstructorParameter)
                    .build(),
            )
            .addProperties(listOf(stepIdProperty) + propertiesFromOutputs)
            .addFunction(
                FunSpec.builder("get")
                    .addModifiers(KModifier.OPERATOR)
                    .returns(String::class)
                    .addParameter("outputName", String::class)
                    .addCode(
                        CodeBlock.Builder().apply {
                            add("return \"steps.\$stepId.outputs.\$outputName\"")
                        }.build(),
                    )
                    .build(),
            )
            .build(),
    )

    return this
}

private fun TypeSpec.Builder.addBuildOutputObjectFunctionIfNecessary(metadata: Metadata): TypeSpec.Builder {
    if (metadata.outputs.isEmpty()) {
        return this
    }

    addFunction(
        FunSpec.builder("buildOutputObject")
            .returns(ClassName("", "Outputs"))
            .addModifiers(KModifier.OVERRIDE)
            .addParameter("stepId", String::class)
            .addCode(CodeBlock.of("return Outputs(stepId)"))
            .build(),
    )

    return this
}

private fun PropertySpec.Builder.annotateDeprecated(input: Input) = apply {
    if (input.deprecationMessage != null) {
        addAnnotation(
            AnnotationSpec.builder(Deprecated::class.asClassName())
                .addMember(CodeBlock.of("%S", input.deprecationMessage))
                .build(),
        )
    }
}

private fun Metadata.buildToYamlArgumentsFunction(inputTypings: Map<String, Typing>) =
    FunSpec.builder("toYamlArguments")
        .addModifiers(KModifier.OVERRIDE)
        .returns(LinkedHashMap::class.parameterizedBy(String::class, String::class))
        .addAnnotation(
            AnnotationSpec.builder(Suppress::class)
                .addMember("\"SpreadOperator\"")
                .build(),
        )
        .addCode(linkedMapOfInputs(inputTypings))
        .build()

private fun Metadata.linkedMapOfInputs(inputTypings: Map<String, Typing>): CodeBlock {
    if (inputs.isEmpty()) {
        return CodeBlock.Builder()
            .add(CodeBlock.of("return %T($CUSTOM_INPUTS)", LinkedHashMap::class))
            .build()
    } else {
        return CodeBlock.Builder().apply {
            add("return linkedMapOf(\n")
            indent()
            add("*listOfNotNull(\n")
            indent()
            inputs.forEach { (key, value) ->
                val asStringCode = inputTypings.getInputTyping(key).asString()
                if (!value.shouldBeNonNullInWrapper()) {
                    add("%N?.let { %S to it$asStringCode },\n", key.toCamelCase(), key)
                } else {
                    add("%S to %N$asStringCode,\n", key, key.toCamelCase())
                }
            }
            add("*$CUSTOM_INPUTS.%M().%M(),\n", Types.mapToList, Types.listToArray)
            unindent()
            add(").toTypedArray()\n")
            unindent()
            add(")")
        }.build()
    }
}

private fun TypeSpec.Builder.addMaybeDeprecated(coords: ActionCoords): TypeSpec.Builder {
    if (coords.deprecatedByVersion == null) {
        return this
    }
    val newerClass = coords.copy(version = coords.deprecatedByVersion)
    addAnnotation(
        AnnotationSpec.builder(Deprecated::class)
            .addMember("message = %S", "This action has a newer major version: ${newerClass.buildActionClassName()}")
            .addMember("replaceWith = ReplaceWith(%S)", newerClass.buildActionClassName())
            .build(),
    )
    return this
}
private fun TypeSpec.Builder.inheritsFromAction(coords: ActionCoords, metadata: Metadata): TypeSpec.Builder {
    val superclass = if (metadata.outputs.isEmpty()) {
        ClassName("it.krzeminski.githubactions.actions", "Action")
    } else {
        ClassName("it.krzeminski.githubactions.actions", "ActionWithOutputs")
            .plusParameter(
                ClassName(
                    "it.krzeminski.githubactions.actions.${coords.owner.toKotlinPackageName()}",
                    "${coords.buildActionClassName()}.Outputs",
                ),
            )
    }
    return this
        .superclass(superclass)
        .addSuperclassConstructorParameter("%S", coords.owner)
        .addSuperclassConstructorParameter("%S", coords.name)
        .addSuperclassConstructorParameter("_customVersion ?: %S", coords.version)
}

private fun Metadata.primaryConstructor(inputTypings: Map<String, Typing>, coords: ActionCoords): FunSpec {
    return FunSpec.constructorBuilder()
        .addParameters(
            inputs.map { (key, input) ->
                ParameterSpec.builder(key.toCamelCase(), inputTypings.getInputType(key, input, coords))
                    .defaultValueIfNullable(input)
                    // Replacing: working around a bug in Kotlin: https://youtrack.jetbrains.com/issue/KT-52940
                    .addKdoc(input.description.replace("/*", "/ *"))
                    .build()
            }.plus(
                ParameterSpec.builder(CUSTOM_INPUTS, Types.mapStringString)
                    .defaultValue("mapOf()")
                    .addKdoc("Type-unsafe map where you can put any inputs that are not yet supported by the wrapper")
                    .build(),
            ).plus(
                ParameterSpec.builder(CUSTOM_VERSION, Types.nullableString)
                    .defaultValue("null")
                    .addKdoc(
                        "Allows overriding action's version, for example to use a specific minor version, " +
                            "or a newer version that the wrapper doesn't yet know about",
                    )
                    .build(),
            ),
        )
        .build()
}

private fun ParameterSpec.Builder.defaultValueIfNullable(input: Input): ParameterSpec.Builder {
    if (!input.shouldBeNonNullInWrapper()) {
        defaultValue("null")
    }
    return this
}

private fun actionKdoc(metadata: Metadata, coords: ActionCoords) =
    """
       |Action: ${metadata.name}
       |
       |${metadata.description}
       |
       |[Action on GitHub](https://github.com/${coords.owner}/${coords.name})
    """.trimMargin()

private fun Map<String, Typing>.getInputTyping(key: String) =
    this[key] ?: StringTyping

private fun Map<String, Typing>.getInputType(key: String, input: Input, coords: ActionCoords) =
    getInputTyping(key).getClassName(coords.owner.toKotlinPackageName(), coords.buildActionClassName())
        .copy(nullable = !input.shouldBeNonNullInWrapper())
