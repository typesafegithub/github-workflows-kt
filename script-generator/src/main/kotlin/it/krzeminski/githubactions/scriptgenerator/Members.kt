package it.krzeminski.githubactions.scriptgenerator

import com.squareup.kotlinpoet.MemberName

object Members {
    val workflow = MemberName("$PACKAGE.dsl", "workflow")
    val linkedMapOf = MemberName("kotlin.collections", "linkedMapOf")
}