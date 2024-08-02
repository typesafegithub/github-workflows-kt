package io.github.typesafegithub.workflows.actionbindinggenerator.bindingsfromunittests

import io.github.typesafegithub.workflows.actions.johnsmith.ActionWithAllTypesOfInputs
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class ActionWithAllTypesOfInputsTest : DescribeSpec({
    it("correctly translates all types of inputs") {
        // given
        val action = ActionWithAllTypesOfInputs(
            fooBar = "test",
            bazGoo = true,
            binKin = false,
            intPint = 43,
            floPint = 123.456f,
            finBin = ActionWithAllTypesOfInputs.Bin.BooBar,
            gooZen = ActionWithAllTypesOfInputs.Zen.Special1,
            bahEnum = ActionWithAllTypesOfInputs.BahEnum.HelloWorld,
            listStrings = listOf("hello", "world"),
            listInts = listOf(1, 42),
            listEnums = listOf(ActionWithAllTypesOfInputs.MyEnum.One, ActionWithAllTypesOfInputs.MyEnum.Three),
            listIntSpecial = listOf(ActionWithAllTypesOfInputs.MyInt.TheAnswer, ActionWithAllTypesOfInputs.MyInt.Value(0))
        )

        // when
        val yaml = action.toYamlArguments()

        // then
        yaml shouldBe linkedMapOf(
            "foo-bar" to "test",
            "baz-goo" to "true",
            "bin-kin" to "false",
            "int-pint" to "43",
            "flo-pint" to "123.456",
            "fin-bin" to "boo-bar",
            "goo-zen" to "3",
            "bah-enum" to "helloworld",
            "list-strings" to "hello,world",
            "list-ints" to "1,42",
            "list-enums" to "one,three",
            "list-int-special" to "42,0"
        )
    }

    it("works for custom values") {
        // given
        val action = ActionWithAllTypesOfInputs(
            fooBar = "test",
            bazGoo = true,
            binKin = false,
            intPint = 43,
            floPint = 123.456f,
            finBin = ActionWithAllTypesOfInputs.Bin.Custom("this-is-custom!"),
            gooZen = ActionWithAllTypesOfInputs.Zen.Value(123),
            bahEnum = ActionWithAllTypesOfInputs.BahEnum.Custom("very-custom"),
        )

        // when
        val yaml = action.toYamlArguments()

        // then
        yaml shouldBe linkedMapOf(
            "foo-bar" to "test",
            "baz-goo" to "true",
            "bin-kin" to "false",
            "int-pint" to "43",
            "flo-pint" to "123.456",
            "fin-bin" to "this-is-custom!",
            "goo-zen" to "123",
            "bah-enum" to "very-custom",
        )
    }
})
