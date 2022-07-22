package it.krzeminski.githubactions.yaml

import it.krzeminski.githubactions.dsl.HasCustomArguments
import it.krzeminski.githubactions.dsl.ListCustomValue
import it.krzeminski.githubactions.dsl.ObjectCustomValue
import it.krzeminski.githubactions.dsl.StringCustomValue

internal fun HasCustomArguments.customArgumentsToYaml(): String = buildString {
    for ((key, customValue) in _customArguments) {
        when (customValue) {
            is ListCustomValue -> printIfHasElements(customValue.value, key)
            is StringCustomValue -> appendLine("  $key: ${customValue.value}")
            is ObjectCustomValue -> {
                appendLine("  $key:")
                for ((subkey, subvalue) in customValue.value) {
                    appendLine("    $subkey: $subvalue")
                }
            }
        }
    }
}.removeSuffix("\n")
