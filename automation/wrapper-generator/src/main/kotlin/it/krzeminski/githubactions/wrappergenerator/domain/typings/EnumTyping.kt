package it.krzeminski.githubactions.wrappergenerator.domain.typings

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import it.krzeminski.githubactions.wrappergenerator.domain.ActionCoords
import it.krzeminski.githubactions.wrappergenerator.generation.buildActionClassName
import it.krzeminski.githubactions.wrappergenerator.generation.toKotlinPackageName
import it.krzeminski.githubactions.wrappergenerator.generation.toPascalCase

data class EnumTyping(
    val typeName: String,
    val items: List<String>,
    val itemsNames: List<String> = items.map { it.toPascalCase() },
) : Typing {

    init {
        require(items.size == itemsNames.size) { "EnumTyping($typeName): items has ${items.size} elements while itemsNames has ${itemsNames.size} elements" }
        val notPascalCase = itemsNames.filter { it.toPascalCase() != it }
        require(notPascalCase.isEmpty()) {
            "EnumTyping($typeName): itemsNames should be in PascalCase, but got: $notPascalCase"
        }
    }
    val itemsNameMap = items.zip(itemsNames).toMap()

    override fun getClassName(actionPackageName: String, actionClassName: String): TypeName =
        ClassName("it.krzeminski.githubactions.actions.$actionPackageName", "$actionClassName.$typeName")

    override fun asString() = ".stringValue"

    override fun buildCustomType(coords: ActionCoords): TypeSpec {
        val actionPackageName = coords.owner.toKotlinPackageName()
        val actionClassName = coords.buildActionClassName()
        val sealedClassName = this.getClassName(actionPackageName, actionClassName)

        return TypeSpec.classBuilder(this.typeName)
            .addModifiers(KModifier.SEALED)
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameter(ParameterSpec.builder("stringValue", String::class).build())
                    .build(),
            )
            .addProperty(PropertySpec.builder("stringValue", String::class).initializer("stringValue").build())
            .addTypes(
                this.items.map {
                    val itemName = itemsNameMap[it]?.let {
                        if (it == "Custom") "CustomEnum" else it
                    } ?: error("FIXME: key=$it absent from $itemsNameMap")
                    TypeSpec.objectBuilder(itemName)
                        .superclass(sealedClassName)
                        .addSuperclassConstructorParameter("%S", it)
                        .build()
                },
            )
            .addType(
                TypeSpec.classBuilder("Custom")
                    .primaryConstructor(
                        FunSpec.constructorBuilder()
                            .addParameter(ParameterSpec.builder("customStringValue", String::class).build())
                            .build(),
                    )
                    .superclass(sealedClassName)
                    .addSuperclassConstructorParameter("customStringValue")
                    .build(),
            )
            .build()
    }
}
