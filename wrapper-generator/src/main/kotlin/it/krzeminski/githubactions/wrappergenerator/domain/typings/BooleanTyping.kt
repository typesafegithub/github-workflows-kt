package it.krzeminski.githubactions.wrappergenerator.domain.typings

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.asTypeName

object BooleanTyping : Typing {
    override val className: ClassName
        get() = Boolean::class.asTypeName()

    override fun asString() = ".toString()"
}
