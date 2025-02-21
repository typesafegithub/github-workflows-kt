package io.github.typesafegithub.workflows.shared.internal.model

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.shouldBe

class VersionTest :
    FunSpec(
        {
            context("parsing") {
                listOf(
                    Pair("v1.2.3", Triple(1, 2, 3)),
                    Pair("1.2.3", Triple(1, 2, 3)),
                    Pair("v1.2.3.4", Triple(1, 2, 3)),
                    Pair("1.2.3.4", Triple(1, 2, 3)),
                    Pair("v1.2", Triple(1, 2, 0)),
                    Pair("v3", Triple(3, 0, 0)),
                    Pair("V3", Triple(3, 0, 0)),
                    Pair("v3-prerelease", Triple(0, 0, 0)),
                    Pair("beta-v3", Triple(0, 0, 0)),
                ).forEach { (string, version) ->
                    test("correctly parses $string") {
                        val parsedVersion = Version(string)
                        assertSoftly {
                            parsedVersion.major shouldBe version.first
                            parsedVersion.minor shouldBe version.second
                            parsedVersion.patch shouldBe version.third
                        }
                    }
                }
            }

            context("isMajorVersion") {
                listOf(
                    Pair("v1.2.3", false),
                    Pair("v1.2", false),
                    Pair("v3", true),
                    Pair("V3", true),
                    Pair("v3-prerelease", false),
                    Pair("beta-v3", false),
                ).forEach { (version, isMajor) ->
                    test("isMajorVersion works correctly for $version") {
                        Version(version).isMajorVersion() shouldBe isMajor
                    }
                }
            }

            context("compareTo") {
                listOf(
                    Triple("v1.2.1", "v1.2", true),
                    Triple("alpha", "beta", false),
                    Triple("1.2.3", "1.2.3.4", false),
                    Triple("3.2.1", "v3", true),
                ).forEach { (left, right, leftIsGreater) ->
                    test("compareTo works correctly for $left vs. $right") {
                        if (leftIsGreater) {
                            Version(left) shouldBeGreaterThan Version(right)
                        } else {
                            Version(right) shouldBeGreaterThan Version(left)
                        }
                    }
                }
            }
        },
    )
