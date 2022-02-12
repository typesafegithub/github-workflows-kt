package it.krzeminski.githubactions.wrappergenerator.domain.typings

import com.squareup.kotlinpoet.TypeName

interface Typing {
    fun getClassName(actionPackageName: String, actionClassName: String): TypeName
    fun asString(): String = ""
}
