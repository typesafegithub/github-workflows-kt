package io.github.typesafegithub.workflows.domain.triggers

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

public data class Schedule(
    val triggers: List<Cron>,
    override val _customArguments: Map<String, @Contextual Any> = mapOf(),
) : Trigger()

/**
 * Builds a cron expression and performs no validation.
 *
 * @see https://crontab.guru
 **/
@Serializable
public data class Cron(val expression: String)

/**
 * Builds a cron expression given individual components. Performs basic validation.
 *
 * @see https://crontab.guru
 **/
public fun Cron(
    minute: String = "*",
    hour: String = "*",
    dayMonth: String = "*",
    month: String = "*",
    dayWeek: String = "*",
): Cron {
    @Suppress("MagicNumber")
    val errors =
        listOf(
            checkCronSyntax(minute, "minute", 0..59),
            checkCronSyntax(hour, "hour", 0..23),
            checkCronSyntax(dayMonth, "dayMonth", 0..59),
            checkCronSyntax(
                month,
                "month",
                1..31,
                listOf("JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"),
            ),
            checkCronSyntax(
                dayWeek,
                "dayWeek",
                0..6,
                listOf("MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"),
            ),
        ).flatten()
    val cron = "$minute $hour $dayMonth $month $dayWeek"
    check(errors.isEmpty()) {
        val formattedErrors = errors.joinToString(separator = "\n- ", prefix = "\n- ")
        "Your Cron syntax [$cron] contains errors:$formattedErrors"
    }
    return Cron(cron)
}

private fun checkCronSyntax(
    value: String,
    field: String,
    range: IntRange,
    namedValues: List<String>? = null,
): List<String> {
    val errors = mutableListOf<String>()

    fun addErrorIf(
        message: String,
        predicate: () -> Boolean,
    ) {
        if (predicate()) errors += "Field '$field' with value [$value] $message"
    }

    addErrorIf("contains invalid characters") {
        val allowedCharacters = ('0'..'9') + listOf('-', '*', ',', '/') + ('A'..'Z')
        value.any { it !in allowedCharacters }
    }
    addErrorIf("contains value(s) not included in $namedValues") {
        namedValues?.let {
            val replace = value.replace(Regex("[-*,/]"), " ")
            val values =
                replace
                    .split(" ")
                    .filter { it.isNotBlank() }
                    .filter { it.toIntOrNull() == null }
            values.any { it !in namedValues }
        } ?: false
    }
    addErrorIf("contains number(s) outside of range $range") {
        val replace = value.replace(Regex("[-*,/]"), " ")
        val numbers =
            replace
                .split(" ")
                .mapNotNull { it.toIntOrNull() }
        numbers.any { it !in range }
    }
    addErrorIf("is blank") {
        value.isBlank()
    }

    return errors
}
