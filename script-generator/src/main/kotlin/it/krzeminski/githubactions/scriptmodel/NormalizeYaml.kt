package it.krzeminski.githubactions.scriptmodel

import it.krzeminski.githubactions.scriptgenerator.allTriggersNames

@OptIn(ExperimentalStdlibApi::class)
fun String.normalizeYaml(): String {
    val lines = lines()
    val triggerNames = allTriggersNames

    val transformed: List<String> = lines.mapIndexed line@{ i, line ->
        val (space, name) = topLevelProperties.find(line)?.destructured?.toList()
            ?: listOf("", "")

        val onLine = convertOnToObject(line, triggerNames.toSet())
        val nextLine = lines.nextNonBlank(i)
        when {
            onLine != null -> onLine
            name !in triggerNames -> line
            nextLine.startsWith("$space  ") -> line
            nextLine.trimStart().startsWith("-") -> line
            else -> "$space$name: {}"
        }
    }
    return transformed.joinToString("\n")
}

fun convertOnToObject(line: String, triggerNames: Set<String>): String? {
    val afterOn = line.substringAfter("on:").trim()
    when {
        line.startsWith("on:").not() -> return null
        afterOn.contains("[") -> {}
        afterOn.isNotBlank() && afterOn in triggerNames ->
            return "on:\n  $afterOn: {}"
        afterOn.isBlank() ->
            return null
    }

    val list = line
        .substringAfter("[")
        .substringBefore("]")
        .replace("\"", "")
        .replace("'", "")
        .replace(" ", "")
        .split(",")
    return "on:\n" + list.joinToString("\n") { "  $it: {}" }
}

private val topLevelProperties =
    "^(\\s*)(\\w+):\\s*$".toRegex()

private fun List<String>.nextNonBlank(index: Int): String =
    IntRange(start = Math.min(index + 1, lastIndex), endInclusive = lastIndex)
        .firstOrNull { i ->
            get(i).replaceBefore("#", "").isNotBlank()
        }?.let { get(it) }
        ?: ""
