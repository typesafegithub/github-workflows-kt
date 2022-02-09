@file:Suppress("RedundantVisibilityModifier")

package it.krzeminski.githubactions.actions.peterjgrainger

import io.kotest.core.spec.style.DescribeSpec
import it.krzeminski.githubactions.shouldHaveYamlArguments
import kotlin.Suppress

public class ActionCreateBranchV2Test : DescribeSpec({
    it("renders with defaults") {
        ActionCreateBranchV2() shouldHaveYamlArguments linkedMapOf()
    }
    it("renders with all parameters") {
        ActionCreateBranchV2.example_full_action shouldHaveYamlArguments
            ActionCreateBranchV2.example_full_map
    }
}
)
