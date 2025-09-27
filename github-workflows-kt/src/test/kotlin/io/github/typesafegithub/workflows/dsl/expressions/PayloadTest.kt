package io.github.typesafegithub.workflows.dsl.expressions

import io.github.typesafegithub.workflows.dsl.expressions.contexts.RunnerContext
import io.kotest.assertions.AssertionErrorBuilder.Companion.fail
import io.kotest.core.spec.style.FunSpec
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import java.io.File
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties

class PayloadTest :
    FunSpec({
        val payloads = File("src/test/resources/payloads")

        fun KClass<*>.properties(): List<String> = declaredMemberProperties.map { it.name }.sorted()

        test("Runner context") {
            val context = RunnerContext::class
            val file = payloads.resolve("runner.json")
            val jsonObject = Json.parseToJsonElement(file.readText()).jsonObject

            context.properties() shouldMatch jsonObject.keys.sorted()
        }
    })

infix fun List<String>.shouldMatch(other: List<String>) {
    if (this.toSet() != other.toSet()) {
        fail(
            """
                |The lists don't match
                |Missing:    ${(other - toSet()).distinct().sorted()}
                |Unexpected: ${(this - other.toSet()).distinct().sorted()}
            """.trimMargin(),
        )
    }
}
