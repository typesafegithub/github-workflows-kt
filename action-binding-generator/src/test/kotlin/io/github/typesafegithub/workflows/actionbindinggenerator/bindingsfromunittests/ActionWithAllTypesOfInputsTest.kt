package io.github.typesafegithub.workflows.actionbindinggenerator.bindingsfromunittests

import io.github.typesafegithub.workflows.actionbindinggenerator.constructAction
import io.github.typesafegithub.workflows.actionbindinggenerator.versioning.BindingVersion
import io.github.typesafegithub.workflows.actionbindinggenerator.versioning.BindingVersion.V1
import io.github.typesafegithub.workflows.actionbindinggenerator.versioning.BindingVersion.V2
import io.github.typesafegithub.workflows.actionbindinggenerator.withAllBindingVersions
import io.github.typesafegithub.workflows.actions.johnsmith.ActionWithAllTypesOfInputsBindingV1
import io.github.typesafegithub.workflows.actions.johnsmith.ActionWithAllTypesOfInputsBindingV2
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class ActionWithAllTypesOfInputsTest : DescribeSpec({
    context("correctly translates all types of inputs") {
        withAllBindingVersions { bindingVersion ->
            // given
            val action = constructAction(
                owner = "johnsmith",
                classBaseName = "ActionWithAllTypesOfInputs",
                bindingVersion = bindingVersion,
                arguments = mapOf(
                    "fooBar" to "test",
                    "bazGoo" to true,
                    "binKin" to false,
                    "intPint" to 43,
                    "floPint" to 123.456f,
                    "finBin" to bindingVersion.finBin,
                    "gooZen" to bindingVersion.gooZen,
                    "bahEnum" to bindingVersion.bahEnum,
                    "listStrings" to listOf("hello", "world"),
                    "listInts" to listOf(1, 42),
                    "listEnums" to bindingVersion.listEnums,
                    "listIntSpecial" to bindingVersion.listIntSpecial,
                ),
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
    }

    context("works for custom values") {
        withAllBindingVersions { bindingVersion ->
            // given
            val action = constructAction(
                owner = "johnsmith",
                classBaseName = "ActionWithAllTypesOfInputs",
                bindingVersion = bindingVersion,
                arguments = mapOf(
                    "fooBar" to "test",
                    "bazGoo" to true,
                    "binKin" to false,
                    "intPint" to 43,
                    "floPint" to 123.456f,
                    "finBin" to bindingVersion.customFinBin,
                    "gooZen" to bindingVersion.customGooZen,
                    "bahEnum" to bindingVersion.customBahEnum,
                ),
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
    }

    context("untyped input is sufficient for required input") {
        withAllBindingVersions { bindingVersion ->
            // given
            val action = constructAction(
                owner = "johnsmith",
                classBaseName = "ActionWithAllTypesOfInputs",
                bindingVersion = bindingVersion,
                arguments = mapOf(
                    "fooBar" to "test",
                    "bazGoo_Untyped" to "\${{ 1 == 1 }}",
                    "binKin" to false,
                    "intPint" to 43,
                    "floPint" to 123.456f,
                    "finBin" to bindingVersion.customFinBin,
                    "gooZen" to bindingVersion.customGooZen,
                    "bahEnum" to bindingVersion.customBahEnum,
                ),
            )

            // when
            val yaml = action.toYamlArguments()

            // then
            yaml shouldBe linkedMapOf(
                "foo-bar" to "test",
                "baz-goo" to "\${{ 1 == 1 }}",
                "bin-kin" to "false",
                "int-pint" to "43",
                "flo-pint" to "123.456",
                "fin-bin" to "this-is-custom!",
                "goo-zen" to "123",
                "bah-enum" to "very-custom",
            )
        }
    }

    context("validates required inputs are not missing") {
        withAllBindingVersions { bindingVersion ->
            // expect
            val exception =
                shouldThrow<IllegalArgumentException> {
                    constructAction(
                        owner = "johnsmith",
                        classBaseName = "ActionWithAllTypesOfInputs",
                        bindingVersion = bindingVersion,
                    )
                }
            when (bindingVersion) {
                V1 -> exception.message shouldBe "Either fooBar, or fooBar_Untyped must be set, one of them is required"
                V2 -> exception.message shouldBe "Either fooBar, fooBar_Untyped, or fooBarExpression must be set, one of them is required"
            }
        }
    }

    context("validates required inputs are not supplied typed and untyped") {
        withAllBindingVersions { bindingVersion ->
            // expect
            val exception =
                shouldThrow<IllegalArgumentException> {
                    constructAction(
                        owner = "johnsmith",
                        classBaseName = "ActionWithAllTypesOfInputs",
                        bindingVersion = bindingVersion,
                        arguments = mapOf(
                            "fooBar" to "test",
                            "fooBar_Untyped" to "untyped test",
                        ),
                    )
                }
            when (bindingVersion) {
                V1 -> exception.message shouldBe "Only one of fooBar, and fooBar_Untyped must be set, but not multiple"
                V2 -> exception.message shouldBe "Only one of fooBar, fooBar_Untyped, and fooBarExpression must be set, but not multiple"
            }
        }
    }

    context("validates not-required inputs are not supplied typed and untyped") {
        withAllBindingVersions { bindingVersion ->
            // expect
            val exception =
                shouldThrow<IllegalArgumentException> {
                    constructAction(
                        owner = "johnsmith",
                        classBaseName = "ActionWithAllTypesOfInputs",
                        bindingVersion = bindingVersion,
                        arguments = mapOf(
                            "fooBar" to "test",
                            "bazGoo_Untyped" to "\${{ 1 == 1 }}",
                            "binKin" to false,
                            "intPint" to 43,
                            "floPint" to 123.456f,
                            "finBin" to bindingVersion.customFinBin,
                            "gooZen" to bindingVersion.customGooZen,
                            "bahEnum" to bindingVersion.customBahEnum,
                            "listStrings" to listOf("test"),
                            "listStrings_Untyped" to "untyped test",
                        ),
                    )
                }
            when (bindingVersion) {
                V1 -> exception.message shouldBe "Only one of listStrings, and listStrings_Untyped must be set, but not multiple"
                V2 -> exception.message shouldBe "Only one of listStrings, listStrings_Untyped, listStringsExpression, and listStringsExpressions must be set, but not multiple"
            }
        }
    }

    context("exposes copy method") {
        withAllBindingVersions { bindingVersion ->
            // given
            val action = constructAction(
                owner = "johnsmith",
                classBaseName = "ActionWithAllTypesOfInputs",
                bindingVersion = bindingVersion,
                arguments = mapOf(
                    "fooBar" to "test",
                    "bazGoo" to true,
                    "binKin" to false,
                    "intPint" to 43,
                    "floPint" to 123.456f,
                    "finBin" to bindingVersion.finBin,
                    "gooZen" to bindingVersion.gooZen,
                    "bahEnum" to bindingVersion.bahEnum,
                    "listStrings" to listOf("hello", "world"),
                    "listInts" to listOf(1, 42),
                    "listEnums" to bindingVersion.listEnums,
                    "listIntSpecial" to bindingVersion.listIntSpecial,
                ),
            )

            when (bindingVersion) {
                V1 -> {
                    // when
                    action as ActionWithAllTypesOfInputsBindingV1
                    @Suppress("DATA_CLASS_INVISIBLE_COPY_USAGE_WARNING")
                    val actionWithOneChange = action.copy(fooBar = "another")

                    // then
                    actionWithOneChange.fooBar shouldBe "another"
                }

                V2 -> {
                    // when
                    action as ActionWithAllTypesOfInputsBindingV2
                    @Suppress("DATA_CLASS_INVISIBLE_COPY_USAGE_WARNING")
                    val actionWithOneChange = action.copy(fooBar = "another")

                    // then
                    actionWithOneChange.fooBar shouldBe "another"
                }
            }
        }
    }
})

private val BindingVersion.finBin
    get() = when (this) {
        V1 -> ActionWithAllTypesOfInputsBindingV1.Bin.BooBar
        V2 -> ActionWithAllTypesOfInputsBindingV2.Bin.BooBar
    }

private val BindingVersion.customFinBin
    get() = when (this) {
        V1 -> ActionWithAllTypesOfInputsBindingV1.Bin.Custom("this-is-custom!")
        V2 -> ActionWithAllTypesOfInputsBindingV2.Bin.Custom("this-is-custom!")
    }

private val BindingVersion.gooZen
    get() = when (this) {
        V1 -> ActionWithAllTypesOfInputsBindingV1.Zen.Special1
        V2 -> ActionWithAllTypesOfInputsBindingV2.Zen.Special1
    }

private val BindingVersion.customGooZen
    get() = when (this) {
        V1 -> ActionWithAllTypesOfInputsBindingV1.Zen.Value(123)
        V2 -> ActionWithAllTypesOfInputsBindingV2.Zen.Value(123)
    }

private val BindingVersion.bahEnum
    get() = when (this) {
        V1 -> ActionWithAllTypesOfInputsBindingV1.BahEnum.HelloWorld
        V2 -> ActionWithAllTypesOfInputsBindingV2.BahEnum.HelloWorld
    }

private val BindingVersion.customBahEnum
    get() = when (this) {
        V1 -> ActionWithAllTypesOfInputsBindingV1.BahEnum.Custom("very-custom")
        V2 -> ActionWithAllTypesOfInputsBindingV2.BahEnum.Custom("very-custom")
    }

private val BindingVersion.listEnums
    get() = when (this) {
        V1 -> listOf(ActionWithAllTypesOfInputsBindingV1.MyEnum.One, ActionWithAllTypesOfInputsBindingV1.MyEnum.Three)
        V2 -> listOf(ActionWithAllTypesOfInputsBindingV2.MyEnum.One, ActionWithAllTypesOfInputsBindingV2.MyEnum.Three)
    }

private val BindingVersion.listIntSpecial
    get() = when (this) {
        V1 -> listOf(
            ActionWithAllTypesOfInputsBindingV1.MyInt.TheAnswer,
            ActionWithAllTypesOfInputsBindingV1.MyInt.Value(0),
        )

        V2 -> listOf(
            ActionWithAllTypesOfInputsBindingV2.MyInt.TheAnswer,
            ActionWithAllTypesOfInputsBindingV2.MyInt.Value(0),
        )
    }
