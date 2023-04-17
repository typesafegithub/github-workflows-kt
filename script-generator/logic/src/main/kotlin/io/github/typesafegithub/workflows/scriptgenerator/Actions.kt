package io.github.typesafegithub.workflows.scriptgenerator

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import io.github.typesafegithub.workflows.actionsmetadata.model.ActionCoords
import io.github.typesafegithub.workflows.actionsmetadata.model.StringTyping
import io.github.typesafegithub.workflows.actionsmetadata.model.Typing
import io.github.typesafegithub.workflows.domain.actions.CustomAction
import io.github.typesafegithub.workflows.scriptmodel.YamlStep
import io.github.typesafegithub.workflows.wrappergenerator.generation.buildActionClassName
import io.github.typesafegithub.workflows.wrappergenerator.generation.toCamelCase
import io.github.typesafegithub.workflows.wrappergenerator.generation.toKotlinPackageName

fun YamlStep.generateAction(
    coords: ActionCoords,
    inputTypings: Map<String, Typing>?,
    _customVersion: String?,
) = CodeBlock { builder ->

    builder.add("uses(\n")
    builder.indent()
    builder.add("name = %S,\n", name ?: coords.buildActionClassName())

    if (inputTypings == null) {
        builder.add(generateCustomAction(coords))
    } else if (with.isEmpty() && _customVersion == null) {
        builder.add("action = %T(),\n", coords.classname())
    } else {
        builder.add(generateActionWithWrapper(coords, inputTypings, _customVersion))
    }

    builder.add(
        env.joinToCode(
            prefix = CodeBlock.of("%L = linkedMapOf(\n", "env"),
            postfix = "),",
            ifEmpty = CodeBlock.EMPTY,
        ) { key, value ->
            value?.let {
                CodeBlock { builder ->
                    builder.add(CodeBlock.of("%S to ", key))
                        .add(value.orExpression())
                }
            }
        },
    )
    if (condition != null) {
        builder
            .add("condition = ")
            .add(condition.orExpression())
            .add(",\n")
    }
    builder.unindent()
    builder.add(")\n")
}

fun YamlStep.generateActionWithWrapper(
    coords: ActionCoords,
    inputTypings: Map<String, Typing>?,
    _customVersion: String?,
): CodeBlock {
    val kclass = Class.forName(coords.classname().reflectionName()).kotlin
    val kclassProperties = kclass.members.map { it.name }.toSet()

    val existingActionProperties = with
        .filterKeys { key -> key.toCamelCase() in kclassProperties }
        .joinToCode(
            prefix = CodeBlock.of("action = %T(", coords.classname()),
            postfix = "",
            newLineAtEnd = false,
        ) { key, value ->
            existingActionProperty(coords, inputTypings, value, key)
        }
    val customActionProperties = with
        .filterKeys { key -> key.toCamelCase() !in kclassProperties }
        .joinToCode(
            ifEmpty = CodeBlock.EMPTY,
            prefix = CodeBlock.of("_customInputs = mapOf(\n"),
            newLineAtEnd = false,
            postfix = "),\n",
        ) { key, value ->
            CodeBlock.of("%S to %S", key, value)
        }
    return CodeBlock { builder ->
        builder.add(existingActionProperties)
        builder.indent()
        builder.add(customActionProperties)
        if (_customVersion != null) {
            builder.add("_customVersion = %S,\n", _customVersion)
        }
        builder.unindent()
        builder.add("),\n")
    }
}

private fun existingActionProperty(
    coords: ActionCoords,
    inputTypings: Map<String, Typing>?,
    value: String?,
    key: String,
) = CodeBlock { builder ->
    value ?: return@CodeBlock
    val typing = inputTypings?.get(key) ?: StringTyping
    builder
        .add("%N = ", key.toCamelCase())
        .add(valueWithTyping(value, typing, coords))
        .add(",\n")
}

fun YamlStep.generateCustomAction(
    coords: ActionCoords,
): CodeBlock {
    val coordsBlock = coords.toMap().joinToCode(
        prefix = CodeBlock.of("action = %T(", CustomAction::class),
        postfix = "",
        newLineAtEnd = false,
    ) { key, value ->
        CodeBlock.of("%L = %S", key.toCamelCase(), value)
    }

    val inputsBlock = with.joinToCode(
        prefix = CodeBlock.of("inputs = %M(\n", Members.mapOf),
        ifEmpty = CodeBlock.of("inputs = emptyMap()"),
    ) { key, value ->
        CodeBlock.of("%S to %S", key, value)
    }

    return CodeBlock { builder ->
        builder.add(coordsBlock)
        builder.indent()
        builder.add(inputsBlock)
        builder.unindent()
        builder.add("),\n")
    }
}

fun ActionCoords(yaml: String): ActionCoords {
    val (owner, name, version) = yaml.split("/", "@")
    return ActionCoords(owner, name, version)
}

fun ActionCoords.classname() =
    ClassName("$PACKAGE.actions.${owner.toKotlinPackageName()}", buildActionClassName())

fun ActionCoords.toMap(): Map<String, String> =
    mapOf(
        "actionOwner" to owner,
        "actionName" to name,
        "actionVersion" to version,
    )
