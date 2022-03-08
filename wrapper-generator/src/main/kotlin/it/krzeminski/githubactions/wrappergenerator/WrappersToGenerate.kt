package it.krzeminski.githubactions.wrappergenerator

import it.krzeminski.githubactions.wrappergenerator.domain.ActionCoords
import it.krzeminski.githubactions.wrappergenerator.domain.WrapperRequest
import it.krzeminski.githubactions.wrappergenerator.domain.typings.BooleanTyping
import it.krzeminski.githubactions.wrappergenerator.domain.typings.EnumTyping
import it.krzeminski.githubactions.wrappergenerator.domain.typings.IntegerTyping
import it.krzeminski.githubactions.wrappergenerator.domain.typings.IntegerWithSpecialValueTyping
import it.krzeminski.githubactions.wrappergenerator.domain.typings.ListOfTypings
import it.krzeminski.githubactions.wrappergenerator.domain.typings.StringTyping

val wrappersToGenerate = listOf(
    WrapperRequest(
        ActionCoords("actions", "cache", "v2"),
        mapOf(
            "path" to ListOfTypings("\\n"),
            "restore-keys" to ListOfTypings("\\n"),
            "upload-chunk-size" to IntegerTyping,
        )
    ),
    WrapperRequest(
        ActionCoords("actions", "checkout", "v2", deprecatedByVersion = "v3"),
        mapOf(
            "ssh-strict" to BooleanTyping,
            "persist-credentials" to BooleanTyping,
            "clean" to BooleanTyping,
            "fetch-depth" to IntegerWithSpecialValueTyping("FetchDepth", mapOf("Infinite" to 0)),
            "lfs" to BooleanTyping,
            "submodules" to BooleanTyping,
        ),
    ),
    WrapperRequest(
        ActionCoords("actions", "checkout", "v3"),
        mapOf(
            "ssh-strict" to BooleanTyping,
            "persist-credentials" to BooleanTyping,
            "clean" to BooleanTyping,
            "fetch-depth" to IntegerWithSpecialValueTyping("FetchDepth", mapOf("Infinite" to 0)),
            "lfs" to BooleanTyping,
            "submodules" to BooleanTyping,
        ),
    ),
    WrapperRequest(
        ActionCoords("actions", "download-artifact", "v2")
    ),
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
            "cache-dependency-path" to ListOfTypings("\\n"),
        ),
    ),
    WrapperRequest(
        ActionCoords("actions", "setup-python", "v2"),
        mapOf(
            "cache" to EnumTyping("PackageManager", listOf("pip", "pipenv")),
            "architecture" to EnumTyping("Architecture", listOf("x64", "x86")),
            "cache-dependency-path" to ListOfTypings("\\n"),
        )
    ),
    WrapperRequest(
        ActionCoords("actions", "upload-artifact", "v2"),
        mapOf(
            "path" to ListOfTypings("\\n"),
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
            "driver-opts" to ListOfTypings("\\n"),
            "install" to BooleanTyping,
            "use" to BooleanTyping,
        )
    ),
    WrapperRequest(
        ActionCoords("docker", "build-push-action", "v2"),
        mapOf(
            "add-hosts" to ListOfTypings("\\n"),
            "allow" to ListOfTypings("\\n"),
            "build-args" to ListOfTypings("\\n"),
            "cache-from" to ListOfTypings("\\n"),
            "cache-to" to ListOfTypings("\\n"),
            "labels" to ListOfTypings(","),
            "load" to BooleanTyping,
            "no-cache" to BooleanTyping,
            "outputs" to ListOfTypings(","),
            "platforms" to ListOfTypings(","),
            "pull" to BooleanTyping,
            "push" to BooleanTyping,
            "secrets" to ListOfTypings("\\n"),
            "secret-files" to ListOfTypings("\\n"),
            "ssh" to ListOfTypings("\\n"),
            "tags" to ListOfTypings("\\n"),
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
    WrapperRequest(
        ActionCoords("madhead", "check-gradle-version", "v1")
    ),
    WrapperRequest(
        ActionCoords("madhead", "read-java-properties", "latest"),
        mapOf("all" to BooleanTyping)
    ),
    WrapperRequest(
        ActionCoords("madhead", "semver-utils", "latest")
    ),

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
            "gradle-home-cache-includes" to ListOfTypings("\\n"),
            "gradle-home-cache-excludes" to ListOfTypings("\\n"),
            "cache-write-only" to StringTyping,
            "gradle-home-cache-strict-match" to StringTyping,
        ),
    ),
    WrapperRequest(
        ActionCoords("gradle", "wrapper-validation-action", "v1"),
        mapOf(
            "min-wrapper-count" to IntegerTyping,
            "allow-snapshots" to BooleanTyping,
            "allow-checksums" to ListOfTypings(","),
        ),
    ),

    WrapperRequest(
        ActionCoords("gradle-update", "update-gradle-wrapper-action", "v1"),
        mapOf(
            "reviewers" to ListOfTypings(","),
            "team-reviewers" to ListOfTypings(","),
            "labels" to ListOfTypings(","),
            "set-distribution-checksum" to BooleanTyping,
            "paths" to ListOfTypings(","),
            "paths-ignore" to ListOfTypings(","),
        ),
    ),

    WrapperRequest(
        ActionCoords("peterjgrainger", "action-create-branch", "v2.1.0")
    ),

    WrapperRequest(
        ActionCoords("repo-sync", "pull-request", "v2"),
        mapOf(
            "pr_reviewer" to ListOfTypings(","),
            "pr_assignee" to ListOfTypings(","),
            "pr_label" to ListOfTypings(","),
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
    WrapperRequest(
        ActionCoords("10up", "action-wordpress-plugin-deploy", "v2.0.0"),
        mapOf(
            "generate-zip" to BooleanTyping,
        )
    ),
    WrapperRequest(
        ActionCoords("8398a7", "action-slack", "v3"),
        mapOf(
            "status" to EnumTyping(
                "Status",
                listOf("success", "failure", "cancelled", "custom"),
                listOf("Success", "Failure", "Cancelled", "CustomEnum"),
            ),
            "fields" to ListOfTypings(","),
            "if_mention" to ListOfTypings(
                delimiter = ",",
                typing = EnumTyping(
                    "MentionStatus",
                    listOf("success", "failure", "cancelled", "custom", "always"),
                    listOf("Success", "Failure", "Cancelled", "CustomEnum", "Always"),
                ),
            ),
        )
    ),
    WrapperRequest(
        ActionCoords("Azure", "docker-login", "v1"),
    ),
    WrapperRequest(
        ActionCoords("Azure", "login", "v1"),
        mapOf(
            "enable-AzPSSession" to BooleanTyping,
            "allow-no-subscriptions" to BooleanTyping,
            "environment" to EnumTyping(
                "Environment",
                listOf("azurecloud", "azurestack", "azureusgovernment", "azurechinacloud", "azuregermancloud"),
                listOf("AzureCloud", "AzureStack", "AzureUsGovernment", "AzureChinaCloud", "AzureGermanCloud"),
            ),
        )
    ),
    WrapperRequest(
        ActionCoords("Azure", "webapps-deploy", "v2"),
        mapOf(
            "images" to ListOfTypings("\\n"),
        )
    ),
    WrapperRequest(
        ActionCoords("Borales", "actions-yarn", "v2.3.0"),
    ),
    WrapperRequest(
        ActionCoords("GoogleCloudPlatform", "github-actions", "v0"),
        mapOf(
            "export_default_credentials" to BooleanTyping,
            "cleanup_credentials" to BooleanTyping,
            "install_components" to ListOfTypings("\\n"),
        )
    ),
    WrapperRequest(
        ActionCoords("JamesIves", "github-pages-deploy-action", "v4"),
        mapOf(
            "clean" to BooleanTyping,
            "folder" to StringTyping,
            "clean-exclude" to ListOfTypings("\\n"),
            "dry-run" to BooleanTyping,
            "single-commit" to BooleanTyping,
            "silent" to BooleanTyping,
        )
    ),
    WrapperRequest(
        ActionCoords("JasonEtco", "create-an-issue", "v2"),
        mapOf(
            "assignees" to ListOfTypings(","),
            "search_existing" to ListOfTypings(","),
            "update_existing" to BooleanTyping,
        )
    ),
    WrapperRequest(
        ActionCoords("SamKirkland", "FTP-Deploy-Action", "v4.3.0"),
        mapOf(
            "port" to IntegerTyping,
            "protocol" to EnumTyping("Protocol", listOf("ftp", "ftps", "ftps-legacy")),
            "dry-run" to BooleanTyping,
            "dangerous-clean-slate" to BooleanTyping,
            "exclude" to ListOfTypings("\\n"),
            "log-level" to EnumTyping("LogLevel", listOf("minimal", "standard", "verbose")),
            "security" to EnumTyping("Security", listOf("strict", "loose")),
        ),
    )
)
