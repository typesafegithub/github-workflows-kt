package it.krzeminski.githubactions.scriptgenerator

import com.squareup.kotlinpoet.ClassName
import it.krzeminski.githubactions.actions.MissingAction
import it.krzeminski.githubactions.scriptmodel.SerializedStep
import it.krzeminski.githubactions.wrappergenerator.domain.ActionCoords
import it.krzeminski.githubactions.wrappergenerator.domain.typings.StringTyping
import it.krzeminski.githubactions.wrappergenerator.domain.typings.Typing
import it.krzeminski.githubactions.wrappergenerator.generation.buildActionClassName
import it.krzeminski.githubactions.wrappergenerator.generation.toCamelCase
import it.krzeminski.githubactions.wrappergenerator.generation.toKotlinPackageName

fun SerializedStep.generateAction(
    coords: ActionCoords,
    inputTypings: Map<String, Typing>?,
) = CodeBlock { builder ->

    builder.add("uses(\n")
    builder.indent()
    builder.add("name = %S,\n", name ?: coords.buildActionClassName())

    if (inputTypings == null) {
        builder.add(generateMissingAction(coords))
    } else if (with.isEmpty()) {
        builder.add("action = %T(),\n", coords.classname())
    } else {
        builder.add(generateActionWithWrapper(coords, inputTypings))
    }

    builder.add(generatePropertyWithLinkedMap("env", env))
    if (condition != null) {
        val (template, arg) = condition.orExpression()
        builder.add("condition = $template,\n", arg)
    }
    builder.unindent()
    builder.add(")\n")
}

fun SerializedStep.generateActionWithWrapper(
    coords: ActionCoords,
    inputTypings: Map<String, Typing>?,
) = CodeBlock { builder ->
    builder.add("action = %T(", coords.classname())
    builder.add("\n").indent()

    with.forEach { (key, value) ->
        if (value != null) {
            val typing = inputTypings?.get(key) ?: StringTyping
            val (percent, arg) = valueWithTyping(value, typing, coords)
            builder.add("%N = $percent,\n", key.toCamelCase(), arg)
        }
    }

    builder.unindent()
    builder.add("),\n")
}

fun SerializedStep.generateMissingAction(
    coords: ActionCoords,
) = CodeBlock { builder ->
    builder
        .add("action = %T(", MissingAction::class)
        .add("\n").indent()
        .add("actionOwner = %S,\n", coords.owner)
        .add("actionName = %S,\n", coords.name)
        .add("actionVersion = %S,\n", coords.version)
        .add("freeArgs = %M(\n", Members.linkedMapOf)
        .indent()

    with.forEach { (key, value) ->
        builder.add("%S to %S,\n", key, value)
    }

    builder
        .unindent()
        .add(")\n")
        .unindent()
        .add("),\n")
}

fun ActionCoords(yaml: String): ActionCoords {
    val (owner, name, version) = yaml.split("/", "@")
    return ActionCoords(owner, name, version)
}

fun ActionCoords.classname() =
    ClassName("$PACKAGE.actions.${owner.toKotlinPackageName()}", buildActionClassName())
