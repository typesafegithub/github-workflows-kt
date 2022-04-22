package it.krzeminski.githubactions.yaml

import com.charleskorn.kaml.MalformedYamlException
import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString

internal fun failIfMalformedYml(yamlContent: String) {
    try {
        yaml.decodeFromString<YamlWorkflow>(yamlContent)
    } catch (e: MalformedYamlException) {
        println(yamlContent)
        throw e
    }
}

private val yaml = Yaml(
    configuration = YamlConfiguration(strictMode = false)
)

@Serializable
private data class YamlWorkflow(
    val name: String
)
