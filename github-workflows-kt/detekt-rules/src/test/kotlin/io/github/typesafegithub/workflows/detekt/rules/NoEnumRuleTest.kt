package io.github.typesafegithub.workflows.detekt.rules

import io.gitlab.arturbosch.detekt.test.TestConfig
import io.gitlab.arturbosch.detekt.test.compileAndLint
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class NoEnumRuleTest :
    FunSpec({
        test("reports enum class") {
            val code = """
                enum class Color { RED, GREEN, BLUE }
            """
            val findings = NoEnumRule(TestConfig()).compileAndLint(code)
            findings.size shouldBe 1
        }

        test("does not report sealed class") {
            val code = """
                sealed class Color {
                    data object RED : Color()
                    data object GREEN : Color()
                    data object BLUE : Color()
                }
            """
            val findings = NoEnumRule(TestConfig()).compileAndLint(code)
            findings.size shouldBe 0
        }

        test("does not report regular class") {
            val code = """
                class Foo
            """
            val findings = NoEnumRule(TestConfig()).compileAndLint(code)
            findings.size shouldBe 0
        }
    })
