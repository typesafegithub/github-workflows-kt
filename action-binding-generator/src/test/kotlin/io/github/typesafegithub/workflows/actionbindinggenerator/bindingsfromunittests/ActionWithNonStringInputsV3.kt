// This file was generated using action-binding-generator. Don't change it by hand, otherwise your
// changes will be overwritten with the next binding code regeneration.
// See https://github.com/typesafegithub/github-workflows-kt for more info.
@file:Suppress(
    "DataClassPrivateConstructor",
    "UNUSED_PARAMETER",
)

package io.github.typesafegithub.workflows.actions.johnsmith

import io.github.typesafegithub.workflows.domain.actions.Action
import io.github.typesafegithub.workflows.domain.actions.RegularAction
import java.util.LinkedHashMap
import kotlin.Boolean
import kotlin.Float
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.toList
import kotlin.collections.toTypedArray

/**
 * Action: Do something cool
 *
 * This is a test description that should be put in the KDoc comment for a class
 *
 * [Action on GitHub](https://github.com/john-smith/action-with-non-string-inputs)
 */
public data class ActionWithNonStringInputsV3 private constructor(
    /**
     * Short description
     */
    public val fooBar: String,
    /**
     * First boolean input!
     */
    public val bazGoo: Boolean,
    /**
     * Boolean and nullable
     */
    public val binKin: Boolean? = null,
    /**
     * Integer
     */
    public val intPint: Int,
    /**
     * Float
     */
    public val floPint: Float,
    /**
     * List of strings
     */
    public val booZoo: List<String>,
    /**
     * Enumeration
     */
    public val finBin: ActionWithNonStringInputsV3.Bin,
    /**
     * Integer with special value
     */
    public val gooZen: ActionWithNonStringInputsV3.Zen,
    /**
     * Enum with custom naming
     */
    public val bahEnum: ActionWithNonStringInputsV3.BahEnum,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the binding
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer
     * version that the binding doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<Action.Outputs>("john-smith", "action-with-non-string-inputs", _customVersion ?:
        "v3") {
    public constructor(
        vararg pleaseUseNamedArguments: Unit,
        fooBar: String,
        bazGoo: Boolean,
        binKin: Boolean? = null,
        intPint: Int,
        floPint: Float,
        booZoo: List<String>,
        finBin: ActionWithNonStringInputsV3.Bin,
        gooZen: ActionWithNonStringInputsV3.Zen,
        bahEnum: ActionWithNonStringInputsV3.BahEnum,
        _customInputs: Map<String, String> = mapOf(),
        _customVersion: String? = null,
    ) : this(fooBar=fooBar, bazGoo=bazGoo, binKin=binKin, intPint=intPint, floPint=floPint,
            booZoo=booZoo, finBin=finBin, gooZen=gooZen, bahEnum=bahEnum,
            _customInputs=_customInputs, _customVersion=_customVersion)

    @Suppress("SpreadOperator")
    override fun toYamlArguments(): LinkedHashMap<String, String> = linkedMapOf(
        *listOfNotNull(
            "foo-bar" to fooBar,
            "baz-goo" to bazGoo.toString(),
            binKin?.let { "bin-kin" to it.toString() },
            "int-pint" to intPint.toString(),
            "flo-pint" to floPint.toString(),
            "boo-zoo" to booZoo.joinToString(","),
            "fin-bin" to finBin.stringValue,
            "goo-zen" to gooZen.integerValue.toString(),
            "bah-enum" to bahEnum.stringValue,
            *_customInputs.toList().toTypedArray(),
        ).toTypedArray()
    )

    override fun buildOutputObject(stepId: String): Action.Outputs = Outputs(stepId)

    public sealed class Bin(
        public val stringValue: String,
    ) {
        public object Foo : ActionWithNonStringInputsV3.Bin("foo")

        public object BooBar : ActionWithNonStringInputsV3.Bin("boo-bar")

        public object Baz123 : ActionWithNonStringInputsV3.Bin("baz123")

        public class Custom(
            customStringValue: String,
        ) : ActionWithNonStringInputsV3.Bin(customStringValue)
    }

    public sealed class Zen(
        public val integerValue: Int,
    ) {
        public class Value(
            requestedValue: Int,
        ) : ActionWithNonStringInputsV3.Zen(requestedValue)

        public object Special1 : ActionWithNonStringInputsV3.Zen(3)

        public object Special2 : ActionWithNonStringInputsV3.Zen(-1)
    }

    public sealed class BahEnum(
        public val stringValue: String,
    ) {
        public object HelloWorld : ActionWithNonStringInputsV3.BahEnum("helloworld")

        public class Custom(
            customStringValue: String,
        ) : ActionWithNonStringInputsV3.BahEnum(customStringValue)
    }
}
