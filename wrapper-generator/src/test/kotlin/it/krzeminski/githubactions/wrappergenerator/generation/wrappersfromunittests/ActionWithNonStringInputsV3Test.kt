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
            booZoo = listOf("foo", "bar"),
            finBin = ActionWithNonStringInputsV3.Bin.BooBar,
        )

        // when
        val yaml = action.toYamlArguments()

        // then
        yaml shouldBe linkedMapOf(
            "foo-bar" to "test",
            "baz-goo" to "true",
            "bin-kin" to "false",
            "int-pint" to "43",
            "boo-zoo" to "foo,bar",
            "fin-bin" to "boo-bar",
        )
    }

    it("works for enum custom value") {
        // given
        val action = ActionWithNonStringInputsV3(
            fooBar = "test",
            bazGoo = true,
            binKin = false,
            intPint = 43,
            booZoo = listOf("foo", "bar"),
            finBin = ActionWithNonStringInputsV3.Bin.Custom("this-is-custom!"),
        )

        // when
        val yaml = action.toYamlArguments()

        // then
        yaml shouldBe linkedMapOf(
            "foo-bar" to "test",
            "baz-goo" to "true",
            "bin-kin" to "false",
            "int-pint" to "43",
            "boo-zoo" to "foo,bar",
            "fin-bin" to "this-is-custom!",
        )
    }
})
