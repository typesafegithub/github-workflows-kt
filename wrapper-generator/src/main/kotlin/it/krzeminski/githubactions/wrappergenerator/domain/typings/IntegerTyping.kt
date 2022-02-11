package it.krzeminski.githubactions.wrappergenerator.domain.typings

import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asTypeName

object IntegerTyping : Typing {
    override val className: TypeName
        get() = Integer::class.asTypeName()

    override fun asString() = ".toString()"
}
