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
        ActionCoords("EndBug", "add-and-commit", "v8"),
        mapOf(
            "default_author" to EnumTyping("DefaultActor", listOf("github_actor", "user_info", "github_actions")),
            "pathspec_error_handling" to EnumTyping("PathSpecErrorHandling", listOf("ignore", "exitImmediately", "exitAtEnd")),
        )
    ),

    WrapperRequest(ActionCoords("madhead", "check-gradle-version", "v1")),
    WrapperRequest(ActionCoords("madhead", "read-java-properties", "latest"), mapOf("all" to BooleanTyping)),
    WrapperRequest(ActionCoords("madhead", "semver-utils", "latest")),

    WrapperRequest(
        ActionCoords("gradle", "gradle-build-action", "v2"),
        mapOf(
            "cache-disabled" to BooleanTyping,
            "cache-read-only" to BooleanTyping,
            "gradle-home-cache-includes" to ListOfStringsTyping("\\n"),
            "gradle-home-cache-excludes" to ListOfStringsTyping("\\n"),
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
        )
    ),
    WrapperRequest(ActionCoords("10up", "action-wordpress-plugin-deploy", "v2.0.0"),
        mapOf(
            "generate-zip" to BooleanTyping,
        )
    ),
    WrapperRequest(
        ActionCoords("8398a7", "action-slack", "v3"),
        mapOf(
            "status" to EnumTyping("Status", listOf("success", "failure", "cancelled")),
            "fields" to ListOfStringsTyping(","),
            "if_mention" to ListOfStringsTyping(","),
        )
    ),
    WrapperRequest(
        ActionCoords("Azure", "docker-login", "v1"),
        mapOf()
    ),
    WrapperRequest(
        ActionCoords("Azure", "login", "v1"),
        mapOf(
            "enable-AzPSSession" to BooleanTyping,
            "allow-no-subscriptions" to BooleanTyping,
            "environment" to EnumTyping("Environment",
                listOf("azurecloud", "azurestack", "azureusgovernment", "azurechinacloud", "azuregermancloud")
            ),
        )
    ),
    WrapperRequest(
        ActionCoords("Azure", "webapps-deploy", "v2"),
        mapOf(
            "images" to ListOfStringsTyping("\\n"),
        )
    ),
    WrapperRequest(ActionCoords("Borales", "actions-yarn", "v2.3.0"),
        mapOf()
    ),
    WrapperRequest(
        ActionCoords("GoogleCloudPlatform", "github-actions", "v0"),
        mapOf(
            "export_default_credentials" to BooleanTyping,
            "cleanup_credentials" to BooleanTyping,
            "install_components" to ListOfStringsTyping("\\n"),
        )
    ),
    WrapperRequest(
        ActionCoords("JamesIves", "github-pages-deploy-action", "v4"),
        mapOf(
            "clean" to BooleanTyping,
            "folder" to StringTyping,
            "clean-exclude" to ListOfStringsTyping("\\n"),
            "dry-run" to BooleanTyping,
            "single-commit" to BooleanTyping,
        )
    ),
    WrapperRequest(
        ActionCoords("JasonEtco", "create-an-issue", "v2"),
        mapOf(
            "assignees" to ListOfStringsTyping(","),
            "search_existing" to ListOfStringsTyping(","),
        )
    ),
    WrapperRequest(
        ActionCoords("SamKirkland", "FTP-Deploy-Action", "v4.3.0"),
        mapOf(
            "port" to IntegerTyping,
            "protocol" to EnumTyping("Protocol", listOf("ftp", "ftps", "ftps-legacy")),
            "dry-run" to BooleanTyping,
            "dangerous-clean-slate" to BooleanTyping,
            "exclude" to ListOfStringsTyping("\\n"),
            "log-level" to EnumTyping("LogLevel", listOf("minimal", "standard", "verbose")),
        ),
    )
)
