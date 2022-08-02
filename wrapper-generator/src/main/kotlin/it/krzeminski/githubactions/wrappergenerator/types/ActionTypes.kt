package it.krzeminski.githubactions.wrappergenerator.types

import com.charleskorn.kaml.SingleLineStringStyle
import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlComment
import it.krzeminski.githubactions.wrappergenerator.domain.WrapperRequest
import it.krzeminski.githubactions.wrappergenerator.domain.typings.BooleanTyping
import it.krzeminski.githubactions.wrappergenerator.domain.typings.EnumTyping
import it.krzeminski.githubactions.wrappergenerator.domain.typings.IntegerTyping
import it.krzeminski.githubactions.wrappergenerator.domain.typings.IntegerWithSpecialValueTyping
import it.krzeminski.githubactions.wrappergenerator.domain.typings.ListOfTypings
import it.krzeminski.githubactions.wrappergenerator.domain.typings.StringTyping
import it.krzeminski.githubactions.wrappergenerator.domain.typings.Typing
import it.krzeminski.githubactions.wrappergenerator.metadata.Metadata
import it.krzeminski.githubactions.wrappergenerator.metadata.myYaml
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import java.io.File

@Serializable
data class ActionTypes(
    @YamlComment("See https://github.com/krzema12/github-actions-typing")
    val inputs: Map<String, ActionType> = emptyMap(),
    @YamlComment("Please check those outputs's description and set a proper type. 'string' is just set by default")
    val outputs: Map<String, ActionType> = emptyMap(),
)

@Serializable
data class ActionType(
    val type: ActionTypeEnum,
    @SerialName("named-values")
    val namedValues: Map<String, Int> = emptyMap(),
    val separator: String = "",
    @SerialName("allowed-values")
    val allowedValues: List<String> = emptyList(),
    @SerialName("list-item")
    val listItem: ActionType? = null,
) {
    init { validateType() }
}

@Serializable
enum class ActionTypeEnum {
    @SerialName("string") String,
    @SerialName("boolean") Boolean,
    @SerialName("integer") Integer,
    @SerialName("float") Float,
    @SerialName("list") List,
    @SerialName("enum") Enum,
}

fun Typing.type(): ActionType = when (this) {
    BooleanTyping -> ActionType(ActionTypeEnum.Boolean)
    IntegerTyping -> ActionType(ActionTypeEnum.Integer)
    StringTyping -> ActionType(ActionTypeEnum.String)
    is EnumTyping -> ActionType(
        type = ActionTypeEnum.Enum,
        allowedValues = items,
    )
    is IntegerWithSpecialValueTyping -> ActionType(
        type = ActionTypeEnum.Integer,
        namedValues = specialValues,
    )
    is ListOfTypings -> ActionType(
        type = ActionTypeEnum.List,
        separator = if (delimiter == "\\n") "\n" else delimiter,
        listItem = typing.type(),
    )
}

fun ActionType.validateType() {
    if (type == ActionTypeEnum.List) {
        check(separator.isNotEmpty() && listItem != null) {
            "Invalid type $this: needs a separator and a listItem"
        }
    } else if (type == ActionTypeEnum.Enum) {
        check(allowedValues.isNotEmpty()) {
            "Invalid type $this: needs allowedValues"
        }
    }
}

fun WrapperRequest.toActionTypes(metadataFile: File): ActionTypes {
    check(metadataFile.canRead()) { "Can't read ${metadataFile.canonicalFile}" }
    val metadata: Metadata = myYaml.decodeFromString(metadataFile.readText())
    val inputTypings = this.provideTypes()
    val inputs = metadata.inputs.mapValues { (key, _) ->
        inputTypings[key]?.type() ?: ActionType(ActionTypeEnum.String)
    }
    val outputs = metadata.outputs.map { (key, _) ->
        key to ActionType(ActionTypeEnum.String)
    }.toMap()

    return ActionTypes(inputs, outputs)
}

fun WrapperRequest.toYaml(metadataFile: File): String {
    val myYaml = Yaml(
        configuration = Yaml.default.configuration.copy(
            strictMode = false,
            encodeDefaults = false,
            singleLineStringStyle = SingleLineStringStyle.Plain,

        )
    )
    return myYaml.encodeToString(toActionTypes(metadataFile)) + "\n"
}
