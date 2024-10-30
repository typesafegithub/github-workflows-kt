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
import com.squareup.kotlinpoet.WildcardTypeName
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.asTypeName
import com.squareup.kotlinpoet.buildCodeBlock
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionTypings
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.MetadataRevision
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.TypingActualSource
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.fullName
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.isTopLevel
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.prettyPrint
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.subName
import io.github.typesafegithub.workflows.actionbindinggenerator.generation.Properties.CUSTOM_INPUTS
import io.github.typesafegithub.workflows.actionbindinggenerator.generation.Properties.CUSTOM_VERSION
import io.github.typesafegithub.workflows.actionbindinggenerator.metadata.Input
import io.github.typesafegithub.workflows.actionbindinggenerator.metadata.Metadata
import io.github.typesafegithub.workflows.actionbindinggenerator.metadata.fetchMetadata
import io.github.typesafegithub.workflows.actionbindinggenerator.metadata.shouldBeRequiredInBinding
import io.github.typesafegithub.workflows.actionbindinggenerator.typing.ListOfTypings
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

public data class ActionBinding(
    val kotlinCode: String,
    val filePath: String,
    val className: String,
    val packageName: String,
    val typingActualSource: TypingActualSource?,
)

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
    bindingVersion: BindingVersion = V1,
    metadataRevision: MetadataRevision,
    metadata: Metadata? = null,
    typings: ActionTypings? = null,
): List<ActionBinding> {
    val metadataResolved = metadata ?: this.fetchMetadata(metadataRevision) ?: return emptyList()
    val metadataProcessed = metadataResolved.removeDeprecatedInputsIfNameClash()

    val typingsResolved = typings ?: this.provideTypes(metadataRevision)

    val packageName = owner.toKotlinPackageName()
    val className = this.buildActionClassName()
    val classNameUntyped = "${className}_Untyped"

    val actionBindingSourceCodeUntyped =
        generateActionBindingSourceCode(
            metadata = metadataProcessed,
            coords = this,
            bindingVersion = bindingVersion,
            inputTypings = emptyMap(),
            outputTypings = emptyMap(),
            className = classNameUntyped,
            untypedClass = true,
            replaceWith = typingsResolved.source?.let { CodeBlock.of("ReplaceWith(%S)", className) },
        )

    return listOfNotNull(
        ActionBinding(
            kotlinCode = actionBindingSourceCodeUntyped,
            filePath = "io/github/typesafegithub/workflows/actions/$packageName/$classNameUntyped.kt",
            className = classNameUntyped,
            packageName = packageName,
            typingActualSource = null,
        ),
        typingsResolved.source?.let {
            val actionBindingSourceCode =
                generateActionBindingSourceCode(
                    metadata = metadataProcessed,
                    coords = this,
                    bindingVersion = bindingVersion,
                    inputTypings = typingsResolved.inputTypings,
                    outputTypings = typingsResolved.outputTypings,
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
    outputTypings: Map<String, Typing>,
    className: String,
    untypedClass: Boolean = false,
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
                    bindingVersion,
                    inputTypings,
                    outputTypings,
                    className,
                    untypedClass,
                    replaceWith,
                ),
            ).addSuppressAnnotation(bindingVersion, metadata, untypedClass)
            .indent("    ")
            .build()
    return buildString {
        fileSpec.writeTo(this)
    }
}

private fun FileSpec.Builder.addSuppressAnnotation(
    bindingVersion: BindingVersion,
    metadata: Metadata,
    untypedClass: Boolean,
): FileSpec.Builder {
    val suppress = AnnotationSpec.builder(Suppress::class.asClassName())
    suppress.addMember(CodeBlock.of("%S", "DataClassPrivateConstructor"))
    suppress.addMember(CodeBlock.of("%S", "UNUSED_PARAMETER"))
    val isDeprecatedInputUsed = metadata.inputs.values.any { it.deprecationMessage.isNullOrBlank().not() }
    if (((bindingVersion >= V2) && !untypedClass && metadata.inputs.isNotEmpty()) || isDeprecatedInputUsed) {
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
    outputTypings: Map<String, Typing>,
    className: String,
    untypedClass: Boolean,
    replaceWith: CodeBlock?,
): TypeSpec =
    TypeSpec
        .classBuilder(className)
        .addModifiers(KModifier.DATA)
        .addKdocIfNotEmpty(actionKdoc(metadata, coords, untypedClass))
        .deprecateBindingVersion(bindingVersion)
        .replaceWith(bindingVersion, replaceWith)
        .addClassConstructorAnnotation()
        .inheritsFromRegularAction(coords, metadata, className)
        .primaryConstructor(metadata.primaryConstructor(bindingVersion, inputTypings, coords, className, untypedClass))
        .properties(bindingVersion, metadata, coords, inputTypings, className, untypedClass)
        .addInitializerBlock(metadata, bindingVersion, coords, inputTypings, untypedClass)
        .addFunction(metadata.secondaryConstructor(bindingVersion, inputTypings, coords, className, untypedClass))
        .addFunction(metadata.buildToYamlArgumentsFunction(bindingVersion, inputTypings, untypedClass))
        .addCustomTypes(inputTypings, outputTypings, coords, className)
        .addOutputClassIfNecessary(bindingVersion, metadata, coords, outputTypings)
        .addBuildOutputObjectFunctionIfNecessary(metadata)
        .build()

private fun TypeSpec.Builder.addCustomTypes(
    inputTypings: Map<String, Typing>,
    outputTypings: Map<String, Typing>,
    coords: ActionCoords,
    className: String,
): TypeSpec.Builder {
    (inputTypings.entries + outputTypings.entries)
        .mapNotNull { (inputName, typing) -> typing.buildCustomType(coords, inputName, className) }
        .distinctBy { it.name }
        .forEach { addType(it) }
    return this
}

private val Expression = ClassName("io.github.typesafegithub.workflows.domain", "Expression")

private fun TypeSpec.Builder.properties(
    bindingVersion: BindingVersion,
    metadata: Metadata,
    coords: ActionCoords,
    inputTypings: Map<String, Typing>,
    className: String,
    untypedClass: Boolean,
): TypeSpec.Builder {
    metadata.inputs.forEach { (key, input) ->
        val typedInput = inputTypings.containsKey(key)
        val propertyBaseName = key.toCamelCase()
        if (!untypedClass && typedInput) {
            addProperty(
                PropertySpec
                    .builder(
                        propertyBaseName,
                        inputTypings
                            .getInputType(
                                bindingVersion,
                                key,
                                input,
                                coords,
                                className,
                                untypedClass = false,
                                typedInput = true,
                            ),
                    ).initializer("%N", propertyBaseName)
                    .annotateDeprecated(bindingVersion, input)
                    .build(),
            )
        }
        addProperty(
            PropertySpec
                .builder(
                    "${propertyBaseName}_Untyped",
                    null
                        .getInputType(
                            bindingVersion,
                            key,
                            input,
                            coords,
                            className,
                            untypedClass,
                            typedInput,
                        ),
                ).initializer("%N", "${propertyBaseName}_Untyped")
                .annotateDeprecated(bindingVersion, input, typedInput)
                .build(),
        )
        if (bindingVersion >= V2) {
            addProperty(
                PropertySpec
                    .builder(
                        "${propertyBaseName}Expression",
                        Expression
                            .parameterizedBy(
                                inputTypings
                                    .getInputType(
                                        bindingVersion,
                                        key,
                                        input,
                                        coords,
                                        className,
                                        untypedClass,
                                        typedInput,
                                    ).copy(nullable = false),
                            ).copy(nullable = true),
                    ).initializer("%N", "${propertyBaseName}Expression")
                    .annotateDeprecated(bindingVersion, input)
                    .build(),
            )
            if (inputTypings[key] is ListOfTypings) {
                addProperty(
                    PropertySpec
                        .builder(
                            "${propertyBaseName}Expressions",
                            List::class
                                .asClassName()
                                .parameterizedBy(
                                    Expression
                                        .parameterizedBy(
                                            (inputTypings[key] as ListOfTypings)
                                                .typing
                                                .getClassName(
                                                    actionPackageName = coords.owner.toKotlinPackageName(),
                                                    actionClassName = coords.buildActionClassName(),
                                                    fieldName = key,
                                                ),
                                        ),
                                ).copy(nullable = true),
                        ).initializer("%N", "${propertyBaseName}Expressions")
                        .annotateDeprecated(bindingVersion, input)
                        .build(),
                )
            }
        }
    }
    addProperty(PropertySpec.builder(CUSTOM_INPUTS, Types.mapStringString).initializer(CUSTOM_INPUTS).build())
    addProperty(PropertySpec.builder(CUSTOM_VERSION, Types.nullableString).initializer(CUSTOM_VERSION).build())
    return this
}

private val OutputsBase = ClassName("io.github.typesafegithub.workflows.domain.actions", "Action", "Outputs")

private fun TypeSpec.Builder.addOutputClassIfNecessary(
    bindingVersion: BindingVersion,
    metadata: Metadata,
    coords: ActionCoords,
    outputTypings: Map<String, Typing>,
): TypeSpec.Builder {
    if (metadata.outputs.isEmpty()) {
        return this
    }

    val stepIdConstructorParameter =
        ParameterSpec
            .builder("stepId", String::class)
            .build()
    val propertiesFromOutputs =
        if (bindingVersion <= V1) {
            metadata.outputs.map { (key, value) ->
                PropertySpec
                    .builder(key.toCamelCase(), String::class)
                    .initializer("\"steps.\$stepId.outputs.$key\"")
                    .addKdocIfNotEmpty(value.description.escapedForComments.removeTrailingWhitespacesForEachLine())
                    .build()
            }
        } else {
            metadata.outputs.flatMap { (key, value) ->
                val outputClassName =
                    outputTypings[key]
                        ?.getClassName(coords.owner.toKotlinPackageName(), coords.buildActionClassName(), key)
                val propertyBaseName = key.toCamelCase()
                listOfNotNull(
                    outputClassName?.let {
                        PropertySpec
                            .builder(
                                propertyBaseName,
                                Expression.parameterizedBy(it),
                            ).initializer("%T(\"steps.\$stepId.outputs.%L\")", Expression, key)
                            .addKdocIfNotEmpty(value.description.escapedForComments.removeTrailingWhitespacesForEachLine())
                            .build()
                    },
                    PropertySpec
                        .builder(
                            "${propertyBaseName}_Untyped",
                            Expression.parameterizedBy(Any::class.asClassName()),
                        ).initializer("%T(\"steps.\$stepId.outputs.%L\")", Expression, key)
                        .addKdocIfNotEmpty(value.description.escapedForComments.removeTrailingWhitespacesForEachLine())
                        .build(),
                )
            }
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
            .addCode("return Outputs(stepId)")
            .build(),
    )

    return this
}

private fun PropertySpec.Builder.annotateDeprecated(
    bindingVersion: BindingVersion,
    input: Input,
    untypedSibling: Boolean = false,
) = apply {
    if (((bindingVersion >= V2) && untypedSibling) || (input.deprecationMessage != null)) {
        addAnnotation(
            AnnotationSpec
                .builder(Deprecated::class.asClassName())
                .addMember(
                    "%S",
                    input.deprecationMessage ?: "Use the typed property or expression property instead",
                ).build(),
        )
    }
}

private fun Metadata.buildToYamlArgumentsFunction(
    bindingVersion: BindingVersion,
    inputTypings: Map<String, Typing>,
    untypedClass: Boolean,
) = FunSpec
    .builder("toYamlArguments")
    .addModifiers(KModifier.OVERRIDE)
    .returns(LinkedHashMap::class.parameterizedBy(String::class, String::class))
    .addAnnotation(
        AnnotationSpec
            .builder(Suppress::class)
            .addMember("%S", "SpreadOperator")
            .build(),
    ).addCode(linkedMapOfInputs(bindingVersion, inputTypings, untypedClass))
    .build()

private fun Metadata.linkedMapOfInputs(
    bindingVersion: BindingVersion,
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
            val propertyBaseName = key.toCamelCase()
            if (!untypedClass && inputTypings.containsKey(key)) {
                val asStringCode = inputTypings.getInputTyping(key).asString()
                add("%N?.let { %S to it$asStringCode },\n", propertyBaseName, key)
            }
            val asStringCode = null.getInputTyping(key).asString()
            if (value.shouldBeRequiredInBinding() &&
                !value.shouldBeNullable(
                    bindingVersion,
                    untypedClass,
                    inputTypings.containsKey(key),
                )
            ) {
                add("%S to %N$asStringCode,\n", key, "${propertyBaseName}_Untyped")
            } else {
                add("%N?.let { %S to it$asStringCode },\n", "${propertyBaseName}_Untyped", key)
            }
            if (bindingVersion >= V2) {
                add("%N?.let { %S to it.expressionString },\n", "${propertyBaseName}Expression", key)
                if (inputTypings[key] is ListOfTypings) {
                    add(
                        "%N?.let { %S to it.joinToString(%S, transform = %T::expressionString) },\n",
                        "${propertyBaseName}Expressions",
                        key,
                        " ",
                        Expression.parameterizedBy(
                            WildcardTypeName.producerOf(Any::class.asClassName().copy(nullable = true)),
                        ),
                    )
                }
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
        .addSuperclassConstructorParameter("_customVersion ?: %S", coords.version)
}

private fun Metadata.primaryConstructor(
    bindingVersion: BindingVersion,
    inputTypings: Map<String, Typing>,
    coords: ActionCoords,
    className: String,
    untypedClass: Boolean,
): FunSpec =
    FunSpec
        .constructorBuilder()
        .addModifiers(KModifier.PRIVATE)
        .addParameters(buildCommonConstructorParameters(bindingVersion, inputTypings, coords, className, untypedClass))
        .build()

private fun Metadata.secondaryConstructor(
    bindingVersion: BindingVersion,
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
        ).addParameters(buildCommonConstructorParameters(bindingVersion, inputTypings, coords, className, untypedClass))
        .callThisConstructor(
            inputs
                .keys
                .flatMap { inputName ->
                    val propertyBaseName = inputName.toCamelCase()
                    val typedInput = inputTypings.containsKey(inputName)
                    listOfNotNull(
                        untypedClass.takeIf { !it && typedInput }?.let { propertyBaseName },
                        "${propertyBaseName}_Untyped",
                        if (bindingVersion >= V2) "${propertyBaseName}Expression" else null,
                        if (bindingVersion >= V2) {
                            (inputTypings[inputName] as? ListOfTypings)?.let { "${propertyBaseName}Expressions" }
                        } else {
                            null
                        },
                    )
                }.plus(CUSTOM_INPUTS)
                .plus(CUSTOM_VERSION)
                .map { CodeBlock.of("%1N = %1N", it) },
        ).build()

private fun Metadata.buildCommonConstructorParameters(
    bindingVersion: BindingVersion,
    inputTypings: Map<String, Typing>,
    coords: ActionCoords,
    className: String,
    untypedClass: Boolean,
): List<ParameterSpec> =
    inputs
        .flatMap { (key, input) ->
            val propertyBaseName = key.toCamelCase()
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
                            propertyBaseName,
                            inputTypings
                                .getInputType(
                                    bindingVersion,
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
                        "${propertyBaseName}_Untyped",
                        null
                            .getInputType(
                                bindingVersion,
                                key,
                                input,
                                coords,
                                className,
                                untypedClass,
                                typedInput,
                            ),
                    ).defaultValueIfNullable(bindingVersion, input, untypedClass, typedInput)
                    .addKdocIfNotEmpty(kdoc)
                    .build(),
                bindingVersion.takeIf { it >= V2 }?.let {
                    ParameterSpec
                        .builder(
                            "${propertyBaseName}Expression",
                            Expression
                                .parameterizedBy(
                                    inputTypings
                                        .getInputType(
                                            bindingVersion,
                                            key,
                                            input,
                                            coords,
                                            className,
                                            untypedClass,
                                            typedInput,
                                        ).copy(nullable = false),
                                ).copy(nullable = true),
                        ).defaultValue("null")
                        .addKdocIfNotEmpty(kdoc)
                        .build()
                },
                bindingVersion.takeIf { it >= V2 }?.let {
                    (inputTypings[key] as? ListOfTypings)?.let { listOfTypings ->
                        ParameterSpec
                            .builder(
                                "${propertyBaseName}Expressions",
                                List::class
                                    .asClassName()
                                    .parameterizedBy(
                                        Expression
                                            .parameterizedBy(
                                                listOfTypings
                                                    .typing
                                                    .getClassName(
                                                        actionPackageName = coords.owner.toKotlinPackageName(),
                                                        actionClassName = coords.buildActionClassName(),
                                                        fieldName = key,
                                                    ),
                                            ),
                                    ).copy(nullable = true),
                            ).defaultValue("null")
                            .addKdocIfNotEmpty(kdoc)
                            .build()
                    }
                },
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

private fun ParameterSpec.Builder.defaultValueIfNullable(
    bindingVersion: BindingVersion,
    input: Input,
    untypedClass: Boolean,
    typedInput: Boolean,
): ParameterSpec.Builder {
    if (input.shouldBeNullable(bindingVersion, untypedClass, typedInput)) {
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
        if (bindingVersion <= V1) {
            untypedClass || metadata.inputs.isEmpty() || metadata.inputs.none { inputTypings.containsKey(it.key) }
        } else {
            metadata.inputs.isEmpty()
        }
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
            add(metadata.initializerBlock(bindingVersion, inputTypings, untypedClass))
        },
    )
    return this
}

private fun Metadata.initializerBlock(
    bindingVersion: BindingVersion,
    inputTypings: Map<String, Typing>,
    untypedClass: Boolean,
) = buildCodeBlock {
    var first = true
    inputs
        .filter { (bindingVersion >= V2) || inputTypings.containsKey(it.key) }
        .forEach { (key, input) ->
            if (!first) {
                add("\n")
            }
            first = false
            val typedInput = inputTypings.containsKey(key)
            val inputProperties =
                listOfNotNull(
                    if (!untypedClass && typedInput) "%1N" else null,
                    "%1L_Untyped",
                    if (bindingVersion >= V2) "%1LExpression" else null,
                    if ((bindingVersion >= V2) && (inputTypings[key] is ListOfTypings)) "%1LExpressions" else null,
                )
            val inputPropertyNames =
                listOfNotNull(
                    if (!untypedClass && typedInput) "%1L" else null,
                    "%1L_Untyped",
                    if (bindingVersion >= V2) "%1LExpression" else null,
                    if ((bindingVersion >= V2) && (inputTypings[key] is ListOfTypings)) "%1LExpressions" else null,
                )
            val propertyBaseName = key.toCamelCase()
            beginControlFlow("require(listOfNotNull(${inputProperties.joinToString()}).size <= 1)", propertyBaseName)
            addStatement(
                "%S",
                "Only one of ${
                    CodeBlock.of(
                        "${inputPropertyNames.dropLast(1).joinToString()}, and ${inputPropertyNames.last()}",
                        propertyBaseName,
                    )
                } must be set, but not multiple",
            )
            endControlFlow()
            if (input.shouldBeRequiredInBinding()) {
                beginControlFlow("require(${inputProperties.joinToString(" || ") { "($it != null)" }})", propertyBaseName)
                addStatement(
                    "%S",
                    "Either ${
                        CodeBlock.of(
                            "${inputPropertyNames.dropLast(1).joinToString()}, or ${inputPropertyNames.last()}",
                            propertyBaseName,
                        )
                    } must be set, one of them is required",
                )
                endControlFlow()
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
    bindingVersion: BindingVersion,
    key: String,
    input: Input,
    coords: ActionCoords,
    className: String,
    untypedClass: Boolean,
    typedInput: Boolean,
) = getInputTyping(key)
    .getClassName(coords.owner.toKotlinPackageName(), className, key)
    .copy(nullable = input.shouldBeNullable(bindingVersion, untypedClass, typedInput))

private fun Input.shouldBeNullable(
    bindingVersion: BindingVersion,
    untypedClass: Boolean,
    typedInput: Boolean,
) = // untyped class => according to required status
    (untypedClass && !shouldBeRequiredInBinding()) ||
        // typed class, typed input => null
        // typed class, untyped input => according to required status
        (!untypedClass && (typedInput || !shouldBeRequiredInBinding())) ||
        // expression siblings
        (bindingVersion >= V2)

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
