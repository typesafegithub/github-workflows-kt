package it.krzeminski.githubactions.wrappergenerator.versions

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.FunSpec
import io.kotest.data.Table2
import io.kotest.data.forAll
import io.kotest.data.headers
import io.kotest.data.row
import io.kotest.data.table
import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe

class SuggestVersionsKtTest : FunSpec({
    test("Compare versions") {
        val sortedVersions = listOf(
            "v1.0.0",
            "v1.0.9",
            "v1.0.12",
            "v1.2.0",
            "v1.9.0",
            "v1.13.1",
            "v9.0.0",
            "v12.0.0",
        )
        sortedVersions.sortedBy { VersionComparable(it) } shouldBe sortedVersions
    }

    context("Wrappers using major versions") {

        test("No available versions") {
            val testCases: Table2<String, String> = table(
                headers("currentVersions", "availableVersions"),
                row("v2", ""),
                row("v2", "v1.1.1"),
                row("v2", "v1.0.0, v1.1.1, v2.0.1, v2.1.0"),
                row("v2", "v2.0.1"),
                row("v2, v3", "v1.1.1, v2.0.1, v3.0.0, v3.4.0"),
                row("v3, v2", "v1.1.1, v2.0.1, v3.0.0, v3.4.0"),
                row("v12", "v9.0.1"),
            )
            testCases.forAll { current, available ->
                suggestNewerVersion(current.split(", "), available.split(", ")) shouldBe null
            }
        }

        test("New major version") {
            assertSoftly {

                suggestNewerVersion(
                    listOf("v1"), listOf("v1.1.1", "v2.0.1", "v3.1.1")
                ) shouldBe "new major version(s) available: [v2, v3]"

                suggestNewerVersion(
                    listOf("v2"), listOf("v1.1.1", "v2.0.1", "v3.1.1")
                ) shouldBe "new major version(s) available: [v3]"

                suggestNewerVersion(
                    listOf("v9"), listOf("v12.0.1")
                ) shouldBe "new major version(s) available: [v12]"
            }
        }
    }

    context("Wrappers using hardcoded version") {
        test("No available versions") {
            val currentVersion = "v2.1.0"
            val testCases = listOf(
                "",
                "v2.1.0",
                "v1.1.0, v1.2.0",
                "v1.1.0, v2.0.0",
                "v1.1.0, v2.1.0",
            )
            testCases.forAll { available ->
                suggestNewerVersion(listOf(currentVersion), available.split(", ")) shouldBe null
            }
        }

        test("Minor version available") {
            assertSoftly {
                suggestNewerVersion(
                    listOf("v2.1.0", "v1.0.4"),
                    listOf("v2.1.0", "v1.0.4", "v2.3.0", "v1.0.5"),
                ) shouldBe "new minor version available: v2.3.0"

                suggestNewerVersion(
                    listOf("v9.0.1"),
                    listOf("v12.1.2"),
                ) shouldBe "new minor version available: v12.1.2"

                suggestNewerVersion(
                    listOf("v9.0.9"),
                    listOf("v9.0.12"),
                ) shouldBe "new minor version available: v9.0.12"
            }
        }
    }
})
