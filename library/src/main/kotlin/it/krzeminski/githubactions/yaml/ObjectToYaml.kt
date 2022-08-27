package it.krzeminski.githubactions.yaml

import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.Yaml

internal fun Any.toYaml(): String = yaml.dump(this)

private val yaml = Yaml(
    DumperOptions().apply {
        defaultFlowStyle = DumperOptions.FlowStyle.BLOCK
    }
)
