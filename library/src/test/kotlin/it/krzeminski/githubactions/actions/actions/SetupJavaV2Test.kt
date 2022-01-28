package it.krzeminski.githubactions.actions.actions

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import it.krzeminski.githubactions.actions.actions.SetupJavaV2.BuildPlatform
import it.krzeminski.githubactions.actions.actions.SetupJavaV2.Distribution.Adopt
import it.krzeminski.githubactions.actions.actions.SetupJavaV2.Distribution.Custom
import it.krzeminski.githubactions.actions.actions.SetupJavaV2.JavaPackage

class SetupJavaV2Test : DescribeSpec({
    it("renders with defaults") {
        // given
        val action = SetupJavaV2(
            distribution = Adopt,
            javaVersion = "11",
        )

        // when
        val yaml = action.toYamlArguments()

        // then
        yaml shouldBe linkedMapOf(
            "distribution" to "adopt",
            "java-version" to "11",
        )
    }

    it("renders with all parameters") {
        // given
        val action = SetupJavaV2(
            distribution = Adopt,
            javaVersion = "11",
            javaPackage = JavaPackage.Jre,
            architecture = "architecture1",
            jdkFile = "jdk-file1",
            checkLatest = true,
            serverId = "server-id1",
            serverUsername = "server-username1",
            serverPassword = "server-password1",
            settingsPath = "settings-path1",
            overwriteSettings = false,
            gpgPrivateKey = "gpg-private-key1",
            gpgPassphrase = "gpg-passphrase1",
            cache = BuildPlatform.Gradle,
        )

        // when
        val yaml = action.toYamlArguments()

        // then
        yaml shouldBe linkedMapOf(
            "distribution" to "adopt",
            "java-version" to "11",
            "java-package" to "jre",
            "architecture" to "architecture1",
            "jdkFile" to "jdk-file1",
            "check-latest" to "true",
            "server-id" to "server-id1",
            "server-username" to "server-username1",
            "server-password" to "server-password1",
            "settings-path" to "settings-path1",
            "overwrite-settings" to "false",
            "gpg-private-key" to "gpg-private-key1",
            "gpg-passphrase" to "gpg-passphrase1",
            "cache" to "gradle",
        )
    }

    it("renders with custom distribution") {
        // given
        val action = SetupJavaV2(
            distribution = Custom("my-custom-distribution"),
            javaVersion = "11",
        )

        // when
        val yaml = action.toYamlArguments()

        // then
        yaml shouldBe linkedMapOf(
            "distribution" to "my-custom-distribution",
            "java-version" to "11",
        )
    }

    it("renders with custom Java package value") {
        // given
        val action = SetupJavaV2(
            distribution = Adopt,
            javaVersion = "11",
            javaPackage = JavaPackage.Custom("custom"),
        )

        // when
        val yaml = action.toYamlArguments()

        // then
        yaml shouldBe linkedMapOf(
            "distribution" to "adopt",
            "java-version" to "11",
            "java-package" to "custom",
        )
    }

    it("renders with custom cache platform") {
        // given
        val action = SetupJavaV2(
            distribution = Adopt,
            javaVersion = "11",
            cache = BuildPlatform.Custom("custom"),
        )

        // when
        val yaml = action.toYamlArguments()

        // then
        yaml shouldBe linkedMapOf(
            "distribution" to "adopt",
            "java-version" to "11",
            "cache" to "custom",
        )
    }
})
