package it.krzeminski.githubactions.actions.actions

import io.kotest.core.spec.style.DescribeSpec
import it.krzeminski.githubactions.shouldHaveYamlArguments

class SetupNodeV2Test : DescribeSpec({
    it("renders with defaults") {
        SetupnodeV2() shouldHaveYamlArguments linkedMapOf()
    }

    it("renders with all parameters") {
        SetupnodeV2.example_full_action shouldHaveYamlArguments SetupnodeV2.example_full_map
    }
})
