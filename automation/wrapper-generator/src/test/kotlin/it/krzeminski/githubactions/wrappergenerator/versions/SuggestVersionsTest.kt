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

class SuggestVersionsTest : FunSpec({
    fun String.versions(): List<Version> =
        split(", ").filter { it.isNotBlank() }.map { Version(it) }

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
        val actual = sortedVersions
            .shuffled()
            .sortedBy { Version(it) }
        actual shouldBe sortedVersions
    }

    context("Wrappers using major versions") {

        test("No available versions") {
            val testCases: Table2<String, String> = table(
                headers("currentVersions", "availableVersions"),
                row("v2", ""),
                row("v2", "v1, v1.1.1"),
                row("v2", "v2, v2.0.1, v2.1.0"),
                row("v2", "v2, v2.0.1, v3.0.0"),
                row("v2, v3", "v1, v1.1.1, v2, v2.0.1, v3, v3.0.0, v3.4.0"),
                row("v3, v2", "v1.1.1, v2.0.1, v3.0.0, v3.4.0"),
                row("v12", "v9"),
            )
            testCases.forAll { current, available ->
                suggestNewerVersion(current.versions(), available.versions()) shouldBe null
            }
        }

        test("New major version") {
            assertSoftly {

                suggestNewerVersion(
                    "v1".versions(),
                    "v1, v1.1.1, v2, v2.0.1, v3, v3.1.1".versions(),
                ) shouldBe "new version(s) available: [v2, v3]"

                suggestNewerVersion(
                    "v2".versions(),
                    "v1.1.1, v2, v2.0.1, v3, v3.1.1".versions(),
                ) shouldBe "new version(s) available: [v3]"

                suggestNewerVersion(
                    "v9".versions(),
                    "v12, v12.0.1".versions(),
                ) shouldBe "new version(s) available: [v12]"
            }
        }
    }

    context("Wrappers using hardcoded version") {
        test("No available versions") {
            val currentVersion = Version("v2.1.0")
            val testCases = listOf(
                "",
                "v2.1.0",
                "v1.1.0, v1.2.0",
                "v1.1.0, v2.0.0",
                "v1.1.0, v2.1.0",
            )
            testCases.forAll { available ->
                suggestNewerVersion(listOf(currentVersion), available.versions()) shouldBe null
            }
        }
    }
},)
