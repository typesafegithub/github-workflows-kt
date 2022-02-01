package it.krzeminski.githubactions.actions.actions

import io.kotest.core.spec.style.DescribeSpec
import it.krzeminski.githubactions.shouldHaveYamlArguments

class CreateBranchV210Test : DescribeSpec({
    it("renders with defaults") {
        CreateBranchV210() shouldHaveYamlArguments emptyMap()
    }

    it("renders with all parameters") {
        CreateBranchV210(
            branch = "main",
            sha = "ec955710c66016d511585121e092b27066a99c28",
        ) shouldHaveYamlArguments mapOf(
            "branch" to "main",
            "sha" to "ec955710c66016d511585121e092b27066a99c28",
        )
    }
})
