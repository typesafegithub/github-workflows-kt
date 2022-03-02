package it.krzeminski.githubactions.wrappergenerator.domain.typings

import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import it.krzeminski.githubactions.wrappergenerator.domain.ActionCoords

class ListOfTypings(
    private val delimiter: String,
    private val typing: Typing = StringTyping,
) : Typing {
    init {
        require(delimiter != "\n") { """ListOfTypings(newline) invalid, use ListOfTypings("\\n") instead""" }
    }

    override fun getClassName(actionPackageName: String, actionClassName: String): TypeName =
        List::class.asClassName()
            .parameterizedBy(typing.getClassName(actionPackageName, actionClassName))

    override fun buildCustomType(coords: ActionCoords): TypeSpec? = typing.buildCustomType(coords)

    override fun asString() = ".joinToString(\"$delimiter\")${mapValue}"

    val mapValue: String = when (typing) {
        is StringTyping -> ""
        is IntegerTyping -> " { it.toString() }"
        is EnumTyping -> " { it.stringValue }"
        is IntegerWithSpecialValueTyping -> " { it.integerValue.toString() }"
        else -> error("ListOfTypings: typing=$typing is not supported")
    }
}
