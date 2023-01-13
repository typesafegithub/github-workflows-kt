package it.krzeminski.githubactions.actionsmetadata

import it.krzeminski.githubactions.actionsmetadata.model.ActionType
import it.krzeminski.githubactions.actionsmetadata.model.ActionTypeEnum
import it.krzeminski.githubactions.actionsmetadata.model.ActionTypes
import it.krzeminski.githubactions.actionsmetadata.model.BooleanTyping
import it.krzeminski.githubactions.actionsmetadata.model.EnumTyping
import it.krzeminski.githubactions.actionsmetadata.model.FloatTyping
import it.krzeminski.githubactions.actionsmetadata.model.IntegerTyping
import it.krzeminski.githubactions.actionsmetadata.model.IntegerWithSpecialValueTyping
import it.krzeminski.githubactions.actionsmetadata.model.ListOfTypings
import it.krzeminski.githubactions.actionsmetadata.model.StringTyping
import it.krzeminski.githubactions.actionsmetadata.model.Typing

internal fun ActionTypes.toTypesMap(): Map<String, Typing> {
    return inputs.mapValues { (key, value) ->
        value.toTyping(key)
    }
}

private fun ActionType.toTyping(fieldName: String): Typing =
    when (this.type) {
        ActionTypeEnum.String -> StringTyping
        ActionTypeEnum.Boolean -> BooleanTyping
        ActionTypeEnum.Integer -> {
            if (this.namedValues.isEmpty()) {
                IntegerTyping
            } else {
                IntegerWithSpecialValueTyping(typeName = name, specialValues = namedValues)
            }
        }
        ActionTypeEnum.Float -> FloatTyping
        ActionTypeEnum.List -> ListOfTypings(
            delimiter = separator,
            typing = listItem?.toTyping(fieldName) ?: error("Lists should have list-item set!"),
        )
        ActionTypeEnum.Enum -> EnumTyping(typeName = name, items = allowedValues)
    }
