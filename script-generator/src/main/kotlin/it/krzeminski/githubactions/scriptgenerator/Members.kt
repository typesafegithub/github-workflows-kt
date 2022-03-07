package it.krzeminski.githubactions.scriptgenerator

import com.squareup.kotlinpoet.MemberName

object Members {
    val expr = MemberName("$PACKAGE.dsl", "expr")
    val workflow = MemberName("$PACKAGE.dsl", "workflow")
    val linkedMapOf = MemberName("kotlin.collections", "linkedMapOf")
}
