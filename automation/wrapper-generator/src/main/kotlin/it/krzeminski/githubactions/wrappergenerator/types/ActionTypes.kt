package it.krzeminski.githubactions.wrappergenerator.types

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActionTypes(
    val inputs: Map<String, ActionType> = emptyMap(),
    val outputs: Map<String, ActionType> = emptyMap(),
)

@Serializable
data class ActionType(
    val type: ActionTypeEnum,
    val name: String? = null,
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
    @SerialName("string")
    String,

    @SerialName("boolean")
    Boolean,

    @SerialName("integer")
    Integer,

    @SerialName("float")
    Float,

    @SerialName("list")
    List,

    @SerialName("enum")
    Enum,
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
