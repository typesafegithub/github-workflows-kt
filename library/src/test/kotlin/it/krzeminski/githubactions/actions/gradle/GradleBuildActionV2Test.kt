package it.krzeminski.githubactions.actions.gradle

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class GradleBuildActionV2Test : DescribeSpec({
    it("renders with defaults") {
        // given
        val action = GradleBuildActionV2()

        // when
        val yaml = action.toYamlArguments()

        // then
        yaml shouldBe linkedMapOf()
    }

    it("renders with Gradle version") {
        // given
        val action = GradleBuildActionV2(
            gradleVersion = "6.3",
        )

        // when
        val yaml = action.toYamlArguments()

        // then
        yaml shouldBe linkedMapOf(
            "gradle-version" to "6.3",
        )
    }

    it("renders with cache disabled") {
        // given
        val action = GradleBuildActionV2(
            cacheDisabled = true,
        )

        // when
        val yaml = action.toYamlArguments()

        // then
        yaml shouldBe linkedMapOf(
            "cache-disabled" to "true",
        )
    }

    it("renders with cache read-only") {
        // given
        val action = GradleBuildActionV2(
            cacheReadOnly = true,
        )

        // when
        val yaml = action.toYamlArguments()

        // then
        yaml shouldBe linkedMapOf(
            "cache-read-only" to "true",
        )
    }

    it("renders with Gradle home cache includes") {
        // given
        val action = GradleBuildActionV2(
            gradleHomeCacheIncludes = listOf("/foo", "/bar"),
        )

        // when
        val yaml = action.toYamlArguments()

        // then
        yaml shouldBe linkedMapOf(
            "gradle-home-cache-includes" to "/foo\n/bar",
        )
    }

    it("renders with Gradle home cache excludes") {
        // given
        val action = GradleBuildActionV2(
            gradleHomeCacheExcludes = listOf("/foo", "/bar"),
        )

        // when
        val yaml = action.toYamlArguments()

        // then
        yaml shouldBe linkedMapOf(
            "gradle-home-cache-excludes" to "/foo\n/bar",
        )
    }

    it("renders with arguments") {
        // given
        val action = GradleBuildActionV2(
            arguments = "clean build",
        )

        // when
        val yaml = action.toYamlArguments()

        // then
        yaml shouldBe linkedMapOf(
            "arguments" to "clean build",
        )
    }

    it("renders with build root directory") {
        // given
        val action = GradleBuildActionV2(
            buildRootDirectory = "/home/test",
        )

        // when
        val yaml = action.toYamlArguments()

        // then
        yaml shouldBe linkedMapOf(
            "build-root-directory" to "/home/test",
        )
    }

    it("renders with Gradle executable") {
        // given
        val action = GradleBuildActionV2(
            gradleExecutable = "/home/john/gradle",
        )

        // when
        val yaml = action.toYamlArguments()

        // then
        yaml shouldBe linkedMapOf(
            "gradle-executable" to "/home/john/gradle",
        )
    }
})
