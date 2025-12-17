package io.github.typesafegithub.workflows.actionbindinggenerator.typing

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.asTypeName
import io.github.typesafegithub.workflows.actionbindinggenerator.domain.ActionCoords
import io.github.typesafegithub.workflows.actionbindinggenerator.utils.toKotlinPackageName
import io.github.typesafegithub.workflows.actionbindinggenerator.utils.toPascalCase

internal fun Typing.getClassName(
    actionPackageName: String,
    actionClassName: String,
    fieldName: String,
): TypeName =
    when (this) {
        BooleanTyping -> {
            Boolean::class.asTypeName()
        }

        is EnumTyping -> {
            val typeName = this.typeName?.toPascalCase() ?: fieldName.toPascalCase()
            ClassName("io.github.typesafegithub.workflows.actions.$actionPackageName", "$actionClassName.$typeName")
        }

        FloatTyping -> {
            Float::class.asTypeName()
        }

        IntegerTyping -> {
            Integer::class.asTypeName()
        }

        is IntegerWithSpecialValueTyping -> {
            val typeName = this.typeName?.toPascalCase() ?: fieldName.toPascalCase()
            ClassName("io.github.typesafegithub.workflows.actions.$actionPackageName", "$actionClassName.$typeName")
        }

        is ListOfTypings -> {
            List::class
                .asClassName()
                .parameterizedBy(typing.getClassName(actionPackageName, actionClassName, fieldName))
        }

        StringTyping -> {
            String::class.asTypeName()
        }
    }

internal fun Typing.asString(): String =
    when (this) {
        BooleanTyping -> {
            ".toString()"
        }

        is EnumTyping -> {
            ".stringValue"
        }

        FloatTyping -> {
            ".toString()"
        }

        IntegerTyping -> {
            ".toString()"
        }

        is IntegerWithSpecialValueTyping -> {
            ".integerValue.toString()"
        }

        is ListOfTypings -> {
            val mapValue: String =
                when (typing) {
                    is StringTyping -> ""
                    is IntegerTyping -> " { it.toString() }"
                    is EnumTyping -> " { it.stringValue }"
                    is IntegerWithSpecialValueTyping -> " { it.integerValue.toString() }"
                    else -> error("ListOfTypings: typing=$typing is not supported")
                }
            ".joinToString(\"${if (delimiter == "\n") "\\n" else delimiter}\")$mapValue"
        }

        else -> {
            ""
        }
    }

internal fun Typing.buildCustomType(
    coords: ActionCoords,
    fieldName: String,
    className: String,
): TypeSpec? =
    when (this) {
        is EnumTyping -> buildEnumCustomType(coords, fieldName, className)
        is IntegerWithSpecialValueTyping -> buildIntegerWithSpecialValueCustomType(coords, fieldName, className)
        is ListOfTypings -> typing.buildCustomType(coords, fieldName, className)
        else -> null
    }

private fun EnumTyping.buildEnumCustomType(
    coords: ActionCoords,
    fieldName: String,
    className: String,
): TypeSpec {
    val sortedItems = items.sorted()
    val itemsNames =
        itemsNames ?: sortedItems
            .map { it.toPascalCase() }
            .groupBy { it }
            .values
            .flatMap { it.mapIndexed { index, name -> "$name${if (index > 0) "_${index + 1}" else ""}" } }
    val typeName = this.typeName?.toPascalCase() ?: fieldName.toPascalCase()
    val actionPackageName = coords.owner.toKotlinPackageName()
    val sealedClassName = this.getClassName(actionPackageName, className, fieldName)

    return TypeSpec
        .classBuilder(typeName)
        .addModifiers(KModifier.SEALED)
        .primaryConstructor(
            FunSpec
                .constructorBuilder()
                .addParameter(ParameterSpec.builder("stringValue", String::class).build())
                .build(),
        ).addProperty(PropertySpec.builder("stringValue", String::class).initializer("stringValue").build())
        .addTypes(
            sortedItems.mapIndexed { i, it ->
                val itemName =
                    itemsNames[i].let {
                        if (it == "Custom") "CustomEnum" else it
                    }
                TypeSpec
                    .objectBuilder(itemName)
                    .superclass(sealedClassName)
                    .addSuperclassConstructorParameter("%S", it)
                    .build()
            },
        ).addType(
            TypeSpec
                .classBuilder("Custom")
                .primaryConstructor(
                    FunSpec
                        .constructorBuilder()
                        .addParameter(ParameterSpec.builder("customStringValue", String::class).build())
                        .build(),
                ).superclass(sealedClassName)
                .addSuperclassConstructorParameter("customStringValue")
                .build(),
        ).build()
}

private fun IntegerWithSpecialValueTyping.buildIntegerWithSpecialValueCustomType(
    coords: ActionCoords,
    fieldName: String,
    className: String,
): TypeSpec {
    val typeName = this.typeName?.toPascalCase() ?: fieldName.toPascalCase()
    val actionPackageName = coords.owner.toKotlinPackageName()
    val sealedClassName = this.getClassName(actionPackageName, className, fieldName)
    return TypeSpec
        .classBuilder(typeName)
        .addModifiers(KModifier.SEALED)
        .primaryConstructor(
            FunSpec
                .constructorBuilder()
                .addParameter(ParameterSpec.builder("integerValue", Int::class).build())
                .build(),
        ).addProperty(PropertySpec.builder("integerValue", Int::class).initializer("integerValue").build())
        .addType(
            TypeSpec
                .classBuilder("Value")
                .primaryConstructor(
                    FunSpec
                        .constructorBuilder()
                        .addParameter(ParameterSpec.builder("requestedValue", Int::class).build())
                        .build(),
                ).superclass(sealedClassName)
                .addSuperclassConstructorParameter("requestedValue")
                .build(),
        ).addTypes(
            this.specialValues.map { (name, value) ->
                TypeSpec
                    .objectBuilder(name.toPascalCase())
                    .superclass(sealedClassName)
                    .addSuperclassConstructorParameter("%L", value)
                    .build()
            },
        ).build()
}
