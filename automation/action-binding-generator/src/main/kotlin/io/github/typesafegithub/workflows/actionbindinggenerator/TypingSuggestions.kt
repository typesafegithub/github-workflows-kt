package io.github.typesafegithub.workflows.actionbindinggenerator

internal fun Metadata.suggestAdditionalTypings(existingTypings: Set<String>): String? {
    val keys = (inputs.keys - existingTypings).associate {
        it to inputs.get(it)!!
    }

    val suggestions = keys.mapNotNull { (key, input) ->
        val typing = input.suggestTyping() ?: return@mapNotNull null
        key to typing
    }
    val formatSuggestions = suggestions
        .joinToString(separator = "\n", prefix = "    mapOf(\n", postfix = "\n    )") {
            """            "${it.first}" to ${it.second},"""
        }

    return if (suggestions.isNotEmpty()) {
        formatSuggestions
    } else {
        null
    }
}

internal fun Input.suggestTyping(): String? {
    val listKeywords = setOf(
        "list of",
        "paths",
        "comma separated",
        "newline separated",
    )
    val normalizedDescription = this.description.lowercase()
        .map { if (it in 'a'..'z') it else ' ' }.joinToString("")

    return when {
        default in listOf("true", "false") -> "BooleanTyping"
        default?.toIntOrNull() != null -> "IntegerTyping"
        listKeywords.any { normalizedDescription.contains(it) } -> """ListOfTypings(TODO("please check"))"""
        else -> null
    }
}
