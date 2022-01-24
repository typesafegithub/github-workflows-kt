package it.krzeminski.githubactions.actions.madhead

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class ReadJavaPropertiesTest : DescribeSpec({
    it("renders with defaults") {
        // given
        val action = ReadJavaProperties("test.properties")

        // when
        val yaml = action.toYamlArguments()

        // then
        yaml shouldBe linkedMapOf(
            "file" to "test.properties"
        )
    }

    describe("property parameter") {
        it("renders with specified property parameter") {
            // given
            val action = ReadJavaProperties(
                file = "test.properties",
                property = "test.property"
            )

            // when
            val yaml = action.toYamlArguments()

            // then
            yaml shouldBe linkedMapOf(
                "file" to "test.properties",
                "property" to "test.property"
            )
        }
    }

    describe("all parameter") {
        it("renders with all=true") {
            // given
            val action = ReadJavaProperties(
                file = "test.properties",
                all = true
            )

            // when
            val yaml = action.toYamlArguments()

            // then
            yaml shouldBe linkedMapOf(
                "file" to "test.properties",
                "all" to "true"
            )
        }

        it("renders with all=false") {
            // given
            val action = ReadJavaProperties(
                file = "test.properties",
                all = false
            )

            // when
            val yaml = action.toYamlArguments()

            // then
            yaml shouldBe linkedMapOf(
                "file" to "test.properties",
                "all" to "false",
            )
        }
    }

    describe("default parameter") {
        it("renders with specified default parameter") {
            // given
            val action = ReadJavaProperties(
                file = "test.properties",
                default = "default.property"
            )

            // when
            val yaml = action.toYamlArguments()

            // then
            yaml shouldBe linkedMapOf(
                "file" to "test.properties",
                "default" to "default.property"
            )
        }
    }
})
