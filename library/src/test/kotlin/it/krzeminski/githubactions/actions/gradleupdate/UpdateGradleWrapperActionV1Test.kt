package it.krzeminski.githubactions.actions.gradleupdate

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class UpdateGradleWrapperActionV1Test : DescribeSpec({
    it("renders with defaults") {
        // given
        val action = UpdateGradleWrapperActionV1()

        // when
        val yaml = action.toYamlArguments()

        // then
        yaml shouldBe linkedMapOf()
    }

    it("renders with all parameters") {
        // given
        val action = UpdateGradleWrapperActionV1(
            repoToken = "repo-token1",
            reviewers = listOf("reviewer1", "reviewer2"),
            teamReviewers = listOf("team-reviewer1", "team-reviewer2"),
            labels = listOf("label1", "label2"),
            baseBranch = "base-branch1",
            targetBranch = "target-branch1",
            setDistributionChecksum = false,
            paths = listOf("path1", "path2"),
            pathsIgnore = listOf("path-ignore1", "path-ignore2"),
        )

        // when
        val yaml = action.toYamlArguments()

        // then
        yaml shouldBe linkedMapOf(
            "repo-token" to "repo-token1",
            "reviewers" to "reviewer1,reviewer2",
            "team-reviewers" to "team-reviewer1,team-reviewer2",
            "labels" to "label1,label2",
            "base-branch" to "base-branch1",
            "target-branch" to "target-branch1",
            "set-distribution-checksum" to "false",
            "paths" to "path1,path2",
            "paths-ignore" to "path-ignore1,path-ignore2",
        )
    }
})
