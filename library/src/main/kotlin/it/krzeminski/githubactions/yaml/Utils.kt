package it.krzeminski.githubactions.yaml

fun LinkedHashMap<String, String>.toYaml() =
    map { (key, value) ->
        if (value.lines().size == 1) {
            "$key: $value"
        } else {
            buildString {
                appendLine("$key: |")
                value.lines().forEach {
                    appendLine(it.prependIndent("  "))
                }
            }.removeSuffix("\n")
        }
    }.joinToString(separator = "\n")
