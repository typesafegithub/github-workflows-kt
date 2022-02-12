package it.krzeminski.githubactions.wrappergenerator.domain.typings

import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asTypeName

object StringTyping : Typing {
    override fun getClassName(actionPackageName: String, actionClassName: String): TypeName =
        String::class.asTypeName()
}
