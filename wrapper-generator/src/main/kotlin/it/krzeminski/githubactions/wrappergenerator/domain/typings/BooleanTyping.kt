package it.krzeminski.githubactions.wrappergenerator.domain.typings

import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asTypeName

object BooleanTyping : Typing {
    override fun getClassName(actionPackageName: String, actionClassName: String): TypeName =
        Boolean::class.asTypeName()

    override fun asString() = ".toString()"
}
