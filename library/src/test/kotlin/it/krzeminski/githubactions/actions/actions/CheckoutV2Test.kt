package it.krzeminski.githubactions.actions.actions

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import it.krzeminski.githubactions.actions.actions.CheckoutV2.FetchDepth

class CheckoutV2Test : DescribeSpec({
    it("renders with defaults") {
        // given
        val action = CheckoutV2()

        // when
        val yaml = action.toYamlArguments()

        // then
        yaml shouldBe linkedMapOf()
    }

    it("renders with all parameters") {
        // given
        val action = CheckoutV2(
            repository = "repository1",
            ref = "ref1",
            token = "token1",
            sshKey = "ssh-key1",
            sshKnownHosts = "ssh-known-hosts1",
            sshStrict = true,
            persistCredentials = false,
            path = "path1",
            clean = true,
            fetchDepth = FetchDepth.Quantity(1),
            lfs = false,
            submodules = true,
        )

        // when
        val yaml = action.toYamlArguments()

        // then
        yaml shouldBe linkedMapOf(
            "repository" to "repository1",
            "ref" to "ref1",
            "token" to "token1",
            "ssh-key" to "ssh-key1",
            "ssh-known-hosts" to "ssh-known-hosts1",
            "ssh-strict" to "true",
            "persist-credentials" to "false",
            "path" to "path1",
            "clean" to "true",
            "fetch-depth" to "1",
            "lfs" to "false",
            "submodules" to "true",
        )
    }

    it("renders fetch depth with specified infinite fetch depth") {
        // given
        val action = CheckoutV2(
            fetchDepth = FetchDepth.Infinite,
        )

        // when
        val yaml = action.toYamlArguments()

        // then
        yaml shouldBe linkedMapOf(
            "fetch-depth" to "0",
        )
    }
})
