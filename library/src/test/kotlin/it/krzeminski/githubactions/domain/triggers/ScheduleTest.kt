package it.krzeminski.githubactions.domain.triggers

import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.core.spec.style.FunSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage

@Suppress("unused")
class ScheduleTest : FunSpec({
    context("Cron") {
        val validCronExpressions = listOf(
            Cron() to "* * * * *",
            Cron(minute = "1", hour = "2", dayMonth = "3", month = "4", dayWeek = "5")
                to "1 2 3 4 5",
            Cron(minute = "1,4,5", hour = "4-6", dayMonth = "10-20/2", month = "6,8", dayWeek = "1/3")
                to "1,4,5 4-6 10-20/2 6,8 1/3",
        )

        test("valid expressions") {
            validCronExpressions.forAll { (cron, expected) ->
                cron.expression shouldBe expected
            }
        }

        test("with invalid range") {
            shouldThrowAny {
                Cron(minute = "60", hour = "24", dayMonth = "60", month = "0", dayWeek = "7")
            }.shouldHaveMessage(
                """
                Your Cron syntax [60 24 60 0 7] contains errors:
                - Field 'minute' with value [60] contains number(s) outside of range 0..59
                - Field 'hour' with value [24] contains number(s) outside of range 0..23
                - Field 'dayMonth' with value [60] contains number(s) outside of range 0..59
                - Field 'month' with value [0] contains number(s) outside of range 1..31
                - Field 'dayWeek' with value [7] contains number(s) outside of range 0..6
                """.trimIndent(),
            )
        }

        test("with invalid blank fields") {
            shouldThrowAny {
                Cron(minute = "", hour = "", dayMonth = "", month = "", dayWeek = "")
            }.shouldHaveMessage(
                """
                Your Cron syntax [    ] contains errors:
                - Field 'minute' with value [] is blank
                - Field 'hour' with value [] is blank
                - Field 'dayMonth' with value [] is blank
                - Field 'month' with value [] is blank
                - Field 'dayWeek' with value [] is blank
                """.trimIndent(),
            )
        }

        test("with invalid characters") {
            shouldThrowAny {
                Cron(minute = "=", hour = ";")
            }.shouldHaveMessage(
                """
                Your Cron syntax [= ; * * *] contains errors:
                - Field 'minute' with value [=] contains invalid characters
                - Field 'hour' with value [;] contains invalid characters
                """.trimIndent(),
            )
        }
    }
},)
