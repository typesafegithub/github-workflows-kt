package io.github.typesafegithub.workflows.yaml

import io.github.typesafegithub.workflows.domain.Container
import io.github.typesafegithub.workflows.domain.Credentials
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class ContainerToYamlTest : DescribeSpec({
    it("renders with required arguments") {
        val container = Container(
            image = "test-image",
        )

        container.toYaml() shouldBe mapOf(
            "image" to "test-image",
        )
    }

    it("renders with all arguments") {
        val container = Container(
            image = "test-image",
            ports = listOf("test-port1", "test-port2"),
            volumes = listOf("test-volume1", "test-volume2"),
            env = linkedMapOf("test-env-key" to "test-env-value"),
            options = listOf("test-option1", "test-option2"),
            credentials = Credentials(
                username = "test-username",
                password = "test-password",
            ),
            _customArguments = mapOf(
                "foo" to true,
                "null-string" to "null",
                "null-value" to null,
                "empty-string" to "",
            ),
        )

        container.toYaml() shouldBe mapOf(
            "image" to "test-image",
            "ports" to listOf("test-port1", "test-port2"),
            "volumes" to listOf("test-volume1", "test-volume2"),
            "env" to mapOf("test-env-key" to "test-env-value"),
            "options" to "test-option1 test-option2",
            "credentials" to mapOf(
                "username" to "test-username",
                "password" to "test-password",
            ),
            "foo" to true,
            "null-string" to "null",
            "null-value" to null,
            "empty-string" to "",
        )
    }
})
