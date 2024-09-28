package io.github.typesafegithub.workflows.shared.internal.model

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class VersionTest :
    FunSpec({
        context("isMajorVersion") {
            listOf(
                Pair("v1.2.3", false),
                Pair("v1.2", false),
                Pair("v3", true),
                // Incorrect behavior, will be fixed in https://github.com/typesafegithub/github-workflows-kt/issues/1670
                Pair("v3-prerelease", true),
            ).forEach { (version, isMajor) ->
                test("isMajorVersion works correctly for $version") {
                    Version(version).isMajorVersion() shouldBe isMajor
                }
            }
        }
    })
