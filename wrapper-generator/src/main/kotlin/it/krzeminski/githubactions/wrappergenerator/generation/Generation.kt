package it.krzeminski.githubactions.wrappergenerator.generation

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.plusParameter
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import it.krzeminski.githubactions.wrappergenerator.domain.ActionCoords
import it.krzeminski.githubactions.wrappergenerator.domain.typings.StringTyping
import it.krzeminski.githubactions.wrappergenerator.domain.typings.Typing
import it.krzeminski.githubactions.wrappergenerator.metadata.Input
import it.krzeminski.githubactions.wrappergenerator.metadata.Metadata
import it.krzeminski.githubactions.wrappergenerator.metadata.fetchMetadata
import it.krzeminski.githubactions.wrappergenerator.metadata.prettyPrint

data class Wrapper(
    val kotlinCode: String,
    val filePath: String,
)

fun ActionCoords.generateWrapper(
    inputTypings: Map<String, Typing> = emptyMap(),
    fetchMetadataImpl: ActionCoords.() -> Metadata = { fetchMetadata() },
): Wrapper {
    val metadata = fetchMetadataImpl()
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
        .addComment(
            """
            This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
            be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
            generator itself.
            """.trimIndent()
        )
        .addType(generateActionClass(metadata, coords, inputTypings))
        .annotateSuppressDeprecation(metadata)
        .indent("    ")
        .build()
    return buildString {
        fileSpec.writeTo(this)
    }
}

private fun FileSpec.Builder.annotateSuppressDeprecation(metadata: Metadata) = apply {
    val deprecatedIsUsed = metadata.inputs.values.any { it.deprecationMessage.isNullOrBlank().not() }
    if (deprecatedIsUsed) {
        addAnnotation(
            AnnotationSpec.builder(Suppress::class.asClassName())
                .addMember(CodeBlock.of("%S", "DEPRECATION"))
                .build()
        )
    }
}

private fun generateActionClass(metadata: Metadata, coords: ActionCoords, inputTypings: Map<String, Typing>): TypeSpec {
    val actionClassName = coords.buildActionClassName()
    return TypeSpec.classBuilder(actionClassName)
        .addKdoc(actionKdoc(metadata, coords))
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
                .build()
        )
    }
    return this
}

private fun TypeSpec.Builder.addOutputClassIfNecessary(metadata: Metadata): TypeSpec.Builder {
    if (metadata.outputs.isEmpty()) {
        return this
    }

    val stepIdConstructorParameter = ParameterSpec.builder("stepId", String::class)
        .addModifiers(KModifier.PRIVATE)
        .build()
    val stepIdProperty = PropertySpec.builder("stepId", String::class)
        .initializer("stepId")
        .addModifiers(KModifier.PRIVATE)
        .build()
    val propertiesFromOutputs = metadata.outputs.map { (key, value) ->
        PropertySpec.builder(key.toCamelCase(), String::class)
            .initializer("\"steps.\$stepId.outputs.$key\"")
            .addKdoc(value.description)
            .build()
    }
    addType(
        TypeSpec.classBuilder("Outputs")
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameter(stepIdConstructorParameter)
                    .build()
            )
            .addProperties(listOf(stepIdProperty) + propertiesFromOutputs)
            .addFunction(
                FunSpec.builder("get")
                    .addModifiers(KModifier.OPERATOR)
                    .addParameter("outputName", String::class)
                    .addCode(
                        CodeBlock.Builder().apply {
                            add("return \"steps.\$stepId.outputs.\$outputName\"")
                        }.build()
                    )
                    .build()
            )
            .build()
    )

    return this
}

private fun TypeSpec.Builder.addBuildOutputObjectFunctionIfNecessary(metadata: Metadata): TypeSpec.Builder {
    if (metadata.outputs.isEmpty()) {
        return this
    }

    addFunction(
        FunSpec.builder("buildOutputObject")
            .addModifiers(KModifier.OVERRIDE)
            .addParameter("stepId", String::class)
            .addCode(CodeBlock.of("return Outputs(stepId)"))
            .build()
    )

    return this
}

private fun PropertySpec.Builder.annotateDeprecated(input: Input) = apply {
    if (input.deprecationMessage != null) {
        addAnnotation(
            AnnotationSpec.builder(Deprecated::class.asClassName())
                .addMember(CodeBlock.of("%S", input.deprecationMessage))
                .build()
        )
    }
}

private fun Metadata.buildToYamlArgumentsFunction(inputTypings: Map<String, Typing>) =
    FunSpec.builder("toYamlArguments")
        .addModifiers(KModifier.OVERRIDE)
        .addAnnotation(
            AnnotationSpec.builder(Suppress::class)
                .addMember("\"SpreadOperator\"")
                .build()
        )
        .addCode(linkedMapOfInputs(inputTypings))
        .build()

private fun Metadata.linkedMapOfInputs(inputTypings: Map<String, Typing>): CodeBlock {
    if (inputs.isEmpty()) {
        return CodeBlock.Builder()
            .add(CodeBlock.of("return %T<String, String>()", LinkedHashMap::class))
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
            unindent()
            add(").toTypedArray()\n")
            unindent()
            add(")")
        }.build()
    }
}

private fun TypeSpec.Builder.inheritsFromAction(coords: ActionCoords, metadata: Metadata): TypeSpec.Builder {
    val superclass = if (metadata.outputs.isEmpty()) {
        ClassName("it.krzeminski.githubactions.actions", "Action")
    } else {
        ClassName("it.krzeminski.githubactions.actions", "ActionWithOutputs")
            .plusParameter(
                ClassName(
                    "it.krzeminski.githubactions.actions.${coords.owner.toKotlinPackageName()}",
                    "${coords.buildActionClassName()}.Outputs"
                )
            )
    }
    return this
        .superclass(superclass)
        .addSuperclassConstructorParameter("%S", coords.owner)
        .addSuperclassConstructorParameter("%S", coords.name)
        .addSuperclassConstructorParameter("%S", coords.version)
}

private fun Metadata.primaryConstructor(inputTypings: Map<String, Typing>, coords: ActionCoords): FunSpec {
    return FunSpec.constructorBuilder()
        .addParameters(
            inputs.map { (key, input) ->
                ParameterSpec.builder(key.toCamelCase(), inputTypings.getInputType(key, input, coords))
                    .defaultValueIfNullable(input)
                    .addKdoc(input.description)
                    .build()
            }
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
        Action: ${metadata.name}

        ${metadata.description}

        [Action on GitHub](https://github.com/${coords.owner}/${coords.name})
    """.trimIndent()

private fun Map<String, Typing>.getInputTyping(key: String) =
    this[key] ?: StringTyping

private fun Map<String, Typing>.getInputType(key: String, input: Input, coords: ActionCoords) =
    getInputTyping(key).getClassName(coords.owner.toKotlinPackageName(), coords.buildActionClassName())
        .copy(nullable = !input.shouldBeNonNullInWrapper())
