package it.krzeminski.githubactions.actions.actions

import io.kotest.core.spec.style.DescribeSpec
import it.krzeminski.githubactions.shouldHaveYamlArguments

class CreateBranchV210Test : DescribeSpec({
    it("renders with defaults") {
        CreateBranchV210() shouldHaveYamlArguments linkedMapOf()
    }

    it("renders with branch") {
        CreateBranchV210(
            branch = "main"
        ) shouldHaveYamlArguments linkedMapOf("branch" to "main")
    }

    it("renders with sha") {
        CreateBranchV210(
            sha = "ec955710c66016d511585121e092b27066a99c28"
        ) shouldHaveYamlArguments linkedMapOf("sha" to "ec955710c66016d511585121e092b27066a99c28")
    }
})
