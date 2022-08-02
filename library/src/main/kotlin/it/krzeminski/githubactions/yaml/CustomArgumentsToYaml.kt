package it.krzeminski.githubactions.yaml

import it.krzeminski.githubactions.dsl.HasCustomArguments
import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.Yaml
import java.io.StringWriter

internal fun HasCustomArguments.customArgumentsToYaml(): String {
    val yaml = Yaml(
        DumperOptions().apply {
            defaultFlowStyle = DumperOptions.FlowStyle.BLOCK
        }
    )
    val stringWriter = StringWriter()
    yaml.dump(this._customArguments, stringWriter)
    val asString = stringWriter.toString().removeSuffix("\n")

    return if (asString == "{}") "" else asString
}
