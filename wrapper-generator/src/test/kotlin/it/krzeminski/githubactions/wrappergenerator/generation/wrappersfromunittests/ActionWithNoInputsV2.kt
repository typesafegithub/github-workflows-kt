// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
@file:Suppress("DEPRECATION")

package it.krzeminski.githubactions.actions.johnsmith

import it.krzeminski.githubactions.actions.Action
import java.util.LinkedHashMap
import kotlin.Deprecated
import kotlin.Suppress

/**
 * Action: Action With No Inputs
 *
 * Description
 *
 * [Action on GitHub](https://github.com/john-smith/action-with-no-inputs)
 */
@Deprecated(
    message = "This action has a newer major version",
    replaceWith = ReplaceWith("ActionWithNoInputsV3")
)
public class ActionWithNoInputsV2() : Action("john-smith", "action-with-no-inputs", "v2") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments() = LinkedHashMap<String, String>()
}
