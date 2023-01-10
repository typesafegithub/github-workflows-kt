package it.krzeminski.githubactions.wrappergenerator.domain.typings

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import it.krzeminski.githubactions.wrappergenerator.domain.ActionCoords
import it.krzeminski.githubactions.wrappergenerator.generation.buildActionClassName
import it.krzeminski.githubactions.wrappergenerator.generation.toKotlinPackageName
import it.krzeminski.githubactions.wrappergenerator.generation.toPascalCase

data class IntegerWithSpecialValueTyping(val typeName: String, val specialValues: Map<String, Int>) : Typing {
    override fun getClassName(actionPackageName: String, actionClassName: String) =
        ClassName("it.krzeminski.githubactions.actions.$actionPackageName", "$actionClassName.$typeName")

    override fun asString() = ".integerValue.toString()"

    override fun buildCustomType(coords: ActionCoords): TypeSpec {
        val actionPackageName = coords.owner.toKotlinPackageName()
        val actionClassName = coords.buildActionClassName()
        val sealedClassName = this.getClassName(actionPackageName, actionClassName)
        return TypeSpec.classBuilder(this.typeName)
            .addModifiers(KModifier.SEALED)
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameter(ParameterSpec.builder("integerValue", Int::class).build())
                    .build(),
            )
            .addProperty(PropertySpec.builder("integerValue", Int::class).initializer("integerValue").build())
            .addType(
                TypeSpec.classBuilder("Value")
                    .primaryConstructor(
                        FunSpec.constructorBuilder()
                            .addParameter(ParameterSpec.builder("requestedValue", Int::class).build())
                            .build(),
                    )
                    .superclass(sealedClassName)
                    .addSuperclassConstructorParameter("requestedValue")
                    .build(),
            )
            .addTypes(
                this.specialValues.map { (name, value) ->
                    TypeSpec.objectBuilder(name.toPascalCase())
                        .superclass(sealedClassName)
                        .addSuperclassConstructorParameter("%L", value)
                        .build()
                },
            )
            .build()
    }
}
