package it.krzeminski.githubactions.wrappergenerator.domain.typings

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.TypeName

data class EnumTyping(val typeName: String, val items: List<String>) : Typing {
    override fun getClassName(actionPackageName: String, actionClassName: String): TypeName =
        ClassName("it.krzeminski.githubactions.actions.$actionPackageName", "$actionClassName.$typeName")

    override fun asString() = ".stringValue"
}
