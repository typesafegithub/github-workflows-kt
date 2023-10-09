package io.github.typesafegithub.workflows.domain

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class ContainerTest : FunSpec({
    context("Container.healthCheck") {
        test("required arguments") {
            Container.healthCheck(
                command = "test 'command'",
            ) shouldBe
                listOf(
                    "--health-cmd \"test 'command'\"",
                    "--health-interval 2s",
                    "--health-timeout 2s",
                    "--health-retries 30",
                )
        }

        test("all arguments") {
            Container.healthCheck(
                command = "test 'command'",
                intervalSeconds = 25,
                timeoutSeconds = 99,
                retries = 42,
            ) shouldBe
                listOf(
                    "--health-cmd \"test 'command'\"",
                    "--health-interval 25s",
                    "--health-timeout 99s",
                    "--health-retries 42",
                )
        }

        test("escapes double quotes in command") {
            Container.healthCheck(
                command = "test \"command\"",
            ) shouldBe
                listOf(
                    """
                    --health-cmd "test "'"'"command"'"'""
                    """.trimIndent(),
                    "--health-interval 2s",
                    "--health-timeout 2s",
                    "--health-retries 30",
                )
        }

        test("throws on invalid arguments") {
            shouldThrow<IllegalArgumentException> {
                Container.healthCheck(
                    command = "test 'command'",
                    intervalSeconds = 0,
                )
            }
            shouldThrow<IllegalArgumentException> {
                Container.healthCheck(
                    command = "test 'command'",
                    timeoutSeconds = 0,
                )
            }
            shouldThrow<IllegalArgumentException> {
                Container.healthCheck(
                    command = "test 'command'",
                    retries = 0,
                )
            }
        }
    }

    context("VolumeMapping") {
        test("throws on invalid arguments") {
            shouldThrow<IllegalArgumentException> {
                VolumeMapping(source = "", target = "/container/path")
            }
            shouldThrow<IllegalArgumentException> {
                VolumeMapping(source = "/host/path", target = "")
            }
            shouldThrow<IllegalArgumentException> {
                VolumeMapping(target = "/container/:path")
            }
            shouldThrow<IllegalArgumentException> {
                VolumeMapping(source = "/host/:path", target = "/container/path")
            }
        }
    }

    context("PortMapping") {
        test("throws on invalid arguments") {
            shouldThrow<IllegalArgumentException> {
                PortMapping(host = 0)
            }
            shouldThrow<IllegalArgumentException> {
                PortMapping(host = 65536)
            }
            shouldThrow<IllegalArgumentException> {
                PortMapping(host = 1, container = 0)
            }
            shouldThrow<IllegalArgumentException> {
                PortMapping(host = 1, container = 65536)
            }
        }

        test("should not throw on port range boundary") {
            shouldNotThrow<IllegalArgumentException> {
                PortMapping(host = 65535, container = 1)
            }
            shouldNotThrow<IllegalArgumentException> {
                PortMapping(host = 1, container = 65535)
            }
        }
    }
})
