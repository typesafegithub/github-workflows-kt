package it.krzeminski.githubactions.wrappergenerator.domain.typings

import com.squareup.kotlinpoet.TypeName

interface Typing {
    val className: TypeName
    fun asString(): String = ""
}
