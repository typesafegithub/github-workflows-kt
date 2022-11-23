package it.krzeminski.githubactions.wrappergenerator.domain.typings

import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage

class EnumTypingsTest : FunSpec({

    test("Happy path") {
        val typing = EnumTyping("MyEnum", listOf("enum_one", "enum_two", "enum-three"))
        typing.itemsNames shouldBe listOf("EnumOne", "EnumTwo", "EnumThree")
    }

    test("Invalid number of itemsNames") {
        shouldThrowAny {
            EnumTyping(
                "MyEnum",
                listOf("enum_one", "enum_two", "enum_three"),
                listOf("One", "Two"),
            )
        }.shouldHaveMessage("EnumTyping(MyEnum): items has 3 elements while itemsNames has 2 elements")

        shouldThrowAny {
            EnumTyping(
                "MyEnum",
                listOf("enum_one", "enum_two", "enum_three"),
                listOf("One", "Two", "Three", "Four"),
            )
        }.shouldHaveMessage("EnumTyping(MyEnum): items has 3 elements while itemsNames has 4 elements")
    }

    test("itemNames should be in PascalCase") {
        shouldThrowAny {
            EnumTyping(
                "MyEnum",
                listOf("enum_one", "enum_two", "enum_three", "enum_four"),
                listOf("One", "two", "Three", "Enum-Four"),
            )
        }.shouldHaveMessage("EnumTyping(MyEnum): itemsNames should be in PascalCase, but got: [two, Enum-Four]")
    }
},)
