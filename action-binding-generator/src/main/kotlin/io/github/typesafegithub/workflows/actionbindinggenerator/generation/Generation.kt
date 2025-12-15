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
import com.squareup.kotlinpoet.buildCodeBlock
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionTypings
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.MetadataRevision
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.SignificantVersion.FULL
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.SignificantVersion.MAJOR
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.SignificantVersion.MINOR
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.TypingActualSource
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.fullName
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.isTopLevel
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.subName
import io.github.typesafegithub.workflows.actionbindinggenerator.generation.Properties.CUSTOM_INPUTS
import io.github.typesafegithub.workflows.actionbindinggenerator.generation.Properties.CUSTOM_VERSION
import io.github.typesafegithub.workflows.actionbindinggenerator.metadata.Input
import io.github.typesafegithub.workflows.actionbindinggenerator.metadata.Metadata
import io.github.typesafegithub.workflows.actionbindinggenerator.metadata.fetchMetadata
import io.github.typesafegithub.workflows.actionbindinggenerator.metadata.shouldBeRequiredInBinding
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
import io.ktor.client.engine.cio.CIO

public data class ActionBinding(
    val kotlinCode: String,
    val filePath: String,
    val className: String,
    val packageName: String,
    val typingActualSource: TypingActualSource?,
)

private object Types {
    val mapStringString =
        Map::class.asTypeName().parameterizedBy(
            String::class.asTypeName(),
            String::class.asTypeName(),
        )
    val nullableString = String::class.asTypeName().copy(nullable = true)
    val mapToList = MemberName("kotlin.collections", "toList")
    val listToArray = MemberName("kotlin.collections", "toTypedArray")
}

private object Properties {
    val CUSTOM_INPUTS = "_customInputs"
    val CUSTOM_VERSION = "_customVersion"
}

public suspend fun ActionCoords.generateBinding(
    metadataRevision: MetadataRevision,
    metadata: Metadata? = null,
    inputTypings: ActionTypings? = null,
    httpClient: HttpClient = HttpClient(CIO),
): List<ActionBinding> {
    val metadataResolved =
        metadata ?: this.fetchMetadata(metadataRevision, httpClient = httpClient) ?: return emptyList()
    val metadataProcessed = metadataResolved.removeDeprecatedInputsIfNameClash()

    val inputTypingsResolved = inputTypings ?: this.provideTypes(metadataRevision, httpClient = httpClient)

    val packageName = owner.toKotlinPackageName()
    val className = this.buildActionClassName()
    val classNameUntyped = "${className}_Untyped"

    val actionBindingSourceCodeUntyped =
        generateActionBindingSourceCode(
            metadataProcessed,
            this,
            emptyMap(),
            classNameUntyped,
            untypedClass = true,
            replaceWith = inputTypingsResolved.source?.let { CodeBlock.of("ReplaceWith(%S)", className) },
        )

    return listOfNotNull(
        ActionBinding(
            kotlinCode = actionBindingSourceCodeUntyped,
            filePath = "io/github/typesafegithub/workflows/actions/$packageName/$classNameUntyped.kt",
            className = classNameUntyped,
            packageName = packageName,
            typingActualSource = null,
        ),
        inputTypingsResolved.source?.let {
            val actionBindingSourceCode =
                generateActionBindingSourceCode(
                    metadata = metadataProcessed,
                    coords = this,
                    inputTypings = inputTypingsResolved.inputTypings,
                    className = className,
                    deprecationMessage =
                        inputTypingsResolved.takeIf { it.fromFallbackVersion }?.let {
                            "This typed binding was created from typings for an older version in " +
                                "https://github.com/typesafegithub/github-actions-typing-catalog. " +
                                "As soon as typings for the requested version are added, there could be breaking " +
                                "changes, and you need to delete these typings from your local Maven cache typically " +
                                "found in ~/.m2/repository/ to get the updated typing. In some cases, though, you " +
                                "may be lucky and things will work fine. To be on the safe side, consider " +
                                "contributing updated typings to the catalog before using this version, or even " +
                                "better: ask the action's owner to host the typings together with the action using " +
                                "https://github.com/typesafegithub/github-actions-typing."
                        },
                )
            ActionBinding(
                kotlinCode = actionBindingSourceCode,
                filePath = "io/github/typesafegithub/workflows/actions/$packageName/$className.kt",
                className = className,
                packageName = packageName,
                typingActualSource = it,
            )
        },
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
            }.values
            .associateBy({ it.key }, { it.value })
    return this.copy(inputs = newInputs)
}

private fun generateActionBindingSourceCode(
    metadata: Metadata,
    coords: ActionCoords,
    inputTypings: Map<String, Typing>,
    className: String,
    untypedClass: Boolean = false,
    deprecationMessage: String? = null,
    replaceWith: CodeBlock? = null,
): String {
    val fileSpec =
        FileSpec
            .builder(
                "io.github.typesafegithub.workflows.actions.${coords.owner.toKotlinPackageName()}",
                className,
            ).addFileComment(
                """
                This file was generated using action-binding-generator. Don't change it by hand, otherwise your
                changes will be overwritten with the next binding code regeneration.
                See https://github.com/typesafegithub/github-workflows-kt for more info.
                """.trimIndent(),
            ).addType(
                generateActionClass(
                    metadata,
                    coords,
                    inputTypings,
                    className,
                    untypedClass,
                    deprecationMessage,
                    replaceWith,
                ),
            ).addSuppressAnnotation(metadata)
            .indent("    ")
            .build()
    return buildString {
        fileSpec.writeTo(this)
    }
}

private fun FileSpec.Builder.addSuppressAnnotation(metadata: Metadata) =
    apply {
        val isDeprecatedInputUsed = metadata.inputs.values.any { it.deprecationMessage.isNullOrBlank().not() }

        addAnnotation(
            AnnotationSpec
                .builder(Suppress::class.asClassName())
                .addMember(CodeBlock.of("%S", "DataClassPrivateConstructor"))
                .addMember(CodeBlock.of("%S", "UNUSED_PARAMETER"))
                .apply {
                    if (isDeprecatedInputUsed) {
                        addMember(CodeBlock.of("%S", "DEPRECATION"))
                    }
                }.build(),
        )
    }

private fun generateActionClass(
    metadata: Metadata,
    coords: ActionCoords,
    inputTypings: Map<String, Typing>,
    className: String,
    untypedClass: Boolean,
    deprecationMessage: String?,
    replaceWith: CodeBlock?,
): TypeSpec =
    TypeSpec
        .classBuilder(className)
        .addModifiers(KModifier.DATA)
        .addKdocIfNotEmpty(actionKdoc(metadata, coords, untypedClass))
        .markDeprecated(deprecationMessage, replaceWith)
        .addClassConstructorAnnotation()
        .inheritsFromRegularAction(coords, metadata, className)
        .primaryConstructor(metadata.primaryConstructor(inputTypings, coords, className, untypedClass))
        .properties(metadata, coords, inputTypings, className, untypedClass)
        .addInitializerBlockIfNecessary(metadata, inputTypings, untypedClass)
        .addFunction(metadata.secondaryConstructor(inputTypings, coords, className, untypedClass))
        .addFunction(metadata.buildToYamlArgumentsFunction(inputTypings, untypedClass))
        .addCustomTypes(inputTypings, coords, className)
        .addOutputClassIfNecessary(metadata)
        .addBuildOutputObjectFunctionIfNecessary(metadata)
        .build()

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
    untypedClass: Boolean,
): TypeSpec.Builder {
    metadata.inputs.forEach { (key, input) ->
        val typedInput = inputTypings.containsKey(key)
        if (!untypedClass && typedInput) {
            addProperty(
                PropertySpec
                    .builder(
                        key.toCamelCase(),
                        inputTypings.getInputType(
                            key,
                            input,
                            coords,
                            className,
                            untypedClass = false,
                            typedInput = true,
                        ),
                    ).initializer(key.toCamelCase())
                    .annotateDeprecated(input)
                    .build(),
            )
        }
        addProperty(
            PropertySpec
                .builder(
                    "${key.toCamelCase()}_Untyped",
                    null.getInputType(key, input, coords, className, untypedClass, typedInput),
                ).initializer("${key.toCamelCase()}_Untyped")
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
        ParameterSpec
            .builder("stepId", String::class)
            .build()
    val propertiesFromOutputs =
        metadata.outputs.map { (key, value) ->
            PropertySpec
                .builder(key.toCamelCase(), String::class)
                .initializer("\"steps.\$stepId.outputs.$key\"")
                .addKdocIfNotEmpty(value.description.escapedForComments.removeTrailingWhitespacesForEachLine())
                .build()
        }
    addType(
        TypeSpec
            .classBuilder("Outputs")
            .primaryConstructor(
                FunSpec
                    .constructorBuilder()
                    .addParameter(stepIdConstructorParameter)
                    .build(),
            ).superclass(OutputsBase)
            .addSuperclassConstructorParameter("stepId")
            .addProperties(propertiesFromOutputs)
            .build(),
    )

    return this
}

private fun <B : Any> B.addKdocIfNotEmpty(kdoc: String): B {
    if (kdoc.isNotEmpty()) {
        when (this) {
            is TypeSpec.Builder -> addKdoc("%L", kdoc)
            is PropertySpec.Builder -> addKdoc("%L", kdoc)
            is ParameterSpec.Builder -> addKdoc("%L", kdoc)
            else -> error("Unexpected type ${this::class}}")
        }
    }
    return this
}

private fun ParameterSpec.Builder.addKdocIfNotEmpty(kdoc: CodeBlock): ParameterSpec.Builder {
    if (kdoc.toString().isNotEmpty()) {
        addKdoc(kdoc)
    }
    return this
}

private fun TypeSpec.Builder.addBuildOutputObjectFunctionIfNecessary(metadata: Metadata): TypeSpec.Builder {
    addFunction(
        FunSpec
            .builder("buildOutputObject")
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
                AnnotationSpec
                    .builder(Deprecated::class.asClassName())
                    .addMember("%S", input.deprecationMessage)
                    .build(),
            )
        }
    }

private fun Metadata.buildToYamlArgumentsFunction(
    inputTypings: Map<String, Typing>,
    untypedClass: Boolean,
) = FunSpec
    .builder("toYamlArguments")
    .addModifiers(KModifier.OVERRIDE)
    .returns(LinkedHashMap::class.parameterizedBy(String::class, String::class))
    .addAnnotation(
        AnnotationSpec
            .builder(Suppress::class)
            .addMember("\"SpreadOperator\"")
            .build(),
    ).addCode(linkedMapOfInputs(inputTypings, untypedClass))
    .build()

private fun Metadata.linkedMapOfInputs(
    inputTypings: Map<String, Typing>,
    untypedClass: Boolean,
) = if (inputs.isEmpty()) {
    CodeBlock.of("return %T($CUSTOM_INPUTS)", LinkedHashMap::class)
} else {
    buildCodeBlock {
        add("return linkedMapOf(\n")
        indent()
        add("*listOfNotNull(\n")
        indent()
        inputs.forEach { (key, value) ->
            val propertyName = key.toCamelCase()
            if (!untypedClass && inputTypings.containsKey(key)) {
                val asStringCode = inputTypings.getInputTyping(key).asString()
                add("%N?.let { %S to it$asStringCode },\n", propertyName, key)
            }
            val asStringCode = null.getInputTyping(key).asString()
            if (value.shouldBeRequiredInBinding() &&
                !value.shouldBeNullable(untypedClass, inputTypings.containsKey(key))
            ) {
                add("%S to %N$asStringCode,\n", key, "${propertyName}_Untyped")
            } else {
                add("%N?.let { %S to it$asStringCode },\n", "${propertyName}_Untyped", key)
            }
        }
        add("*$CUSTOM_INPUTS.%M().%M(),\n", Types.mapToList, Types.listToArray)
        unindent()
        add(").toTypedArray()\n")
        unindent()
        add(")")
    }
}

private fun TypeSpec.Builder.markDeprecated(
    deprecationMessage: String?,
    replaceWith: CodeBlock?,
): TypeSpec.Builder {
    if ((deprecationMessage != null) || (replaceWith != null)) {
        addAnnotation(
            AnnotationSpec
                .builder(Deprecated::class.asClassName())
                .addMember("%S", deprecationMessage ?: "Use the typed class instead")
                .apply {
                    if (replaceWith != null) {
                        addMember(replaceWith)
                    }
                }.build(),
        )
    }
    return this
}

private fun TypeSpec.Builder.addClassConstructorAnnotation(): TypeSpec.Builder {
    addAnnotation(
        AnnotationSpec
            .builder(ExposedCopyVisibility::class.asClassName())
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
        .addSuperclassConstructorParameter("%S", coords.fullName)
        .addSuperclassConstructorParameter(
            "_customVersion ?: %S",
            when (coords.significantVersion) {
                MAJOR -> coords.version.majorVersion
                MINOR -> coords.version.minorVersion
                FULL -> coords.version
            },
        )
}

private val String.majorVersion get() = substringBefore('.')
private val String.minorVersion get() = split('.', limit = 3).take(2).joinToString(".")

private fun Metadata.primaryConstructor(
    inputTypings: Map<String, Typing>,
    coords: ActionCoords,
    className: String,
    untypedClass: Boolean,
): FunSpec =
    FunSpec
        .constructorBuilder()
        .addModifiers(KModifier.PRIVATE)
        .addParameters(buildCommonConstructorParameters(inputTypings, coords, className, untypedClass))
        .build()

private fun Metadata.secondaryConstructor(
    inputTypings: Map<String, Typing>,
    coords: ActionCoords,
    className: String,
    untypedClass: Boolean,
): FunSpec =
    FunSpec
        .constructorBuilder()
        .addParameter(
            ParameterSpec
                .builder("pleaseUseNamedArguments", Unit::class)
                .addModifiers(KModifier.VARARG)
                .build(),
        ).addParameters(buildCommonConstructorParameters(inputTypings, coords, className, untypedClass))
        .callThisConstructor(
            inputs
                .keys
                .flatMap { inputName ->
                    val typedInput = inputTypings.containsKey(inputName)
                    listOfNotNull(
                        untypedClass.takeIf { !it && typedInput }?.let { inputName.toCamelCase() },
                        "${inputName.toCamelCase()}_Untyped",
                    )
                }.plus(CUSTOM_INPUTS)
                .plus(CUSTOM_VERSION)
                .map { CodeBlock.of("%N = %N", it, it) },
        ).build()

private fun Metadata.buildCommonConstructorParameters(
    inputTypings: Map<String, Typing>,
    coords: ActionCoords,
    className: String,
    untypedClass: Boolean,
): List<ParameterSpec> =
    inputs
        .flatMap { (key, input) ->
            val typedInput = inputTypings.containsKey(key)
            val description = input.description.escapedForComments.removeTrailingWhitespacesForEachLine()
            val kdoc =
                buildCodeBlock {
                    if (typedInput && !untypedClass && input.shouldBeRequiredInBinding()) {
                        add("%L", "<required>".escapedForComments)
                        if (description.isNotEmpty()) {
                            add(" ")
                        }
                    }
                    add("%L", description)
                }

            listOfNotNull(
                untypedClass.takeIf { !it && typedInput }?.let {
                    ParameterSpec
                        .builder(
                            key.toCamelCase(),
                            inputTypings.getInputType(
                                key,
                                input,
                                coords,
                                className,
                                untypedClass = false,
                                typedInput = true,
                            ),
                        ).defaultValue("null")
                        .addKdocIfNotEmpty(kdoc)
                        .build()
                },
                ParameterSpec
                    .builder(
                        "${key.toCamelCase()}_Untyped",
                        null.getInputType(key, input, coords, className, untypedClass, typedInput),
                    ).defaultValueIfNullable(input, untypedClass, typedInput)
                    .addKdocIfNotEmpty(kdoc)
                    .build(),
            )
        }.plus(
            ParameterSpec
                .builder(CUSTOM_INPUTS, Types.mapStringString)
                .defaultValue("mapOf()")
                .addKdocIfNotEmpty(
                    "Type-unsafe map where you can put any inputs that are not yet supported by the binding",
                ).build(),
        ).plus(
            ParameterSpec
                .builder(CUSTOM_VERSION, Types.nullableString)
                .defaultValue("null")
                .addKdocIfNotEmpty(
                    "Allows overriding action's version, for example to use a specific minor version, " +
                        "or a newer version that the binding doesn't yet know about",
                ).build(),
        )

private fun ParameterSpec.Builder.defaultValueIfNullable(
    input: Input,
    untypedClass: Boolean,
    typedInput: Boolean,
): ParameterSpec.Builder {
    if (input.shouldBeNullable(untypedClass, typedInput)) {
        defaultValue("null")
    }
    return this
}

private fun TypeSpec.Builder.addInitializerBlockIfNecessary(
    metadata: Metadata,
    inputTypings: Map<String, Typing>,
    untypedClass: Boolean,
): TypeSpec.Builder {
    if (untypedClass || metadata.inputs.isEmpty() || metadata.inputs.none { inputTypings.containsKey(it.key) }) {
        return this
    }
    addInitializerBlock(metadata.initializerBlock(inputTypings))
    return this
}

private fun Metadata.initializerBlock(inputTypings: Map<String, Typing>) =
    buildCodeBlock {
        var first = true
        inputs
            .filter { inputTypings.containsKey(it.key) }
            .forEach { (key, input) ->
                if (!first) {
                    add("\n")
                }
                first = false
                val propertyName = key.toCamelCase()
                add(
                    """
                    require(!((%1N != null) && (%1L_Untyped != null))) {
                        %2S
                    }

                    """.trimIndent(),
                    propertyName,
                    "Only $propertyName or ${propertyName}_Untyped must be set, but not both",
                )
                if (input.shouldBeRequiredInBinding()) {
                    add(
                        """
                        require((%1N != null) || (%1L_Untyped != null)) {
                            %2S
                        }

                        """.trimIndent(),
                        propertyName,
                        "Either $propertyName or ${propertyName}_Untyped must be set, one of them is required",
                    )
                }
            }
    }

private fun actionKdoc(
    metadata: Metadata,
    coords: ActionCoords,
    untypedClass: Boolean,
) = (
    if (untypedClass) {
        """
        |```text
        |!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        |!!!                             WARNING                             !!!
        |!!!                                                                 !!!
        |!!! This action binding has no typings provided. All inputs will    !!!
        |!!! have a default type of String.                                  !!!
        |!!! To be able to use this action in a type-safe way, ask the       !!!
        |!!! action's owner to provide the typings using                     !!!
        |!!!                                                                 !!!
        |!!! https://github.com/typesafegithub/github-actions-typing         !!!
        |!!!                                                                 !!!
        |!!! or if it's impossible, contribute typings to a community-driven !!!
        |!!!                                                                 !!!
        |!!! https://github.com/typesafegithub/github-actions-typing-catalog !!!
        |!!!                                                                 !!!
        |!!! This '_Untyped' binding will be available even once the typings !!!
        |!!! are added.                                                      !!!
        |!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        |```
        |
        |
        """.trimMargin()
    } else {
        ""
    }
) +
    """
       |Action: ${metadata.name.escapedForComments}
       |
       |${metadata.description.escapedForComments.removeTrailingWhitespacesForEachLine()}
       |
       |[Action on GitHub](https://github.com/${coords.owner}/${coords.name}${if (coords.isTopLevel) "" else "/tree/${coords.version}${coords.subName}"})
    """.trimMargin()

private fun Map<String, Typing>?.getInputTyping(key: String) = this?.get(key) ?: StringTyping

private fun Map<String, Typing>?.getInputType(
    key: String,
    input: Input,
    coords: ActionCoords,
    className: String,
    untypedClass: Boolean,
    typedInput: Boolean,
) = getInputTyping(key)
    .getClassName(coords.owner.toKotlinPackageName(), className, key)
    .copy(nullable = input.shouldBeNullable(untypedClass, typedInput))

private fun Input.shouldBeNullable(
    untypedClass: Boolean,
    typedInput: Boolean,
) = (untypedClass && !shouldBeRequiredInBinding()) ||
    (!untypedClass && (typedInput || !shouldBeRequiredInBinding()))

private val String.escapedForComments
    get() =
        // Working around a bug in Kotlin: https://youtrack.jetbrains.com/issue/KT-23333
        // and a shortcoming in KotlinPoet: https://github.com/square/kotlinpoet/issues/887
        replace("/*", "/&#42;")
            .replace("*/", "&#42;/")
            .replace("`[^`]++`".toRegex()) {
                it.value.replace("&#42;", "`&#42;`")
            }.replace("<", "&lt;")
            .replace(">", "&gt;")
