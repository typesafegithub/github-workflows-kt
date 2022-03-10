package it.krzeminski.githubactions.scriptmodel

import it.krzeminski.githubactions.yaml.triggerClassMap

@OptIn(ExperimentalStdlibApi::class)
fun String.normalizeYaml(): String {
    val lines = lines()
    val triggerNames = triggerClassMap.map { it.first }.toSet()

    val transformed: List<String> = lines.mapIndexed line@{ i, line ->
        val (space, name) = topLevelProperties.find(line)?.destructured?.toList()
            ?: listOf("", "")

        val onLine = convertOnToObject(line)
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

fun convertOnToObject(line: String): String? {
    if (line.startsWith("on:").not() || line.contains("[").not()) {
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
