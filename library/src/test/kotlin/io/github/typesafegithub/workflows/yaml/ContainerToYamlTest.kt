package io.github.typesafegithub.workflows.yaml

import io.github.typesafegithub.workflows.domain.Container
import io.github.typesafegithub.workflows.domain.Credentials
import io.github.typesafegithub.workflows.domain.PortMapping.Protocol
import io.github.typesafegithub.workflows.dsl.port
import io.github.typesafegithub.workflows.dsl.tcp
import io.github.typesafegithub.workflows.dsl.udp
import io.github.typesafegithub.workflows.dsl.volume
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
            ports = listOf(
                port(8080 to 80),
                port(22 to 22),
            ),
            volumes = listOf(
                volume("/host/path" to "/container/path"),
                volume("/host/path2" to "/container/path2"),
            ),
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
            "ports" to listOf("8080:80", "22:22"),
            "volumes" to listOf("/host/path:/container/path", "/host/path2:/container/path2"),
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

    it("renders volumes") {
        volume("/container/path")
            .toYaml() shouldBe "/container/path"

        volume("/container/path", isReadOnly = true)
            .toYaml() shouldBe "/container/path:ro"

        volume("/host/path" to "/container/path")
            .toYaml() shouldBe "/host/path:/container/path"

        volume("/host/path" to "/container/path", isReadOnly = true)
            .toYaml() shouldBe "/host/path:/container/path:ro"
    }

    it("renders ports") {
        port(22).toYaml() shouldBe "22"
        port(22 to 25).toYaml() shouldBe "22:25"
        port(22, Protocol.Custom("")).toYaml() shouldBe "22"
        port(22 to 25, Protocol.Custom("")).toYaml() shouldBe "22:25"

        port(22, Protocol.TCP).toYaml() shouldBe "22/tcp"
        port(22 to 25, Protocol.TCP).toYaml() shouldBe "22:25/tcp"
        port(22, Protocol.UDP).toYaml() shouldBe "22/udp"
        port(22 to 25, Protocol.UDP).toYaml() shouldBe "22:25/udp"
        port(22, Protocol.Custom("test")).toYaml() shouldBe "22/test"
        port(22 to 25, Protocol.Custom("test")).toYaml() shouldBe "22:25/test"

        tcp(22).toYaml() shouldBe "22/tcp"
        tcp(22 to 25).toYaml() shouldBe "22:25/tcp"
        udp(22).toYaml() shouldBe "22/udp"
        udp(22 to 25).toYaml() shouldBe "22:25/udp"
    }
})
