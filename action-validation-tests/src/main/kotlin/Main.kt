import com.charleskorn.kaml.Yaml
import it.krzeminski.githubactions.actions.actions.CheckoutV2
import it.krzeminski.githubactions.actions.actions.CheckoutV2.FetchDepth
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import java.net.URL

@Serializable
data class ActionManifest(
    val inputs: Map<String, ActionInput>,
)

@Serializable
data class ActionInput(
    val description: String,
)

fun main() {
    val actionUnderTest = CheckoutV2(
        fetchDepth = FetchDepth.Value(3),
    )

    val actionManifestYamlUrl = URL(
        "https://raw.githubusercontent.com/${actionUnderTest.actionOwner}/${actionUnderTest.actionName}/main/action.yml"
    )
    val actionManifestYaml = actionManifestYamlUrl.readText()
    val yaml = Yaml(configuration = Yaml.default.configuration.copy(strictMode = false))
    val actionManifest = yaml.decodeFromString<ActionManifest>(actionManifestYaml)

    println("Inputs defined in the action manifest:")
    val inputsInManifest = actionManifest.inputs.map { it.key }.toSet()
    println(inputsInManifest)

    println("Inputs implemented in the wrapper:")
    val inputsInWrapper = actionUnderTest.toYamlArguments().map { it.key }.toSet()
    println(inputsInWrapper)

    if (inputsInManifest != inputsInWrapper) {
        println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
        println("There's discrepancy in inputs!")

        println("Present in manifest, absent in wrapper:")
        println(inputsInManifest - inputsInWrapper)
        println("Present in wrapper, absent in manifest:")
        println(inputsInWrapper - inputsInManifest)
    }
}
