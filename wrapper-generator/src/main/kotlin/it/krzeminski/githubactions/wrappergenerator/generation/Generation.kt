package it.krzeminski.githubactions.wrappergenerator.generation

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import it.krzeminski.githubactions.wrappergenerator.domain.ActionCoords
import it.krzeminski.githubactions.wrappergenerator.domain.WrapperRequest
import it.krzeminski.githubactions.wrappergenerator.domain.typings.StringTyping
import it.krzeminski.githubactions.wrappergenerator.domain.typings.Typing
import it.krzeminski.githubactions.wrappergenerator.metadata.Input
import it.krzeminski.githubactions.wrappergenerator.metadata.Metadata
import it.krzeminski.githubactions.wrappergenerator.metadata.fetchMetadata

data class Wrapper(
    val kotlinCode: String,
    val filePath: String,
)

fun WrapperRequest.generateWrapper(
    fetchMetadataImpl: ActionCoords.() -> Metadata = { fetchMetadata() },
): Wrapper {
    val metadata = fetchMetadataImpl(coords)
    checkPropertiesAreValid(metadata)
    val actionWrapperSourceCode = generateActionWrapperSourceCode(metadata)
    return Wrapper(
        kotlinCode = actionWrapperSourceCode,
        filePath = "library/src/gen/kotlin/it/krzeminski/githubactions/actions/${coords.owner.toKotlinPackageName()}/${coords.buildActionClassName()}.kt",
    )
}

fun WrapperRequest.checkPropertiesAreValid(metadata: Metadata) {
    val invalidProperties = inputTypings.keys + deprecated - metadata.inputs.keys
    require(invalidProperties.isEmpty()) {
        """
            Request contains invalid properties:
            Available: ${metadata.inputs.keys}
            Invalid:   $invalidProperties
            """.trimIndent()
    }
}

private fun WrapperRequest.generateActionWrapperSourceCode(metadata: Metadata): String {
    val fileSpec = FileSpec.builder("it.krzeminski.githubactions.actions.${coords.owner.toKotlinPackageName()}", coords.buildActionClassName())
        .addComment(
            """
            This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
            be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
            generator itself.
            """.trimIndent()
        )
        .apply {
            if (deprecated.isNotEmpty()) {
                addAnnotation(AnnotationSpec.builder(Suppress::class.asClassName())
                    .addMember(CodeBlock.of("%S", "DEPRECATION"))
                    .build())
            }
        }
        .addType(generateActionClass(metadata))
        .indent("    ")
        .build()
    return buildString {
        fileSpec.writeTo(this)
    }
}

private fun WrapperRequest.generateActionClass(metadata: Metadata): TypeSpec {
    val actionClassName = coords.buildActionClassName()
    return TypeSpec.classBuilder(actionClassName)
        .addKdoc(actionKdoc(metadata, coords))
        .inheritsFromAction(coords)
        .primaryConstructor(metadata.primaryConstructor(inputTypings, coords))
        .properties(metadata, coords, inputTypings, deprecated)
        .addFunction(metadata.buildToYamlArgumentsFunction(inputTypings))
        .addCustomTypes(inputTypings.values.toSet(), coords)
        .build()
}

private fun TypeSpec.Builder.addCustomTypes(typings: Set<Typing>, coords: ActionCoords): TypeSpec.Builder {
    typings
        .mapNotNull { it.buildCustomType(coords) }
        .forEach { addType(it) }
    return this
}

private fun TypeSpec.Builder.properties(
    metadata: Metadata,
    coords: ActionCoords,
    inputTypings: Map<String, Typing>,
    deprecated: Set<String>
): TypeSpec.Builder {
    metadata.inputs.forEach { (key, input) ->
        addProperty(
            PropertySpec.builder(key.toCamelCase(), inputTypings.getInputType(key, input, coords))
                .initializer(key.toCamelCase())
                .apply {
                    if (key in deprecated) { addAnnotation(
                        AnnotationSpec.builder(Deprecated::class.asClassName())
                            .addMember(CodeBlock.of("%S", input.description))
                            .build())
                    }
                }
                .build()
        )
    }
    return this
}

private fun Metadata.buildToYamlArgumentsFunction(inputTypings: Map<String, Typing>) =
    FunSpec.builder("toYamlArguments")
        .addModifiers(KModifier.OVERRIDE)
        .addAnnotation(
            AnnotationSpec.builder(Suppress::class)
                .addMember("\"SpreadOperator\"")
                .build()
        )
        .addCode(
            CodeBlock.Builder().apply {
                add("return linkedMapOf(\n")
                indent()
                add("*listOfNotNull(\n")
                indent()
                inputs.forEach { (key, value) ->
                    val asStringCode = inputTypings.getInputTyping(key).asString()
                    if (!value.shouldBeNonNullInWrapper()) {
                        add("%L?.let { %S to it$asStringCode },\n", key.toCamelCase(), key)
                    } else {
                        add("%S to %L$asStringCode,\n", key, key.toCamelCase())
                    }
                }
                unindent()
                add(").toTypedArray()\n")
                unindent()
                add(")")
            }.build()
        )
        .build()

private fun TypeSpec.Builder.inheritsFromAction(coords: ActionCoords): TypeSpec.Builder = this
    .superclass(ClassName("it.krzeminski.githubactions.actions", "Action"))
    .addSuperclassConstructorParameter("%S", coords.owner)
    .addSuperclassConstructorParameter("%S", coords.name)
    .addSuperclassConstructorParameter("%S", coords.version)

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
