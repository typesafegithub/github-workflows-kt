package it.krzeminski.githubactions.wrappergenerator

import it.krzeminski.githubactions.wrappergenerator.domain.ActionCoords
import it.krzeminski.githubactions.wrappergenerator.domain.WrapperRequest
import it.krzeminski.githubactions.wrappergenerator.domain.typings.BooleanTyping
import it.krzeminski.githubactions.wrappergenerator.domain.typings.EnumTyping
import it.krzeminski.githubactions.wrappergenerator.domain.typings.IntegerTyping
import it.krzeminski.githubactions.wrappergenerator.domain.typings.IntegerWithSpecialValueTyping
import it.krzeminski.githubactions.wrappergenerator.domain.typings.ListOfStringsTyping
import it.krzeminski.githubactions.wrappergenerator.domain.typings.StringTyping

val wrappersToGenerate = listOf(
    WrapperRequest(
        ActionCoords("actions", "cache", "v2"),
        mapOf(
            "path" to ListOfStringsTyping("\\n"),
            "restore-keys" to ListOfStringsTyping("\\n"),
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
            "cache-dependency-path" to ListOfStringsTyping("\\n"),
        ),
    ),
    WrapperRequest(
        ActionCoords("actions", "setup-python", "v2"),
        mapOf(
            "cache" to EnumTyping("PackageManager", listOf("pip", "pipenv")),
            "architecture" to EnumTyping("Architecture", listOf("x64", "x86")),
            "cache-dependency-path" to ListOfStringsTyping("\\n"),
        )
    ),
    WrapperRequest(
        ActionCoords("actions", "upload-artifact", "v2"),
        mapOf(
            "path" to ListOfStringsTyping("\\n"),
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
            "driver-opts" to ListOfStringsTyping("\\n"),
            "install" to BooleanTyping,
            "use" to BooleanTyping,
        )
    ),
    WrapperRequest(
        ActionCoords("docker", "build-push-action", "v2"),
        mapOf(
            "add-hosts" to ListOfStringsTyping("\\n"),
            "allow" to ListOfStringsTyping("\\n"),
            "build-args" to ListOfStringsTyping("\\n"),
            "cache-from" to ListOfStringsTyping("\\n"),
            "cache-to" to ListOfStringsTyping("\\n"),
            "labels" to ListOfStringsTyping(","),
            "load" to BooleanTyping,
            "no-cache" to BooleanTyping,
            "outputs" to ListOfStringsTyping(","),
            "platforms" to ListOfStringsTyping(","),
            "pull" to BooleanTyping,
            "push" to BooleanTyping,
            "secrets" to ListOfStringsTyping("\\n"),
            "secret-files" to ListOfStringsTyping("\\n"),
            "ssh" to ListOfStringsTyping("\\n"),
            "tags" to ListOfStringsTyping("\\n"),
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
            "gradle-home-cache-includes" to ListOfStringsTyping("\\n"),
            "gradle-home-cache-excludes" to ListOfStringsTyping("\\n"),
            "cache-write-only" to StringTyping,
            "gradle-home-cache-strict-match" to StringTyping,
        ),
    ),
    WrapperRequest(
        ActionCoords("gradle", "wrapper-validation-action", "v1"),
        mapOf(
            "min-wrapper-count" to IntegerTyping,
            "allow-snapshots" to BooleanTyping,
            "allow-checksums" to ListOfStringsTyping(","),
        ),
    ),

    WrapperRequest(
        ActionCoords("gradle-update", "update-gradle-wrapper-action", "v1"),
        mapOf(
            "reviewers" to ListOfStringsTyping(","),
            "team-reviewers" to ListOfStringsTyping(","),
            "labels" to ListOfStringsTyping(","),
            "set-distribution-checksum" to BooleanTyping,
            "paths" to ListOfStringsTyping(","),
            "paths-ignore" to ListOfStringsTyping(","),
        ),
    ),

    WrapperRequest(ActionCoords("peterjgrainger", "action-create-branch", "v2.1.0")),

    WrapperRequest(
        ActionCoords("repo-sync", "pull-request", "v2"),
        mapOf(
            "pr_reviewer" to ListOfStringsTyping(","),
            "pr_assignee" to ListOfStringsTyping(","),
            "pr_label" to ListOfStringsTyping(","),
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
