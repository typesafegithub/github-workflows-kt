package it.krzeminski.githubactions.wrappergenerator.domain.typings

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.asTypeName

object StringTyping : Typing {
    override val className: ClassName
        get() = String::class.asTypeName()
}
