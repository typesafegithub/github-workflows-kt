package it.krzeminski.githubactions.wrappergenerator.domain.typings

import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import it.krzeminski.githubactions.wrappergenerator.domain.ActionCoords

sealed interface Typing {
    fun getClassName(actionPackageName: String, actionClassName: String): TypeName
    fun asString(): String = ""
    fun buildCustomType(coords: ActionCoords): TypeSpec? = null
}
