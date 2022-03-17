// This file was generated using 'wrapper-generator' module. Don't change it by hand, your changes will
// be overwritten with the next wrapper code regeneration. Instead, consider introducing changes to the
// generator itself.
package it.krzeminski.githubactions.actions.actionsrs

import it.krzeminski.githubactions.actions.Action
import kotlin.String
import kotlin.Suppress

/**
 * Action: rust-audit-check
 *
 * Run cargo audit and check for security advisories
 *
 * [Action on GitHub](https://github.com/actions-rs/audit-check)
 */
public class AuditCheckV1(
    /**
     * GitHub Actions token
     */
    public val token: String
) : Action("actions-rs", "audit-check", "v1") {
    @Suppress("SpreadOperator")
    public override fun toYamlArguments() = linkedMapOf(
        *listOfNotNull(
            "token" to token,
        ).toTypedArray()
    )
}
