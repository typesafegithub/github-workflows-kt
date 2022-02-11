package it.krzeminski.githubactions.wrappergenerator.domain.typings

import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asTypeName

object BooleanTyping : Typing {
    override val className: TypeName
        get() = Boolean::class.asTypeName()

    override fun asString() = ".toString()"
}
