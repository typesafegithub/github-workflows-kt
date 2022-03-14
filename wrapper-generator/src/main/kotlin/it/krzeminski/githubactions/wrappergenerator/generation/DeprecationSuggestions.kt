package it.krzeminski.githubactions.wrappergenerator.generation

import it.krzeminski.githubactions.wrappergenerator.domain.ActionCoords
import it.krzeminski.githubactions.wrappergenerator.domain.WrapperRequest
import it.krzeminski.githubactions.wrappergenerator.metadata.prettyPrint

fun List<WrapperRequest>.suggestDeprecations(): String {
    val nonDeprecatedCoords = this.map { it.actionCoords }

    val groupedBy: List<List<ActionCoords>> = nonDeprecatedCoords
        .groupBy { coords -> "${coords.owner}/${coords.name}" }
        .values.toList()

    val messages = groupedBy.flatMap { list ->
        val maxVersion = list.maxByOrNull { it.version }?.version ?: error("Unexpected empty list in $groupedBy")
        list.mapNotNull {
            when {
                it.version == maxVersion && it.deprecatedByVersion == null -> null
                it.version == maxVersion ->
                    """WARNING: ${it.prettyPrint} is deprecatedByVersion "${it.deprecatedByVersion}", an older version ?"""
                it.deprecatedByVersion == maxVersion -> null
                else ->
                    """WARNING: newer version available for ${it.prettyPrint}. Maybe add: deprecatedByVersion = "$maxVersion" ?"""
            }
        }
    }
    return messages.joinToString("\n")
}
