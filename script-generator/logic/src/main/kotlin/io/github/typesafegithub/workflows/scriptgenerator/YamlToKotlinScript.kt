package io.github.typesafegithub.workflows.scriptgenerator

import io.github.typesafegithub.workflows.scriptmodel.YamlWorkflow
import io.github.typesafegithub.workflows.scriptmodel.myYaml
import io.github.typesafegithub.workflows.scriptmodel.normalizeYaml
import kotlinx.serialization.decodeFromString
import java.net.URL

fun yamlToKotlinScript(yaml: String, filename: String? = null): String {
    val workflow: YamlWorkflow = decodeYamlWorkflow(yaml)
    return workflow.toKotlin(filename ?: workflow.name)
}

fun URL.filename(): String =
    path.substringAfterLast("/").substringBefore(".")

private fun decodeYamlWorkflow(text: String): YamlWorkflow {
    return myYaml.decodeFromString(text.normalizeYaml())
}
