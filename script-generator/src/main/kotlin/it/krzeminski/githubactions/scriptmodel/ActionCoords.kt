package it.krzeminski.githubactions.scriptmodel

import com.squareup.kotlinpoet.ClassName
import it.krzeminski.githubactions.wrappergenerator.domain.ActionCoords
import it.krzeminski.githubactions.wrappergenerator.generation.buildActionClassName
import it.krzeminski.githubactions.wrappergenerator.generation.toKotlinPackageName

fun ActionCoords(yaml: String): ActionCoords {
    val (owner, name, version) = yaml.split("/", "@")
    return ActionCoords(owner, name, version)
}

fun ActionCoords.classname() =
    ClassName("it.krzeminski.githubactions.actions.${owner.toKotlinPackageName()}", buildActionClassName())

