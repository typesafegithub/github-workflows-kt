package it.krzeminski.githubactions.scriptgenerator

import it.krzeminski.githubactions.wrappergenerator.domain.ActionCoords
import it.krzeminski.githubactions.wrappergenerator.domain.typings.BooleanTyping
import it.krzeminski.githubactions.wrappergenerator.domain.typings.EnumTyping
import it.krzeminski.githubactions.wrappergenerator.domain.typings.IntegerWithSpecialValueTyping
import it.krzeminski.githubactions.wrappergenerator.domain.typings.ListOfTypings
import it.krzeminski.githubactions.wrappergenerator.domain.typings.Typing
import it.krzeminski.githubactions.wrappergenerator.generation.buildActionClassName
import it.krzeminski.githubactions.wrappergenerator.generation.toPascalCase

data class TemplateArg(
    val template: String, // for example "%S" or "%L" or "function(%S)"
    val arg: String // arg for CodeBlock.of("something $template", arg)
)

fun String.orExpression(): TemplateArg {
    val input = trim()
    if (input.startsWith(EXPR_PREFIX) && endsWith(EXPR_SUFFIX)) {
        val expression = input.removeSuffix(EXPR_SUFFIX).removePrefix(EXPR_PREFIX).trim()
        return TemplateArg("expr(%S)", expression)
    } else {
        return TemplateArg("%S", input)
    }
}

private const val EXPR_PREFIX = "\${{"
private const val EXPR_SUFFIX = "}}"

fun valueWithTyping(value: String, typing: Typing, coords: ActionCoords): TemplateArg {
    val classname = coords.buildActionClassName()
    return when (typing) {
        is EnumTyping ->
            TemplateArg("%L", "$classname.${typing.typeName}.${value.toPascalCase()}")

        is BooleanTyping ->
            TemplateArg("%L", value)

        is IntegerWithSpecialValueTyping ->
            TemplateArg("%L", "$classname.${typing.typeName}.Value($value)")

        is ListOfTypings -> {
            val delimeter = if (typing.delimiter == "\\n") "\n" else typing.delimiter

            val listString = value.trim().split(delimeter).joinToString(
                prefix = "listOf(", postfix = ")", transform = {
                    val (percent, transformed) = valueWithTyping(it, typing.typing, coords)
                    if (percent == "%S") "$QUOTE$transformed$QUOTE" else transformed
                }
            )
            TemplateArg("%L", listString)
        }
        else ->
            TemplateArg("%S", value)
    }
}
