package it.krzeminski.githubactions.scriptgenerator

import com.squareup.kotlinpoet.FileSpec
import it.krzeminski.githubactions.scriptmodel.Workflow

fun Workflow.toKotlin() =
    FileSpec.builder("", "$name.kts")

        .build()