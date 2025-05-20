// This file was generated using action-binding-generator. Don't change it by hand, otherwise your
// changes will be overwritten with the next binding code regeneration.
// See https://github.com/typesafegithub/github-workflows-kt for more info.
@file:Suppress("UNUSED_PARAMETER")

package io.github.typesafegithub.workflows.actions.johnsmith

import io.github.typesafegithub.workflows.domain.actions.Action
import io.github.typesafegithub.workflows.domain.actions.RegularAction
import java.util.LinkedHashMap
import java.util.Objects
import kotlin.Any
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.Map

/**
 * Action: Action With No Inputs
 *
 * Description
 *
 * [Action on GitHub](https://github.com/john-smith/action-with-no-inputs-binding-v2)
 *
 * @param _customInputs Type-unsafe map where you can put any inputs that are not yet supported by the binding
 * @param _customVersion Allows overriding action's version, for example to use a specific minor version, or a newer version that the binding doesn't yet know about
 */
public class ActionWithNoInputsBindingV2(
    vararg pleaseUseNamedArguments: Unit,
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the binding
     */
    public val _customInputs: Map<String, String> = mapOf(),
    /**
     * Allows overriding action's version, for example to use a specific minor version, or a newer version that the binding doesn't yet know about
     */
    public val _customVersion: String? = null,
) : RegularAction<Action.Outputs>("john-smith", "action-with-no-inputs-binding-v2", _customVersion ?: "v3") {
    init {
        println("WARNING: The used binding version v2 for john-smith/action-with-no-inputs-binding-v2@v3 is experimental! Last stable version is v1.")
        if (System.getenv("GITHUB_ACTIONS").toBoolean()) {
            println("""
                    |
                    |::warning title=Experimental Binding Version Used::The used binding version v2 for john-smith/action-with-no-inputs-binding-v2@v3 is experimental! Last stable version is v1.
                    """.trimMargin())
        }

    }

    @Suppress("SpreadOperator")
    override fun toYamlArguments(): LinkedHashMap<String, String> = LinkedHashMap(_customInputs)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as ActionWithNoInputsBindingV2
        return _customInputs == other._customInputs &&
            _customVersion == other._customVersion
    }

    override fun hashCode(): Int = Objects.hash(
        _customInputs,
        _customVersion,
    )

    override fun toString(): String = buildString {
        append("ActionWithNoInputsBindingV2(")
        append("""_customInputs=$_customInputs""")
        append(", ")
        append("""_customVersion=$_customVersion""")
        append(")")
    }

    /**
     * @param _customInputs Type-unsafe map where you can put any inputs that are not yet supported by the binding
     * @param _customVersion Allows overriding action's version, for example to use a specific minor version, or a newer version that the binding doesn't yet know about
     */
    public fun copy(
        vararg pleaseUseNamedArguments: Unit,
        _customInputs: Map<String, String> = this._customInputs,
        _customVersion: String? = this._customVersion,
    ): ActionWithNoInputsBindingV2 = ActionWithNoInputsBindingV2(
        _customInputs = _customInputs,
        _customVersion = _customVersion,
    )

    override fun buildOutputObject(stepId: String): Action.Outputs = Outputs(stepId)
}
