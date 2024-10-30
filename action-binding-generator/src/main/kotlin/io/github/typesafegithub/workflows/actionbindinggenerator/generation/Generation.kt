package io.github.typesafegithub.workflows.actionbindinggenerator.generation

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.KModifier.OVERRIDE
import com.squareup.kotlinpoet.KModifier.PRIVATE
import com.squareup.kotlinpoet.KModifier.VARARG
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.plusParameter
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.asTypeName
import com.squareup.kotlinpoet.buildCodeBlock
import com.squareup.kotlinpoet.joinToCode
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.MetadataRevision
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.TypingActualSource
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.fullName
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.isTopLevel
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.prettyPrint
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.subName
import io.github.typesafegithub.workflows.actionbindinggenerator.generation.Properties.CUSTOM_INPUTS
import io.github.typesafegithub.workflows.actionbindinggenerator.generation.Properties.CUSTOM_VERSION
import io.github.typesafegithub.workflows.actionbindinggenerator.generation.Types.nullableAny
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
import io.github.typesafegithub.workflows.actionbindinggenerator.versioning.BindingVersion
import io.github.typesafegithub.workflows.actionbindinggenerator.versioning.BindingVersion.V1
import io.github.typesafegithub.workflows.actionbindinggenerator.versioning.BindingVersion.V2
import java.util.Objects

public data class ActionBinding(
    val kotlinCode: String,
    val filePath: String,
    val className: String,
    val packageName: String,
    val typingActualSource: TypingActualSource?,
)

private object Types {
    val mapStringString = Map::class.asTypeName().parameterizedBy(String::class.asTypeName(), String::class.asTypeName())
    val nullableAny = Any::class.asTypeName().copy(nullable = true)
    val nullableString = String::class.asTypeName().copy(nullable = true)
    val mapToList = MemberName("kotlin.collections", "toList")
    val listToArray = MemberName("kotlin.collections", "toTypedArray")
}

private object Properties {
    val CUSTOM_INPUTS = "_customInputs"
    val CUSTOM_VERSION = "_customVersion"
}

public fun ActionCoords.generateBinding(
    bindingVersion: BindingVersion = V1,
    metadataRevision: MetadataRevision,
    metadata: Metadata? = null,
    inputTypings: Pair<Map<String, Typing>, TypingActualSource?>? = null,
): List<ActionBinding> {
    val metadataResolved = metadata ?: this.fetchMetadata(metadataRevision) ?: return emptyList()
    val metadataProcessed = metadataResolved.removeDeprecatedInputsIfNameClash()

    val inputTypingsResolved = inputTypings ?: this.provideTypes(metadataRevision)

    val packageName = owner.toKotlinPackageName()
    val className = this.buildActionClassName()
    val classNameUntyped = "${className}_Untyped"

    val actionBindingSourceCodeUntyped =
        generateActionBindingSourceCode(
            metadata = metadataProcessed,
            coords = this,
            bindingVersion = bindingVersion,
            inputTypings = emptyMap(),
            className = classNameUntyped,
            untypedClass = true,
            replaceWith = inputTypingsResolved.second?.let { CodeBlock.of("ReplaceWith(%S)", className) },
        )

    return listOfNotNull(
        ActionBinding(
            kotlinCode = actionBindingSourceCodeUntyped,
            filePath = "io/github/typesafegithub/workflows/actions/$packageName/$classNameUntyped.kt",
            className = classNameUntyped,
            packageName = packageName,
            typingActualSource = null,
        ),
        inputTypingsResolved.second?.let {
            val actionBindingSourceCode =
                generateActionBindingSourceCode(
                    metadata = metadataProcessed,
                    coords = this,
                    bindingVersion = bindingVersion,
                    inputTypings = inputTypingsResolved.first,
                    className = className,
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
    bindingVersion: BindingVersion,
    inputTypings: Map<String, Typing>,
    className: String,
    untypedClass: Boolean = false,
    replaceWith: CodeBlock? = null,
): String {
    val packageName = "io.github.typesafegithub.workflows.actions.${coords.owner.toKotlinPackageName()}"
    val fileSpec =
        FileSpec
            .builder(packageName, className)
            .addFileComment(
                """
                This file was generated using action-binding-generator. Don't change it by hand, otherwise your
                changes will be overwritten with the next binding code regeneration.
                See https://github.com/typesafegithub/github-workflows-kt for more info.
                """.trimIndent(),
            ).addType(
                generateActionClass(
                    metadata,
                    coords,
                    bindingVersion,
                    inputTypings,
                    packageName,
                    className,
                    untypedClass,
                    replaceWith,
                ),
            ).addSuppressAnnotation(
                bindingVersion = bindingVersion,
                classIsDeprecated = replaceWith != null,
                metadata = metadata,
            ).indent("    ")
            .build()
    return buildString {
        fileSpec.writeTo(this)
    }
}

private fun FileSpec.Builder.addSuppressAnnotation(
    bindingVersion: BindingVersion,
    classIsDeprecated: Boolean,
    metadata: Metadata,
): FileSpec.Builder {
    val suppress = AnnotationSpec.builder(Suppress::class.asClassName())
    if (bindingVersion <= V1) {
        suppress.addMember(CodeBlock.of("%S", "DataClassPrivateConstructor"))
    }
    suppress.addMember(CodeBlock.of("%S", "UNUSED_PARAMETER"))
    val isDeprecatedInputUsed = metadata.inputs.values.any { it.deprecationMessage.isNullOrBlank().not() }
    if (bindingVersion.isDeprecated || ((bindingVersion >= V2) && classIsDeprecated) || isDeprecatedInputUsed) {
        suppress.addMember(CodeBlock.of("%S", "DEPRECATION"))
    }
    addAnnotation(suppress.build())
    return this
}

private fun generateActionClass(
    metadata: Metadata,
    coords: ActionCoords,
    bindingVersion: BindingVersion,
    inputTypings: Map<String, Typing>,
    packageName: String,
    className: String,
    untypedClass: Boolean,
    replaceWith: CodeBlock?,
): TypeSpec {
    val commonConstructorParameters =
        metadata.buildCommonConstructorParameters(inputTypings, coords, className, untypedClass)
    return TypeSpec
        .classBuilder(className)
        .addDataModifier(bindingVersion)
        .addKdocIfNotEmpty(actionKdoc(metadata, coords, untypedClass))
        .deprecateBindingVersion(bindingVersion)
        .replaceWith(bindingVersion, replaceWith)
        .addClassConstructorAnnotation(bindingVersion)
        .inheritsFromRegularAction(coords, metadata, className)
        .primaryConstructor(buildPrimaryConstructor(bindingVersion, commonConstructorParameters))
        .properties(metadata, coords, inputTypings, className, untypedClass)
        .addInitializerBlock(metadata, bindingVersion, coords, inputTypings, untypedClass)
        .addSecondaryConstructor(bindingVersion, metadata, inputTypings, commonConstructorParameters, untypedClass)
        .addFunction(metadata.buildToYamlArgumentsFunction(inputTypings, untypedClass))
        .addEqualsFunction(bindingVersion, className, commonConstructorParameters)
        .addHashCodeFunction(bindingVersion, commonConstructorParameters)
        .addToStringFunction(bindingVersion, className, commonConstructorParameters)
        .addCopyFunction(bindingVersion, packageName, className, commonConstructorParameters)
        .addCustomTypes(inputTypings, coords, className)
        .addOutputClassIfNecessary(metadata)
        .addBuildOutputObjectFunctionIfNecessary(metadata)
        .build()
}

private fun TypeSpec.Builder.addDataModifier(bindingVersion: BindingVersion): TypeSpec.Builder {
    if (bindingVersion <= V1) {
        addModifiers(KModifier.DATA)
    }
    return this
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
                .builder("${key.toCamelCase()}_Untyped", null.getInputType(key, input, coords, className, untypedClass, typedInput))
                .initializer("${key.toCamelCase()}_Untyped")
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
            .addModifiers(OVERRIDE)
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
    .addModifiers(OVERRIDE)
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
            if (value.shouldBeRequiredInBinding() && !value.shouldBeNullable(untypedClass, inputTypings.containsKey(key))) {
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

private fun TypeSpec.Builder.deprecateBindingVersion(bindingVersion: BindingVersion): TypeSpec.Builder {
    if (bindingVersion.isDeprecated) {
        addAnnotation(
            AnnotationSpec
                .builder(Deprecated::class.asClassName())
                .addMember("%S", "Use a non-deprecated binding version in the repository URL")
                .build(),
        )
    }
    return this
}

private fun TypeSpec.Builder.replaceWith(
    bindingVersion: BindingVersion,
    replaceWith: CodeBlock?,
): TypeSpec.Builder {
    if (!bindingVersion.isDeprecated && replaceWith != null) {
        addAnnotation(
            AnnotationSpec
                .builder(Deprecated::class.asClassName())
                .addMember("%S", "Use the typed class instead")
                .addMember(replaceWith)
                .build(),
        )
    }
    return this
}

private fun TypeSpec.Builder.addClassConstructorAnnotation(bindingVersion: BindingVersion): TypeSpec.Builder {
    if (bindingVersion <= V1) {
        addAnnotation(
            AnnotationSpec
                .builder(ExposedCopyVisibility::class.asClassName())
                .build(),
        )
    }
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
        .addSuperclassConstructorParameter("_customVersion ?: %S", coords.version)
}

private fun buildPrimaryConstructor(
    bindingVersion: BindingVersion,
    commonConstructorParameters: Iterable<ParameterSpec>,
): FunSpec {
    val constructor = FunSpec.constructorBuilder()
    if (bindingVersion <= V1) {
        constructor.addModifiers(PRIVATE)
    }
    if (bindingVersion >= V2) {
        constructor
            .addParameter(
                ParameterSpec
                    .builder("pleaseUseNamedArguments", Unit::class)
                    .addModifiers(VARARG)
                    .build(),
            )
    }
    return constructor.addParameters(commonConstructorParameters).build()
}

private fun TypeSpec.Builder.addSecondaryConstructor(
    bindingVersion: BindingVersion,
    metadata: Metadata,
    inputTypings: Map<String, Typing>,
    commonConstructorParameters: Iterable<ParameterSpec>,
    untypedClass: Boolean,
): TypeSpec.Builder {
    if (bindingVersion <= V1) {
        addFunction(metadata.buildSecondaryConstructor(inputTypings, commonConstructorParameters, untypedClass))
    }
    return this
}

private fun Metadata.buildSecondaryConstructor(
    inputTypings: Map<String, Typing>,
    commonConstructorParameters: Iterable<ParameterSpec>,
    untypedClass: Boolean,
): FunSpec =
    FunSpec
        .constructorBuilder()
        .addParameter(
            ParameterSpec
                .builder("pleaseUseNamedArguments", Unit::class)
                .addModifiers(VARARG)
                .build(),
        ).addParameters(commonConstructorParameters)
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
                .addKdocIfNotEmpty("Type-unsafe map where you can put any inputs that are not yet supported by the binding")
                .build(),
        ).plus(
            ParameterSpec
                .builder(CUSTOM_VERSION, Types.nullableString)
                .defaultValue("null")
                .addKdocIfNotEmpty(
                    "Allows overriding action's version, for example to use a specific minor version, " +
                        "or a newer version that the binding doesn't yet know about",
                ).build(),
        )

private fun TypeSpec.Builder.addEqualsFunction(
    bindingVersion: BindingVersion,
    className: String,
    commonConstructorParameters: Iterable<ParameterSpec>,
): TypeSpec.Builder {
    if (bindingVersion >= V2) {
        addFunction(buildEqualsFunction(className, commonConstructorParameters))
    }
    return this
}

private fun buildEqualsFunction(
    className: String,
    commonConstructorParameters: Iterable<ParameterSpec>,
) = FunSpec
    .builder("equals")
    .addModifiers(OVERRIDE)
    .addParameter(ParameterSpec.builder("other", nullableAny).build())
    .returns(Boolean::class)
    .addCode(
        buildCodeBlock {
            addStatement("if (this === other) return true")
            addStatement("if (javaClass != other?.javaClass) return false")
            addStatement("other as %N", className)

            val propertyNames = commonConstructorParameters.map { it.name }
            when (propertyNames.size) {
                0 -> addStatement("return true")
                1 -> addStatement("return %1N == other.%1N", propertyNames.first())
                else -> {
                    add("return %1N == other.%1N &&\n", propertyNames.first())
                    add(
                        propertyNames.drop(1).joinToCode(
                            separator = " &&\n",
                        ) {
                            buildCodeBlock {
                                indent()
                                add("%1N == other.%1N", it)
                                unindent()
                            }
                        },
                    )
                }
            }
        },
    ).build()

private fun TypeSpec.Builder.addHashCodeFunction(
    bindingVersion: BindingVersion,
    commonConstructorParameters: Iterable<ParameterSpec>,
) = apply {
    if (bindingVersion >= V2) {
        addFunction(buildHashCodeFunction(commonConstructorParameters))
    }
}

private fun buildHashCodeFunction(commonConstructorParameters: Iterable<ParameterSpec>) =
    FunSpec
        .builder("hashCode")
        .addModifiers(OVERRIDE)
        .returns(Int::class)
        .addCode(
            buildCodeBlock {
                val propertyNames = commonConstructorParameters.map { it.name }
                when (propertyNames.size) {
                    0 -> addStatement("return 0")
                    1 -> addStatement("return %T.hash(%N)", Objects::class, propertyNames.first())
                    else -> {
                        add("return %T.hash(\n", Objects::class)
                        add(
                            propertyNames.joinToCode(
                                separator = ",\n",
                                suffix = ",\n)",
                            ) {
                                buildCodeBlock {
                                    indent()
                                    add("%N", it)
                                    unindent()
                                }
                            },
                        )
                    }
                }
            },
        ).build()

private fun TypeSpec.Builder.addToStringFunction(
    bindingVersion: BindingVersion,
    className: String,
    commonConstructorParameters: Iterable<ParameterSpec>,
): TypeSpec.Builder {
    if (bindingVersion >= V2) {
        addFunction(buildToStringFunction(className, commonConstructorParameters))
    }
    return this
}

private fun buildToStringFunction(
    className: String,
    commonConstructorParameters: Iterable<ParameterSpec>,
) = FunSpec
    .builder("toString")
    .addModifiers(OVERRIDE)
    .returns(String::class)
    .addCode(
        buildCodeBlock {
            val propertyNames = commonConstructorParameters.map { it.name }
            when (propertyNames.size) {
                0 -> addStatement("return %S", "$className()")

                1 ->
                    addStatement(
                        "return %P",
                        CodeBlock.of("%1L(%2L=$%2N)", className, propertyNames.first()),
                    )

                else -> {
                    beginControlFlow("return buildString")
                    addStatement("append(%S)", "$className(")
                    propertyNames.dropLast(1).forEach {
                        addStatement(
                            "append(%P)",
                            CodeBlock.of("%1L=$%1N", it),
                        )
                        addStatement("append(%S)", ", ")
                    }
                    addStatement(
                        "append(%P)",
                        CodeBlock.of("%1L=$%1N", propertyNames.last()),
                    )
                    addStatement("append(%S)", ")")
                    endControlFlow()
                }
            }
        },
    ).build()

private fun TypeSpec.Builder.addCopyFunction(
    bindingVersion: BindingVersion,
    packageName: String,
    className: String,
    commonConstructorParameters: Iterable<ParameterSpec>,
): TypeSpec.Builder {
    if (bindingVersion >= V2) {
        addFunction(buildCopyFunction(packageName, className, commonConstructorParameters))
    }
    return this
}

private fun buildCopyFunction(
    packageName: String,
    className: String,
    commonConstructorParameters: Iterable<ParameterSpec>,
) = FunSpec
    .builder("copy")
    .returns(ClassName(packageName, className))
    .addParameter(
        ParameterSpec
            .builder("pleaseUseNamedArguments", Unit::class)
            .addModifiers(VARARG)
            .build(),
    ).addParameters(
        commonConstructorParameters
            .map { it.toBuilder().defaultValue("this.%N", it.name).build() },
    ).addCode(
        buildCodeBlock {
            val propertyNames = commonConstructorParameters.map { it.name }
            when (propertyNames.size) {
                0 -> addStatement("return %N()", className)

                1 -> addStatement("return %1N(%2N = %2N)", className, propertyNames.first())

                else -> {
                    add("return %1N(\n", className)
                    add(
                        propertyNames.joinToCode(
                            separator = "",
                            suffix = ")",
                        ) {
                            buildCodeBlock {
                                indent()
                                add("%1N = %1N,\n", it)
                                unindent()
                            }
                        },
                    )
                }
            }
        },
    ).build()

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

private fun TypeSpec.Builder.addInitializerBlock(
    metadata: Metadata,
    bindingVersion: BindingVersion,
    coords: ActionCoords,
    inputTypings: Map<String, Typing>,
    untypedClass: Boolean,
): TypeSpec.Builder {
    if (!bindingVersion.isDeprecated &&
        !bindingVersion.isExperimental &&
        (untypedClass || metadata.inputs.isEmpty() || metadata.inputs.none { inputTypings.containsKey(it.key) })
    ) {
        return this
    }

    addInitializerBlock(
        buildCodeBlock {
            if (bindingVersion.isDeprecated) {
                val firstStableVersion = BindingVersion.entries.find { !it.isDeprecated && !it.isExperimental }
                addStatement(
                    "println(%S)",
                    """
                    WARNING: The used binding version $bindingVersion for ${coords.prettyPrint} is deprecated! First stable version is $firstStableVersion.
                    """.trimIndent(),
                )
                beginControlFlow("""if (System.getenv("GITHUB_ACTIONS").toBoolean())""")
                addStatement(
                    "println(%S)",
                    """

                    ::warning title=Deprecated Binding Version Used::The used binding version $bindingVersion for ${coords.prettyPrint} is deprecated! First stable version is $firstStableVersion.
                    """.trimIndent(),
                )
                endControlFlow()
                add("\n")
            }
            if (bindingVersion.isExperimental) {
                val lastStableVersion = BindingVersion.entries.findLast { !it.isDeprecated && !it.isExperimental }
                addStatement(
                    "println(%S)",
                    """
                    WARNING: The used binding version $bindingVersion for ${coords.prettyPrint} is experimental! Last stable version is $lastStableVersion.
                    """.trimIndent(),
                )
                beginControlFlow("""if (System.getenv("GITHUB_ACTIONS").toBoolean())""")
                addStatement(
                    "println(%S)",
                    """

                    ::warning title=Experimental Binding Version Used::The used binding version $bindingVersion for ${coords.prettyPrint} is experimental! Last stable version is $lastStableVersion.
                    """.trimIndent(),
                )
                endControlFlow()
                add("\n")
            }
            add(metadata.initializerBlock(inputTypings))
        },
    )
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
