package it.krzeminski.githubactions.yaml

inline fun <reified T : Enum<T>> T.toSnakeCase(): String {
    require(name.first() !in 'a'..'z' && name.all { it in 'a'..'z' || it in 'A'..'Z' })
    return pascalCaseRegex.findAll(name).joinToString(separator = "_") { it.value.lowercase() }
}

val pascalCaseRegex = Regex("[A-Z][a-z]*")
