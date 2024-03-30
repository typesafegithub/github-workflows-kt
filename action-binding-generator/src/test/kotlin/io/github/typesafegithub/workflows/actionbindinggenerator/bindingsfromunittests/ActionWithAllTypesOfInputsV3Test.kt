package io.github.typesafegithub.workflows.actionbindinggenerator.bindingsfromunittests

import io.github.typesafegithub.workflows.actions.johnsmith.ActionWithAllTypesOfInputsV3
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class ActionWithAllTypesOfInputsV3Test : DescribeSpec({
    it("correctly translates all types of inputs") {
        // given
        val action = ActionWithAllTypesOfInputsV3(
            fooBar = "test",
            bazGoo = true,
            binKin = false,
            intPint = 43,
            floPint = 123.456f,
            finBin = ActionWithAllTypesOfInputsV3.Bin.BooBar,
            gooZen = ActionWithAllTypesOfInputsV3.Zen.Special1,
            bahEnum = ActionWithAllTypesOfInputsV3.BahEnum.HelloWorld,
            listStrings = listOf("hello", "world"),
            listInts = listOf(1, 42),
            listEnums = listOf(ActionWithAllTypesOfInputsV3.MyEnum.One, ActionWithAllTypesOfInputsV3.MyEnum.Three),
            listIntSpecial = listOf(ActionWithAllTypesOfInputsV3.MyInt.TheAnswer, ActionWithAllTypesOfInputsV3.MyInt.Value(0))
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
        val action = ActionWithAllTypesOfInputsV3(
            fooBar = "test",
            bazGoo = true,
            binKin = false,
            intPint = 43,
            floPint = 123.456f,
            finBin = ActionWithAllTypesOfInputsV3.Bin.Custom("this-is-custom!"),
            gooZen = ActionWithAllTypesOfInputsV3.Zen.Value(123),
            bahEnum = ActionWithAllTypesOfInputsV3.BahEnum.Custom("very-custom"),
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
