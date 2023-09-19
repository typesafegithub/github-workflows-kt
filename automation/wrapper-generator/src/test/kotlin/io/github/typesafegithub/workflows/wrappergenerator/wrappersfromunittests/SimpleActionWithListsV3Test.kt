package io.github.typesafegithub.workflows.wrappergenerator.wrappersfromunittests

import io.github.typesafegithub.workflows.actions.johnsmith.SimpleActionWithListsV3
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class SimpleActionWithListsV3Test : DescribeSpec({
    it("renders with defaults") {
        // given
        val action = SimpleActionWithListsV3(
            listStrings = listOf("hello", "world"),
            listInts = listOf(1, 42),
            listEnums = listOf(SimpleActionWithListsV3.MyEnum.One, SimpleActionWithListsV3.MyEnum.Three),
            listIntSpecial = listOf(SimpleActionWithListsV3.MyInt.TheAnswer, SimpleActionWithListsV3.MyInt.Value(0))
        )

        // when
        val yaml = action.toYamlArguments()

        // then
        yaml shouldBe linkedMapOf(
            "list-strings" to "hello,world",
            "list-ints" to "1,42",
            "list-enums" to "one,three",
            "list-int-special" to "42,0"
        )
    }
})
