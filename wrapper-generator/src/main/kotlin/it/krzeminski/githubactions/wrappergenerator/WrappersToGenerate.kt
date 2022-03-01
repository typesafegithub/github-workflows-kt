package it.krzeminski.githubactions.wrappergenerator

import it.krzeminski.githubactions.wrappergenerator.domain.ActionCoords
import it.krzeminski.githubactions.wrappergenerator.domain.WrapperRequest
import it.krzeminski.githubactions.wrappergenerator.domain.typings.BooleanTyping
import it.krzeminski.githubactions.wrappergenerator.domain.typings.EnumTyping
import it.krzeminski.githubactions.wrappergenerator.domain.typings.IntegerTyping
import it.krzeminski.githubactions.wrappergenerator.domain.typings.IntegerWithSpecialValueTyping
import it.krzeminski.githubactions.wrappergenerator.domain.typings.ListsOfTypings
import it.krzeminski.githubactions.wrappergenerator.domain.typings.StringTyping

val wrappersToGenerate = listOf(
    WrapperRequest(
        ActionCoords("actions", "cache", "v2"),
        mapOf(
            "path" to ListsOfTypings("\\n"),
            "restore-keys" to ListsOfTypings("\\n"),
            "upload-chunk-size" to IntegerTyping,
        )
    ),
    WrapperRequest(
        ActionCoords("actions", "checkout", "v2"),
        mapOf(
            "ssh-strict" to BooleanTyping,
            "persist-credentials" to BooleanTyping,
            "clean" to BooleanTyping,
            "fetch-depth" to IntegerWithSpecialValueTyping("FetchDepth", mapOf("Infinite" to 0)),
            "lfs" to BooleanTyping,
            "submodules" to BooleanTyping,
        ),
    ),
    WrapperRequest(ActionCoords("actions", "download-artifact", "v2")),
    WrapperRequest(
        ActionCoords("actions", "setup-java", "v2"),
        mapOf(
            "distribution" to EnumTyping(
                "Distribution",
                listOf(
                    "adopt",
                    "adopt-hotspot",
                    "adopt-openj9",
                    "liberica",
                    "microsoft",
                    "temurin",
                    "zulu",
                ),
            ),
            "java-package" to EnumTyping(
                "JavaPackage",
                listOf("jdk", "jre", "jdk+fx", "jre+fx"),
            ),
            "check-latest" to BooleanTyping,
            "overwrite-settings" to BooleanTyping,
            "cache" to EnumTyping("BuildPlatform", listOf("maven", "gradle")),
        ),
    ),
    WrapperRequest(
        ActionCoords("actions", "setup-node", "v2"),
        mapOf(
            "always-auth" to BooleanTyping,
            "check-latest" to BooleanTyping,
            "cache" to EnumTyping("PackageManager", listOf("npm", "yarn", "pnpm")),
            "cache-dependency-path" to ListsOfTypings("\\n"),
        ),
    ),
    WrapperRequest(
        ActionCoords("actions", "setup-python", "v2"),
        mapOf(
            "cache" to EnumTyping("PackageManager", listOf("pip", "pipenv")),
            "architecture" to EnumTyping("Architecture", listOf("x64", "x86")),
            "cache-dependency-path" to ListsOfTypings("\\n"),
        )
    ),
    WrapperRequest(
        ActionCoords("actions", "upload-artifact", "v2"),
        mapOf(
            "path" to ListsOfTypings("\\n"),
            "if-no-files-found" to EnumTyping(
                "BehaviorIfNoFilesFound",
                listOf("warn", "error", "ignore"),
            ),
            "retention-days" to IntegerWithSpecialValueTyping(
                "RetentionPeriod",
                mapOf("Default" to 0),
            )
        ),
    ),

    WrapperRequest(
        ActionCoords("docker", "login-action", "v1"),
        mapOf(
            "ecr" to BooleanTyping,
            "logout" to BooleanTyping,
        )
    ),
    WrapperRequest(
        ActionCoords("docker", "setup-buildx-action", "v1"),
        mapOf(
            "driver-opts" to ListsOfTypings("\\n"),
            "install" to BooleanTyping,
            "use" to BooleanTyping,
        )
    ),
    WrapperRequest(
        ActionCoords("docker", "build-push-action", "v2"),
        mapOf(
            "add-hosts" to ListsOfTypings("\\n"),
            "allow" to ListsOfTypings("\\n"),
            "build-args" to ListsOfTypings("\\n"),
            "cache-from" to ListsOfTypings("\\n"),
            "cache-to" to ListsOfTypings("\\n"),
            "labels" to ListsOfTypings(","),
            "load" to BooleanTyping,
            "no-cache" to BooleanTyping,
            "outputs" to ListsOfTypings(","),
            "platforms" to ListsOfTypings(","),
            "pull" to BooleanTyping,
            "push" to BooleanTyping,
            "secrets" to ListsOfTypings("\\n"),
            "secret-files" to ListsOfTypings("\\n"),
            "ssh" to ListsOfTypings("\\n"),
            "tags" to ListsOfTypings("\\n"),
        )
    ),

    WrapperRequest(
        ActionCoords("EndBug", "add-and-commit", "v8"),
        mapOf(
            "default_author" to EnumTyping("DefaultActor", listOf("github_actor", "user_info", "github_actions")),
            "pathspec_error_handling" to EnumTyping("PathSpecErrorHandling", listOf("ignore", "exitImmediately", "exitAtEnd")),
            "push" to StringTyping,
        )
    ),

    WrapperRequest(ActionCoords("madhead", "check-gradle-version", "v1")),
    WrapperRequest(ActionCoords("madhead", "read-java-properties", "latest"), mapOf("all" to BooleanTyping)),
    WrapperRequest(ActionCoords("madhead", "semver-utils", "latest")),

    WrapperRequest(
        ActionCoords("nobrayner", "discord-webhook", "v1"),
        mapOf(
            "include-details" to BooleanTyping,
        )
    ),

    WrapperRequest(
        ActionCoords("gradle", "gradle-build-action", "v2"),
        mapOf(
            "cache-disabled" to BooleanTyping,
            "cache-read-only" to BooleanTyping,
            "gradle-home-cache-includes" to ListsOfTypings("\\n"),
            "gradle-home-cache-excludes" to ListsOfTypings("\\n"),
            "cache-write-only" to StringTyping,
            "gradle-home-cache-strict-match" to StringTyping,
        ),
    ),
    WrapperRequest(
        ActionCoords("gradle", "wrapper-validation-action", "v1"),
        mapOf(
            "min-wrapper-count" to IntegerTyping,
            "allow-snapshots" to BooleanTyping,
            "allow-checksums" to ListsOfTypings(","),
        ),
    ),

    WrapperRequest(
        ActionCoords("gradle-update", "update-gradle-wrapper-action", "v1"),
        mapOf(
            "reviewers" to ListsOfTypings(","),
            "team-reviewers" to ListsOfTypings(","),
            "labels" to ListsOfTypings(","),
            "set-distribution-checksum" to BooleanTyping,
            "paths" to ListsOfTypings(","),
            "paths-ignore" to ListsOfTypings(","),
        ),
    ),

    WrapperRequest(ActionCoords("peterjgrainger", "action-create-branch", "v2.1.0")),

    WrapperRequest(
        ActionCoords("repo-sync", "pull-request", "v2"),
        mapOf(
            "pr_reviewer" to ListsOfTypings(","),
            "pr_assignee" to ListsOfTypings(","),
            "pr_label" to ListsOfTypings(","),
            "pr_draft" to BooleanTyping,
            "pr_allow_empty" to BooleanTyping,
        ),
    ),

    WrapperRequest(
        ActionCoords("AkhileshNS", "heroku-deploy", "v3.12.12"),
        mapOf(
            "dontuseforce" to BooleanTyping,
            "dontautocreate" to BooleanTyping,
            "usedocker" to BooleanTyping,
            "delay" to IntegerTyping,
            "rollbackonhealthcheckfailed" to BooleanTyping,
            "justlogin" to BooleanTyping,
            "docker_heroku_process_type" to EnumTyping("HerokuProcessType", listOf("web", "worker")),
            "docker_build_args" to StringTyping,
        )
    ),
)
