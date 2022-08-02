package it.krzeminski.githubactions.yaml

fun LinkedHashMap<String, String>.toYaml() =
    map { (rawKey, value) ->
        // Needed for type-safe environment variables. See test("Executing workflow with type-safe expressions")
        val key = rawKey.removePrefix("\$")
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
