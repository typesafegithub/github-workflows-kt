// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.johnsmith

import it.krzeminski.githubactions.actions.Action
import java.util.LinkedHashMap
import kotlin.String
import kotlin.Suppress
import kotlin.collections.Map

/**
 * Action: Action With No Inputs
 *
 * Description
 *
 * [Action on GitHub](https://github.com/john-smith/action-with-no-inputs)
 */
public class ActionWithNoInputsV3(
    /**
     * Type-unsafe map where you can put any inputs that are not yet supported by the wrapper
     */
    public val _customInputs: Map<String, String> = mapOf()
) : Action("john-smith", "action-with-no-inputs", "v3") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments() = LinkedHashMap(_customInputs)
}
