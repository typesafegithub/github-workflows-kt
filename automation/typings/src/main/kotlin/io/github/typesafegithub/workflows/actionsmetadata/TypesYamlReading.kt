package io.github.typesafegithub.workflows.actionsmetadata

import io.github.typesafegithub.workflows.actionsmetadata.model.ActionType
import io.github.typesafegithub.workflows.actionsmetadata.model.ActionTypeEnum
import io.github.typesafegithub.workflows.actionsmetadata.model.ActionTypes
import io.github.typesafegithub.workflows.actionsmetadata.model.BooleanTyping
import io.github.typesafegithub.workflows.actionsmetadata.model.EnumTyping
import io.github.typesafegithub.workflows.actionsmetadata.model.FloatTyping
import io.github.typesafegithub.workflows.actionsmetadata.model.IntegerTyping
import io.github.typesafegithub.workflows.actionsmetadata.model.IntegerWithSpecialValueTyping
import io.github.typesafegithub.workflows.actionsmetadata.model.ListOfTypings
import io.github.typesafegithub.workflows.actionsmetadata.model.StringTyping
import io.github.typesafegithub.workflows.actionsmetadata.model.Typing

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
        ActionTypeEnum.List ->
            ListOfTypings(
                delimiter = separator,
                typing = listItem?.toTyping(fieldName) ?: error("Lists should have list-item set!"),
            )
        ActionTypeEnum.Enum -> EnumTyping(typeName = name, items = allowedValues)
    }
