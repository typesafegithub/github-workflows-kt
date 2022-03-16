package it.krzeminski.githubactions.domain.triggers

import it.krzeminski.githubactions.dsl.CustomValue
import kotlinx.serialization.Serializable

data class Schedule(
    val triggers: List<Cron>,
    override val _customArguments: Map<String, CustomValue> = mapOf(),
) : Trigger()

/** See https://crontab.guru **/
@Serializable
data class Cron(val expression: String)

/** See https://crontab.guru **/
fun Cron(
    minute: String = "*",
    hour: String = "*",
    dayMonth: String = "*",
    month: String = "*",
    dayWeek: String = "*",
): Cron {
    @Suppress("MagicNumber")
    val errors = listOf(
        checkCronSyntax(minute, "minute", 0..59),
        checkCronSyntax(hour, "hour", 0..23),
        checkCronSyntax(dayMonth, "dayMonth", 0..59),
        checkCronSyntax(month, "month", 1..31),
        checkCronSyntax(dayWeek, "dayWeek", 0..6),
    ).flatten()
    val cron = "$minute $hour $dayMonth $month $dayWeek"
    check(errors.isEmpty()) {
        val formattedErrors = errors.joinToString(separator = "\n- ", prefix = "\n- ")
        "Your Cron syntax [$cron] contains errors:$formattedErrors"
    }
    return Cron(cron)
}

private fun checkCronSyntax(value: String, field: String, range: IntRange): List<String> {
    val errors = mutableListOf<String>()
    fun addErrorIf(message: String, predicate: () -> Boolean) {
        if (predicate()) errors += "Field '$field' with value [$value] $message"
    }

    addErrorIf("contains invalid characters") {
        val allowedCharacters = ('0'..'9') + listOf('-', '*', ',', '/')
        value.any { it !in allowedCharacters }
    }
    addErrorIf("contains number(s) outside of range $range") {
        val replace = value.replace(Regex("[-*,/]"), " ")
        val numbers = replace
            .split(" ")
            .mapNotNull { it.toIntOrNull() }
        numbers.any { it !in range }
    }
    addErrorIf("is blank") {
        value.isBlank()
    }

    return errors
}
