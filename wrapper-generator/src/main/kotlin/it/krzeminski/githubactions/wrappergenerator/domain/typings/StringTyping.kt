package it.krzeminski.githubactions.wrappergenerator.domain.typings

import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asTypeName

object StringTyping : Typing {
    override val className: TypeName
        get() = String::class.asTypeName()
}
