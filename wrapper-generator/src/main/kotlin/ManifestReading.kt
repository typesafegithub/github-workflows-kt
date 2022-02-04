import com.charleskorn.kaml.Yaml
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import java.net.URI

@Serializable
data class Manifest(
    val name: String,
    val description: String,
    val inputs: Map<String, Input>,
)

@Serializable
data class Input(
    val description: String,
    val default: String? = null,
)

fun ActionCoords.fetchManifest(fetchUri: (URI) -> String = ::fetchUri): Manifest {
    val manifestUri = URI("https://raw.githubusercontent.com/$owner/$name/$version/action.yml") // TODO what if .yAml?
    val manifestYaml = fetchUri(manifestUri)
    return myYaml.decodeFromString(manifestYaml)
}

private fun fetchUri(uri: URI) = uri.toURL().readText()

val myYaml = Yaml(configuration = Yaml.default.configuration.copy(
    strictMode = false,
))
