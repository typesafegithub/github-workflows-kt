package it.krzeminski.githubactions.wrappergenerator.domain.typings

import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asTypeName

object FloatTyping : Typing {
    override fun getClassName(actionPackageName: String, actionClassName: String): TypeName =
        Float::class.asTypeName()

    override fun asString() = ".toString()"
}
