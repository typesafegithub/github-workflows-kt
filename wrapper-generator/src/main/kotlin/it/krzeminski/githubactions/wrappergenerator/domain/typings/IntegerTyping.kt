package it.krzeminski.githubactions.wrappergenerator.domain.typings

import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asTypeName

object IntegerTyping : Typing {
    override fun getClassName(actionPackageName: String, actionClassName: String): TypeName =
        Integer::class.asTypeName()

    override fun asString() = ".toString()"
}
