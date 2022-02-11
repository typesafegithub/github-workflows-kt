package it.krzeminski.githubactions.wrappergenerator.domain.typings

import com.squareup.kotlinpoet.ClassName

interface Typing {
    val className: ClassName
    fun asString(): String = ""
}
