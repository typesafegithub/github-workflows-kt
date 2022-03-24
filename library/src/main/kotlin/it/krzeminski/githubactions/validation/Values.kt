package it.krzeminski.githubactions.validation

internal fun requireMatchesRegex(field: String, value: String, regex: Regex, url: String?) {
    require(regex.matchEntire(value) != null) {
        val seeUrl = if (url.isNullOrBlank()) "" else "\nSee: $url"
        """Invalid field ${field.replace('.', '(')}="$value") does not match regex: $regex$seeUrl"""
    }
}
