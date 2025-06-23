package io.github.typesafegithub.workflows

import com.google.devtools.ksp.processing.CodeGenerator
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ksp.writeTo

fun generateVersionInfo(
    codeGenerator: CodeGenerator,
    libraryVersion: String,
) {
    val funSpec =
        FunSpec
            .builder(name = "getLibraryVersion")
            .addModifiers(KModifier.INTERNAL)
            .returns(String::class)
            .addCode(CodeBlock.of("return \"$libraryVersion\""))
            .build()

    val fileSpec =
        FileSpec
            .builder(packageName = "io.github.typesafegithub.workflows.internal", fileName = "VersionInfo.kt")
            .addFunction(funSpec)
            .build()

    fileSpec.writeTo(codeGenerator = codeGenerator, aggregating = false)
}
