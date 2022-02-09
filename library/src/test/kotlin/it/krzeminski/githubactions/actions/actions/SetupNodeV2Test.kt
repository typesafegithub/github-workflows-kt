@file:Suppress("RedundantVisibilityModifier")

package it.krzeminski.githubactions.actions.actions

import io.kotest.core.spec.style.DescribeSpec
import it.krzeminski.githubactions.shouldHaveYamlArguments
import kotlin.Suppress

public class SetupNodeV2Test : DescribeSpec({
    it("renders with defaults") {
        SetupNodeV2() shouldHaveYamlArguments linkedMapOf()
    }

    it("renders with all parameters") {
        SetupNodeV2.example_full_action shouldHaveYamlArguments SetupNodeV2.example_full_map
    }
})
