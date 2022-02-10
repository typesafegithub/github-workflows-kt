package it.krzeminski.githubactions.wrappergenerator.generation

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asTypeName
import it.krzeminski.githubactions.wrappergenerator.domain.ActionCoords
import it.krzeminski.githubactions.wrappergenerator.metadata.Metadata
import it.krzeminski.githubactions.wrappergenerator.metadata.fetchMetadata
import java.util.Locale

data class Wrapper(
    val kotlinCode: String,
    val filePath: String,
)

fun ActionCoords.generateWrapper(
    fetchMetadataImpl: ActionCoords.() -> Metadata = { fetchMetadata() },
): Wrapper {
    val metadata = fetchMetadataImpl()
    val actionWrapperSourceCode = generateActionWrapperSourceCode(metadata, this)
    return Wrapper(
        kotlinCode = actionWrapperSourceCode,
        filePath = "library/src/main/kotlin/it/krzeminski/githubactions/actions/${owner.toKotlinPackageName()}/${this.buildActionClassName()}.kt",
    )
}

private fun generateActionWrapperSourceCode(metadata: Metadata, coords: ActionCoords): String {
    val fileSpec = FileSpec.builder("it.krzeminski.githubactions.actions.${coords.owner.toKotlinPackageName()}", coords.buildActionClassName())
        .addType(generateActionClass(metadata, coords))
        .indent("    ")
        .build()
    return buildString {
        fileSpec.writeTo(this)
    }
}

private fun generateActionClass(metadata: Metadata, coords: ActionCoords): TypeSpec =
    TypeSpec.classBuilder(coords.buildActionClassName())
        .addKdoc(actionKdoc(metadata, coords))
        .inheritsFromAction(coords)
        .primaryConstructor(metadata.primaryConstructor())
        .addFunction(metadata.buildToYamlArgumentsFunction())
        .build()

private fun Metadata.buildToYamlArgumentsFunction() =
    FunSpec.builder("toYamlArguments")
        .addModifiers(KModifier.OVERRIDE)
        .addCode(
            CodeBlock.Builder().apply {
                add("return linkedMapOf(\n")
                indent()
                inputs.forEach { add("%S to %L,\n", it.key, it.key.toCamelCase()) }
                unindent()
                add(")")
            }.build()
        )
        .build()

private fun TypeSpec.Builder.inheritsFromAction(coords: ActionCoords): TypeSpec.Builder = this
    .superclass(ClassName("it.krzeminski.githubactions.actions", "Action"))
    .addSuperclassConstructorParameter("%S", coords.owner)
    .addSuperclassConstructorParameter("%S", coords.name)
    .addSuperclassConstructorParameter("%S", coords.version)

private fun ActionCoords.buildActionClassName() =
    "${name.toPascalCase()}${version.replaceFirstChar { it.titlecase(Locale.getDefault()) }}"

private fun Metadata.primaryConstructor(): FunSpec {
    return FunSpec.constructorBuilder()
        .addParameters(
            inputs.map { (key, input) ->
                ParameterSpec.builder(key.toCamelCase(), String::class.asTypeName())
                    .addKdoc(input.description)
                    .build()
            }
        )
        .build()
}

private fun actionKdoc(metadata: Metadata, coords: ActionCoords) =
    """
        Action: ${metadata.name}

        ${metadata.description}

        http://github.com/${coords.owner}/${coords.name}
    """.trimIndent()
