package io.github.typesafegithub.workflows.actionbindinggenerator

public sealed interface Typing

internal object BooleanTyping : Typing

internal data class EnumTyping(
    /**
     * Custom type name when used in Kotlin code.
     * Null means that the type name should be inferred from the input name.
     */
    val typeName: String? = null,
    /**
     * Items as required by the action, e.g. foo-bar-1
     */
    val items: List<String>,
    /**
     * Items in a more readable form when used in Kotlin code and following its conventions, e.g. FooBar1.
     * Null means that these names should be inferred from the items.
     */
    val itemsNames: List<String>? = null,
) : Typing

internal object FloatTyping : Typing

internal object IntegerTyping : Typing

internal data class IntegerWithSpecialValueTyping(
    /**
     * Custom type name when used in Kotlin code.
     * Null means that the type name should be inferred from the input name.
     */
    val typeName: String? = null,
    val specialValues: Map<String, Int>,
) : Typing

internal data class ListOfTypings(
    val delimiter: String,
    val typing: Typing = StringTyping,
) : Typing

internal object StringTyping : Typing
