package it.krzeminski.githubactions.wrappergenerator.generation.wrappersfromunittests

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import it.krzeminski.githubactions.actions.johnsmith.ActionWithNonStringInputsV3

class ActionWithNonStringInputsV3Test : DescribeSpec({
    it("correctly translates boolean inputs ") {
        // given
        val action = ActionWithNonStringInputsV3(
            fooBar = "test",
            bazGoo = true,
            binKin = false,
            intPint = 43,
            floPint = 123.456f,
            booZoo = listOf("foo", "bar"),
            finBin = ActionWithNonStringInputsV3.Bin.BooBar,
            gooZen = ActionWithNonStringInputsV3.Zen.Special1,
            bahEnum = ActionWithNonStringInputsV3.BahEnum.HelloWorld
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
            "boo-zoo" to "foo,bar",
            "fin-bin" to "boo-bar",
            "goo-zen" to "3",
            "bah-enum" to "helloworld",
        )
    }

    it("works for custom values") {
        // given
        val action = ActionWithNonStringInputsV3(
            fooBar = "test",
            bazGoo = true,
            binKin = false,
            intPint = 43,
            floPint = 123.456f,
            booZoo = listOf("foo", "bar"),
            finBin = ActionWithNonStringInputsV3.Bin.Custom("this-is-custom!"),
            gooZen = ActionWithNonStringInputsV3.Zen.Value(123),
            bahEnum = ActionWithNonStringInputsV3.BahEnum.Custom("very-custom"),
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
            "boo-zoo" to "foo,bar",
            "fin-bin" to "this-is-custom!",
            "goo-zen" to "123",
            "bah-enum" to "very-custom",
        )
    }
})
