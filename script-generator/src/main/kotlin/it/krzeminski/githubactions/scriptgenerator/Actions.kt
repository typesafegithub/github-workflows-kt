package it.krzeminski.githubactions.scriptgenerator

import com.squareup.kotlinpoet.ClassName
import it.krzeminski.githubactions.actions.MissingAction
import it.krzeminski.githubactions.scriptmodel.SerializedStep
import it.krzeminski.githubactions.wrappergenerator.domain.ActionCoords
import it.krzeminski.githubactions.wrappergenerator.domain.WrapperRequest
import it.krzeminski.githubactions.wrappergenerator.domain.typings.StringTyping
import it.krzeminski.githubactions.wrappergenerator.generation.buildActionClassName
import it.krzeminski.githubactions.wrappergenerator.generation.toCamelCase
import it.krzeminski.githubactions.wrappergenerator.generation.toKotlinPackageName

fun generateAction(
    step: SerializedStep,
    coords: ActionCoords,
    wrapper: WrapperRequest?,
) = CodeBlock { builder ->

    builder.add("uses(\n")
    builder.indent()
    builder.add("name = %S,\n", step.name ?: coords.buildActionClassName())

    if (wrapper == null) {
        builder.add(generateMissingAction(coords, step))
    } else if (step.with.isEmpty()) {
        builder.add("action = %T(),\n", coords.classname())
    } else {
        builder.add(generateActionWithWrapper(coords, step, wrapper))
    }

    builder.add(generatePropertyWithLinkedMap("env", step.env))
    if (step.condition != null) {
        val (template, arg) = step.condition.orExpression()
        builder.add("condition = $template,\n", arg)
    }
    builder.unindent()
    builder.add(")\n")
}

fun generateActionWithWrapper(
    coords: ActionCoords,
    step: SerializedStep,
    wrapper: WrapperRequest,
) = CodeBlock { builder ->
    builder.add("action = %T(", coords.classname())
    builder.add("\n").indent()

    step.with.forEach { (key, value) ->
        if (value != null) {
            val typing = wrapper.inputTypings.get(key) ?: StringTyping
            val (percent, arg) = valueWithTyping(value, typing, coords)
            builder.add("%N = $percent,\n", key.toCamelCase(), arg)
        }
    }

    builder.unindent()
    builder.add("),\n")
}

fun generateMissingAction(
    coords: ActionCoords,
    step: SerializedStep,
) = CodeBlock { builder ->
    builder
        .add("action = %T(", MissingAction::class)
        .add("\n").indent()
        .add("actionOwner = %S,\n", coords.owner)
        .add("actionName = %S,\n", coords.name)
        .add("actionVersion = %S,\n", coords.version)
        .add("freeArgs = %M(\n", Members.linkedMapOf)
        .indent()

    step.with.forEach { (key, value) ->
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

