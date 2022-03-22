// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
@file:Suppress("DEPRECATION")

package it.krzeminski.githubactions.actions.johnsmith

import it.krzeminski.githubactions.actions.Action
import java.util.LinkedHashMap
import kotlin.Deprecated
import kotlin.String
import kotlin.Suppress
import kotlin.collections.Map

/**
 * Action: Deprecated Action
 *
 * Description
 *
 * [Action on GitHub](https://github.com/john-smith/deprecated-action)
 */
@Deprecated(
    message = "This action has a newer major version: DeprecatedActionV3",
    replaceWith = ReplaceWith("DeprecatedActionV3")
)
public class DeprecatedActionV2(
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf()
) : Action("john-smith", "deprecated-action", "v2") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments() = LinkedHashMap(_customInputs)
}
