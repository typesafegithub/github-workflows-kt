package io.github.typesafegithub.workflows.actionbindinggenerator.generation

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
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.MetadataRevision
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.TypingActualSource
import io.github.typesafegithub.workflows.actionbindinggenerator.generation.Properties.CUSTOM_INPUTS
import io.github.typesafegithub.workflows.actionbindinggenerator.generation.Properties.CUSTOM_VERSION
import io.github.typesafegithub.workflows.actionbindinggenerator.metadata.Input
import io.github.typesafegithub.workflows.actionbindinggenerator.metadata.Metadata
import io.github.typesafegithub.workflows.actionbindinggenerator.metadata.fetchMetadata
import io.github.typesafegithub.workflows.actionbindinggenerator.metadata.shouldBeNonNullInBinding
import io.github.typesafegithub.workflows.actionbindinggenerator.typing.StringTyping
import io.github.typesafegithub.workflows.actionbindinggenerator.typing.Typing
import io.github.typesafegithub.workflows.actionbindinggenerator.typing.asString
import io.github.typesafegithub.workflows.actionbindinggenerator.typing.buildCustomType
import io.github.typesafegithub.workflows.actionbindinggenerator.typing.getClassName
import io.github.typesafegithub.workflows.actionbindinggenerator.typing.provideTypes
import io.github.typesafegithub.workflows.actionbindinggenerator.utils.removeTrailingWhitespacesForEachLine
import io.github.typesafegithub.workflows.actionbindinggenerator.utils.toCamelCase
import io.github.typesafegithub.workflows.actionbindinggenerator.utils.toKotlinPackageName
import io.ktor.client.HttpClient
import java.nio.file.Path

public data class ActionBinding(
    val kotlinCode: String,
    val filePath: String,
    val className: String,
    val packageName: String,
    val typingActualSource: TypingActualSource?,
)

public enum class ClientType {
    /**
     * The binding is going to be bundled with the library.
     */
    BUNDLED_WITH_LIB,

    /**
     * The binding is going to be provided in a versioned JAR.
     */
    VERSIONED_JAR,
}

private object Types {
    val mapStringString = Map::class.asTypeName().parameterizedBy(String::class.asTypeName(), String::class.asTypeName())
    val nullableString = String::class.asTypeName().copy(nullable = true)
    val mapToList = MemberName("kotlin.collections", "toList")
    val listToArray = MemberName("kotlin.collections", "toTypedArray")
}

private object Properties {
    val CUSTOM_INPUTS = "_customInputs"
    val CUSTOM_VERSION = "_customVersion"
}

public fun ActionCoords.generateBinding(
    metadataRevision: MetadataRevision,
    metadata: Metadata? = null,
    inputTypings: Pair<Map<String, Typing>, TypingActualSource?>? = null,
    clientType: ClientType = ClientType.BUNDLED_WITH_LIB,
    httpClient: HttpClient,
): ActionBinding? {
    require(this.version.removePrefix("v").toIntOrNull() != null) {
        "Only major versions are supported, and '${this.version}' was given!"
    }
    val metadataResolved = metadata ?: this.fetchMetadata(metadataRevision, httpClient) ?: return null
    val metadataProcessed = metadataResolved.removeDeprecatedInputsIfNameClash()

    val inputTypingsResolved = inputTypings ?: this.provideTypes(metadataRevision, httpClient)

    val className = this.buildActionClassName(includeVersion = clientType == ClientType.BUNDLED_WITH_LIB)
    val actionBindingSourceCode =
        generateActionBindingSourceCode(metadataProcessed, this, inputTypingsResolved.first, className)
    val packageName = owner.toKotlinPackageName()
    return ActionBinding(
        kotlinCode = actionBindingSourceCode,
        filePath = "github-workflows-kt/src/gen/kotlin/io/github/typesafegithub/workflows/actions/$packageName/$className.kt",
        className = className,
        packageName = packageName,
        typingActualSource = inputTypingsResolved.second,
    )
}

private fun Metadata.removeDeprecatedInputsIfNameClash(): Metadata {
    val newInputs =
        this.inputs.entries
            .groupBy { (originalKey, _) -> originalKey.toCamelCase() }
            .mapValues { (_, clashingInputs) ->
                clashingInputs
                    .find { it.value.deprecationMessage == null }
                    ?: clashingInputs.first()
            }.values.associateBy({ it.key }, { it.value })
    return this.copy(inputs = newInputs)
}

private fun generateActionBindingSourceCode(
    metadata: Metadata,
    coords: ActionCoords,
    inputTypings: Map<String, Typing>,
    className: String,
): String {
    val fileSpec =
        FileSpec.builder(
            "io.github.typesafegithub.workflows.actions.${coords.owner.toKotlinPackageName()}",
            className,
        )
            .addFileComment(
                """
                This file was generated using action-binding-generator. Don't change it by hand, otherwise your
                changes will be overwritten with the next binding code regeneration.
                See https://github.com/typesafegithub/github-workflows-kt for more info.
                """.trimIndent(),
            )
            .addType(generateActionClass(metadata, coords, inputTypings, className))
            .addSuppressAnnotation(metadata, coords)
            .indent("    ")
            .build()
    return buildString {
        fileSpec.writeTo(this)
    }
}

private fun FileSpec.Builder.addSuppressAnnotation(
    metadata: Metadata,
    coords: ActionCoords,
) = apply {
    val isDeprecatedInputUsed = metadata.inputs.values.any { it.deprecationMessage.isNullOrBlank().not() }
    val addSuppressionForDeprecation = isDeprecatedInputUsed || coords.deprecatedByVersion != null

    addAnnotation(
        AnnotationSpec.builder(Suppress::class.asClassName())
            .addMember(CodeBlock.of("%S", "DataClassPrivateConstructor"))
            .addMember(CodeBlock.of("%S", "UNUSED_PARAMETER"))
            .apply {
                if (addSuppressionForDeprecation) {
                    addMember(CodeBlock.of("%S", "DEPRECATION"))
                }
            }
            .build(),
    )
}

private fun generateActionClass(
    metadata: Metadata,
    coords: ActionCoords,
    inputTypings: Map<String, Typing>,
    className: String,
): TypeSpec {
    return TypeSpec.classBuilder(className)
        .addModifiers(KModifier.DATA)
        .addKdoc(actionKdoc(metadata, coords))
        .addMaybeDeprecated(coords)
        .inheritsFromRegularAction(coords, metadata, className)
        .primaryConstructor(metadata.primaryConstructor(inputTypings, coords, className))
        .properties(metadata, coords, inputTypings, className)
        .addFunction(metadata.secondaryConstructor(inputTypings, coords, className))
        .addFunction(metadata.buildToYamlArgumentsFunction(inputTypings))
        .addCustomTypes(inputTypings, coords, className)
        .addOutputClassIfNecessary(metadata)
        .addBuildOutputObjectFunctionIfNecessary(metadata)
        .build()
}

private fun TypeSpec.Builder.addCustomTypes(
    typings: Map<String, Typing>,
    coords: ActionCoords,
    className: String,
): TypeSpec.Builder {
    typings
        .mapNotNull { (inputName, typing) -> typing.buildCustomType(coords, inputName, className) }
        .distinctBy { it.name }
        .forEach { addType(it) }
    return this
}

private fun TypeSpec.Builder.properties(
    metadata: Metadata,
    coords: ActionCoords,
    inputTypings: Map<String, Typing>,
    className: String,
): TypeSpec.Builder {
    metadata.inputs.forEach { (key, input) ->
        addProperty(
            PropertySpec.builder(key.toCamelCase(), inputTypings.getInputType(key, input, coords, className))
                .initializer(key.toCamelCase())
                .annotateDeprecated(input)
                .build(),
        )
    }
    addProperty(PropertySpec.builder(CUSTOM_INPUTS, Types.mapStringString).initializer(CUSTOM_INPUTS).build())
    addProperty(PropertySpec.builder(CUSTOM_VERSION, Types.nullableString).initializer(CUSTOM_VERSION).build())
    return this
}

private val OutputsBase = ClassName("io.github.typesafegithub.workflows.domain.actions", "Action", "Outputs")

private fun TypeSpec.Builder.addOutputClassIfNecessary(metadata: Metadata): TypeSpec.Builder {
    if (metadata.outputs.isEmpty()) {
        return this
    }

    val stepIdConstructorParameter =
        ParameterSpec.builder("stepId", String::class)
            .build()
    val propertiesFromOutputs =
        metadata.outputs.map { (key, value) ->
            PropertySpec.builder(key.toCamelCase(), String::class)
                .initializer("\"steps.\$stepId.outputs.$key\"")
                .addKdoc(value.description.nestedCommentsSanitized.removeTrailingWhitespacesForEachLine())
                .build()
        }
    addType(
        TypeSpec.classBuilder("Outputs")
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameter(stepIdConstructorParameter)
                    .build(),
            )
            .superclass(OutputsBase)
            .addSuperclassConstructorParameter("stepId")
            .addProperties(propertiesFromOutputs)
            .build(),
    )

    return this
}

private fun TypeSpec.Builder.addBuildOutputObjectFunctionIfNecessary(metadata: Metadata): TypeSpec.Builder {
    addFunction(
        FunSpec.builder("buildOutputObject")
            .returns(if (metadata.outputs.isEmpty()) OutputsBase else ClassName("", "Outputs"))
            .addModifiers(KModifier.OVERRIDE)
            .addParameter("stepId", String::class)
            .addCode(CodeBlock.of("return Outputs(stepId)"))
            .build(),
    )

    return this
}

private fun PropertySpec.Builder.annotateDeprecated(input: Input) =
    apply {
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
                if (!value.shouldBeNonNullInBinding()) {
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

private fun TypeSpec.Builder.inheritsFromRegularAction(
    coords: ActionCoords,
    metadata: Metadata,
    className: String,
): TypeSpec.Builder {
    val superclass =
        ClassName("io.github.typesafegithub.workflows.domain.actions", "RegularAction")
            .plusParameter(
                if (metadata.outputs.isEmpty()) {
                    OutputsBase
                } else {
                    ClassName(
                        "io.github.typesafegithub.workflows.actions.${coords.owner.toKotlinPackageName()}",
                        className,
                        "Outputs",
                    )
                },
            )
    return this
        .superclass(superclass)
        .addSuperclassConstructorParameter("%S", coords.owner)
        .addSuperclassConstructorParameter("%S", coords.name)
        .addSuperclassConstructorParameter("_customVersion ?: %S", coords.version)
}

private fun Metadata.primaryConstructor(
    inputTypings: Map<String, Typing>,
    coords: ActionCoords,
    className: String,
): FunSpec {
    return FunSpec.constructorBuilder()
        .addModifiers(KModifier.PRIVATE)
        .addParameters(buildCommonConstructorParameters(inputTypings, coords, className))
        .build()
}

private fun Metadata.secondaryConstructor(
    inputTypings: Map<String, Typing>,
    coords: ActionCoords,
    className: String,
): FunSpec {
    return FunSpec.constructorBuilder()
        .addParameter(
            ParameterSpec.builder("pleaseUseNamedArguments", Unit::class)
                .addModifiers(KModifier.VARARG)
                .build(),
        )
        .addParameters(buildCommonConstructorParameters(inputTypings, coords, className))
        .callThisConstructor(
            (inputs.keys.map { it.toCamelCase() } + CUSTOM_INPUTS + CUSTOM_VERSION)
                .map { CodeBlock.of("%N=%N", it, it) },
        )
        .build()
}

private fun Metadata.buildCommonConstructorParameters(
    inputTypings: Map<String, Typing>,
    coords: ActionCoords,
    className: String,
): List<ParameterSpec> =
    inputs.map { (key, input) ->
        ParameterSpec.builder(key.toCamelCase(), inputTypings.getInputType(key, input, coords, className))
            .defaultValueIfNullable(input)
            .addKdoc(input.description.nestedCommentsSanitized.removeTrailingWhitespacesForEachLine())
            .build()
    }.plus(
        ParameterSpec.builder(CUSTOM_INPUTS, Types.mapStringString)
            .defaultValue("mapOf()")
            .addKdoc("Type-unsafe map where you can put any inputs that are not yet supported by the binding")
            .build(),
    ).plus(
        ParameterSpec.builder(CUSTOM_VERSION, Types.nullableString)
            .defaultValue("null")
            .addKdoc(
                "Allows overriding action's version, for example to use a specific minor version, " +
                    "or a newer version that the binding doesn't yet know about",
            )
            .build(),
    )

private fun ParameterSpec.Builder.defaultValueIfNullable(input: Input): ParameterSpec.Builder {
    if (!input.shouldBeNonNullInBinding()) {
        defaultValue("null")
    }
    return this
}

private fun actionKdoc(
    metadata: Metadata,
    coords: ActionCoords,
) = """
       |Action: ${metadata.name.nestedCommentsSanitized}
       |
       |${metadata.description.nestedCommentsSanitized.removeTrailingWhitespacesForEachLine()}
       |
       |[Action on GitHub](https://github.com/${coords.owner}/${coords.name.substringBefore(
    '/',
)}${if ("/" in coords.name) "/tree/${coords.version}/${coords.name.substringAfter('/')}" else ""})
    """.trimMargin()

private fun Map<String, Typing>.getInputTyping(key: String) = this[key] ?: StringTyping

private fun Map<String, Typing>.getInputType(
    key: String,
    input: Input,
    coords: ActionCoords,
    className: String,
) = getInputTyping(key).getClassName(coords.owner.toKotlinPackageName(), className, key)
    .copy(nullable = !input.shouldBeNonNullInBinding())

// Replacing: working around a bug in Kotlin: https://youtrack.jetbrains.com/issue/KT-23333
//            and a shortcoming in KotlinPoet: https://github.com/square/kotlinpoet/issues/887
private val String.nestedCommentsSanitized
    get() =
        replace("/*", "/&#42;")
            .replace("*/", "&#42;/")
            .replace("`[^`]++`".toRegex()) {
                it.value.replace("&#42;", "`&#42;`")
            }

private fun ActionCoords.getCommitHashFromFileSystem(): String? =
    Path.of("actions", owner, name.substringBefore('/'), version, "commit-hash.txt").toFile()
        .let { if (it.exists()) it.readText().trim() else null }
