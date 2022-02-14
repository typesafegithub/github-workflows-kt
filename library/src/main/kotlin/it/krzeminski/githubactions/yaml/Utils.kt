package it.krzeminski.githubactions.yaml

fun LinkedHashMap<String, String>.toYaml() =
    map { (key, value) ->
        if (value.lines().size == 1) {
            val valueMaybeQuoted = if (value.first() in specialYamlCharactersWhenStartingValues) {
                "'$value'"
            } else {
                value
            }
            "$key: $valueMaybeQuoted"
        } else {
            buildString {
                appendLine("$key: |")
                value.lines().forEach {
                    appendLine(it.prependIndent("  "))
                }
            }.removeSuffix("\n")
        }
    }.joinToString(separator = "\n")

private val specialYamlCharactersWhenStartingValues = setOf('*', '[', '!')
