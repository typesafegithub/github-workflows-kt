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
        ActionCoords("actions-rs", "audit-check", "v1"),
    ),
    WrapperRequest(
        ActionCoords("actions-rs", "cargo", "v1"),
        mapOf(
            "use-cross" to BooleanTyping,
            "command" to EnumTyping(
                "Command",
                listOf(
                    "help", "version",
                    "bench", "build", "check", "clean", "doc", "fetch", "fix", "run", "rustc", "rustdoc", "test", "report",
                    "generate-lockfile", "locate-project", "metadata", "pkgid", "tree", "update", "vendor", "verify-project",
                    "init", "install", "new", "search", "uninstall",
                    "login", "owner", "package", "publish", "yank"
                )
            ),
            "args" to ListOfTypings(" "),
        )
    ),
    WrapperRequest(
        ActionCoords("actions-rs", "clippy-check", "v1"),
        mapOf(
            "use-cross" to BooleanTyping,
            "args" to ListOfTypings(" "),
        )
    ),
    WrapperRequest(
        ActionCoords("actions-rs", "toolchain", "v1"),
        mapOf(
            "default" to BooleanTyping,
            "override" to BooleanTyping,
            "components" to ListOfTypings(","),
        )
    ),
    WrapperRequest(
        ActionCoords("actions", "cache", "v2", deprecatedByVersion = "v3"),
        mapOf(
            "path" to ListOfTypings("\\n"),
            "restore-keys" to ListOfTypings("\\n"),
            "upload-chunk-size" to IntegerTyping,
        )
    ),
    WrapperRequest(
        ActionCoords("actions", "cache", "v3"),
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
            "set-safe-directory" to BooleanTyping,
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
            "set-safe-directory" to BooleanTyping,
        ),
    ),
    WrapperRequest(
        ActionCoords("actions", "create-release", "v1"),
        mapOf(
            "draft" to BooleanTyping,
            "prerelease" to BooleanTyping,
        )
    ),
    WrapperRequest(
        ActionCoords("actions", "download-artifact", "v2", deprecatedByVersion = "v3")
    ),
    WrapperRequest(
        ActionCoords("actions", "download-artifact", "v3")
    ),
    WrapperRequest(
        ActionCoords("actions", "first-interaction", "v1"),
    ),
    WrapperRequest(
        ActionCoords("actions", "github-script", "v6"),
        mapOf(
            "debug" to BooleanTyping,
            "previews" to ListOfTypings(","),
            "result-encoding" to EnumTyping("Encoding", listOf("string", "json"))
        )
    ),
    WrapperRequest(
        ActionCoords("actions", "labeler", "v4"),
        mapOf(
            "sync-labels" to BooleanTyping,
        )
    ),
    WrapperRequest(
        ActionCoords("actions", "setup-dotnet", "v2"),
        mapOf(
            "include-prerelease" to BooleanTyping,
        )
    ),
    WrapperRequest(
        ActionCoords("actions", "setup-go", "v3"),
        mapOf(
            "check-latest" to BooleanTyping,
            "cache" to BooleanTyping,
        )
    ),
    WrapperRequest(
        ActionCoords("actions", "setup-java", "v2", deprecatedByVersion = "v3"),
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
        ActionCoords("actions", "setup-java", "v3"),
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
        ActionCoords("actions", "setup-node", "v2", deprecatedByVersion = "v3"),
        mapOf(
            "always-auth" to BooleanTyping,
            "check-latest" to BooleanTyping,
            "cache" to EnumTyping("PackageManager", listOf("npm", "yarn", "pnpm")),
            "cache-dependency-path" to ListOfTypings("\\n"),
        ),
    ),
    WrapperRequest(
        ActionCoords("actions", "setup-node", "v3"),
        mapOf(
            "always-auth" to BooleanTyping,
            "check-latest" to BooleanTyping,
            "cache" to EnumTyping("PackageManager", listOf("npm", "yarn", "pnpm")),
            "cache-dependency-path" to ListOfTypings("\\n"),
        ),
    ),
    WrapperRequest(
        ActionCoords("actions", "setup-python", "v2", deprecatedByVersion = "v3"),
        mapOf(
            "cache" to EnumTyping("PackageManager", listOf("pip", "pipenv")),
            "architecture" to EnumTyping("Architecture", listOf("x64", "x86")),
            "cache-dependency-path" to ListOfTypings("\\n"),
        )
    ),
    WrapperRequest(
        ActionCoords("actions", "setup-python", "v3", deprecatedByVersion = "v4"),
        mapOf(
            "cache" to EnumTyping("PackageManager", listOf("pip", "pipenv", "poetry")),
            "architecture" to EnumTyping("Architecture", listOf("x64", "x86")),
            "cache-dependency-path" to ListOfTypings("\\n"),
        )
    ),
    WrapperRequest(
        ActionCoords("actions", "setup-python", "v4"),
        mapOf(
            "cache" to EnumTyping("PackageManager", listOf("pip", "pipenv", "poetry")),
            "architecture" to EnumTyping("Architecture", listOf("x64", "x86")),
            "cache-dependency-path" to ListOfTypings("\\n"),
        )
    ),
    WrapperRequest(
        ActionCoords("actions", "stale", "v5"),
        mapOf(
            "any-of-issue-labels" to ListOfTypings(","),
            "any-of-labels" to ListOfTypings(","),
            "any-of-pr-labels" to ListOfTypings(","),
            "ascending" to BooleanTyping,
            "days-before-close" to IntegerTyping,
            "days-before-issue-close" to IntegerWithSpecialValueTyping("Days", mapOf("Never" to -1)),
            "days-before-pr-close" to IntegerWithSpecialValueTyping("Days", mapOf("Never" to -1)),
            "days-before-pr-stale" to IntegerWithSpecialValueTyping("Days", mapOf("Never" to -1)),
            "days-before-stale" to IntegerTyping,
            "debug-only" to BooleanTyping,
            "delete-branch" to BooleanTyping,
            "enable-statistics" to BooleanTyping,
            "exempt-all-assignees" to BooleanTyping,
            "exempt-all-milestones" to BooleanTyping,
            "exempt-all-pr-assignees" to BooleanTyping,
            "exempt-assignees" to ListOfTypings(","),
            "exempt-draft-pr" to BooleanTyping,
            "exempt-issue-assignees" to ListOfTypings(","),
            "exempt-issue-labels" to ListOfTypings(","),
            "exempt-issue-milestones" to ListOfTypings(","),
            "exempt-pr-milestones" to ListOfTypings(","),
            "exempt-milestones" to ListOfTypings(","),
            "exempt-all-pr-milestones" to BooleanTyping,
            "exempt-all-issue-milestones" to BooleanTyping,
            "exempt-pr-assignees" to ListOfTypings(","),
            "exempt-pr-labels" to ListOfTypings(","),
            "ignore-issue-updates" to BooleanTyping,
            "ignore-pr-updates" to BooleanTyping,
            "ignore-updates" to BooleanTyping,
            "labels-to-add-when-unstale" to ListOfTypings(","),
            "labels-to-remove-when-unstale" to ListOfTypings(","),
            "only-issue-labels" to ListOfTypings(","),
            "only-labels" to ListOfTypings(","),
            "only-pr-labels" to ListOfTypings(","),
            "operations-per-run" to IntegerTyping,
            "remove-issue-stale-when-updated" to BooleanTyping,
            "remove-pr-stale-when-updated" to BooleanTyping,
            "remove-stale-when-updated" to BooleanTyping,
        )
    ),
    WrapperRequest(
        ActionCoords("actions", "upload-artifact", "v2", deprecatedByVersion = "v3"),
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
        ActionCoords("actions", "upload-artifact", "v3"),
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
        ActionCoords("ad-m", "github-push-action", "v0.6.0"),
        mapOf(
            "force" to BooleanTyping,
            "tags" to BooleanTyping,
        )
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
        ActionCoords("anmol098", "waka-readme-stats", "v4"),
        mapOf(
            "SHOW_OS" to BooleanTyping,
            "SHOW_PROJECTS" to BooleanTyping,
            "SHOW_EDITORS" to BooleanTyping,
            "SHOW_TIMEZONE" to BooleanTyping,
            "SHOW_COMMIT" to BooleanTyping,
            "SHOW_LANGUAGE" to BooleanTyping,
            "SHOW_LINES_OF_CODE" to BooleanTyping,
            "SHOW_LANGUAGE_PER_REPO" to BooleanTyping,
            "SHOW_LOC_CHART" to BooleanTyping,
            "SHOW_DAYS_OF_WEEK" to BooleanTyping,
            "SHOW_PROFILE_VIEWS" to BooleanTyping,
            "SHOW_SHORT_INFO" to BooleanTyping,
            "COMMIT_BY_ME" to BooleanTyping,
            "IGNORED_REPOS" to ListOfTypings(","),
        )
    ),
    WrapperRequest(
        ActionCoords("anothrNick", "github-tag-action", "v1.38.0"),
        mapOf()
    ),
    WrapperRequest(
        ActionCoords("appleboy", "scp-action", "v0.1.2"),
        mapOf(
            "port" to IntegerTyping,
            "use_insecure_cipher" to BooleanTyping,
            "rm" to BooleanTyping,
            "debug" to BooleanTyping,
            "strip_components" to IntegerTyping,
            "overwrite" to BooleanTyping,
            "proxy_port" to IntegerTyping,
            "proxy_use_insecure_cipher" to BooleanTyping,
        )
    ),
    WrapperRequest(
        ActionCoords("appleboy", "ssh-action", "v0.1.4"),
        mapOf(
            "port" to IntegerTyping,
            "sync" to BooleanTyping,
            "use_insecure_cipher" to BooleanTyping,
            "proxy_port" to IntegerTyping,
            "proxy_use_insecure_cipher" to BooleanTyping,
            "script_stop" to BooleanTyping,
            "debug" to BooleanTyping,
            "envs" to ListOfTypings(","),
        )
    ),
    WrapperRequest(
        ActionCoords("appleboy", "telegram-action", "v0.1.1"),
        mapOf(
            "debug" to BooleanTyping,
            "disable_web_page_preview" to BooleanTyping,
            "disable_notification" to BooleanTyping,
            "format" to EnumTyping(
                "Format",
                listOf("markdown", "html")
            )
        )
    ),
    WrapperRequest(
        ActionCoords("aws-actions", "amazon-ecr-login", "v1"),
        mapOf(
            "registries" to ListOfTypings(","),
            "skip-logout" to BooleanTyping,
        )
    ),
    WrapperRequest(
        ActionCoords("aws-actions", "amazon-ecs-deploy-task-definition", "v1"),
        mapOf(
            "wait-for-service-stability" to BooleanTyping,
            "wait-for-minutes" to IntegerTyping,
            "force-new-deployment" to BooleanTyping,
        )
    ),
    WrapperRequest(
        ActionCoords("aws-actions", "amazon-ecs-render-task-definition", "v1"),
        mapOf(
            "environment-variables" to ListOfTypings("\\n")
        )
    ),
    WrapperRequest(
        ActionCoords("aws-actions", "configure-aws-credentials", "v1"),
        mapOf(
            "role-duration-seconds" to IntegerTyping,
            "role-skip-session-tagging" to BooleanTyping,
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
        ActionCoords("bahmutov", "npm-install", "v1"),
        mapOf(
            "useLockFile" to BooleanTyping,
            "useRollingCache" to BooleanTyping,
        )
    ),
    WrapperRequest(
        ActionCoords("Borales", "actions-yarn", "v2.3.0", deprecatedByVersion = "v3.0.0"),
    ),
    WrapperRequest(
        ActionCoords("Borales", "actions-yarn", "v3.0.0"),
    ),
    WrapperRequest(
        ActionCoords("cachix", "install-nix-action", "v17"),
        mapOf(
            "install_options" to ListOfTypings("\\n")
        )
    ),
    WrapperRequest(
        ActionCoords("calibreapp", "image-actions", "v1.1.0"),
        mapOf(
            "jpegQuality" to IntegerTyping,
            "pngQuality" to IntegerTyping,
            "webpQuality" to IntegerTyping,
            "ignorePaths" to StringTyping,
            "ignorePaths" to ListOfTypings("\\n"),
        )
    ),
    WrapperRequest(
        ActionCoords("codecov", "codecov-action", "v3"),
        mapOf(
            "files" to ListOfTypings(","),
            "flags" to ListOfTypings(","),
            "dry_run" to BooleanTyping,
            "env_vars" to ListOfTypings(","),
            "fail_ci_if_error" to BooleanTyping,
            "functionalities" to ListOfTypings(","),
            "gcov" to BooleanTyping,
            "gcov_args" to ListOfTypings(" "),
            "gcov_ignore" to ListOfTypings("\\n"),
            "gcov_include" to ListOfTypings("\\n"),
            "move_coverage_to_trash" to BooleanTyping,
            "os" to EnumTyping("OperatingSystem", listOf("alpine", "linux", "macos", "windows"), listOf("Alpine", "Linux", "MacOs", "Windows")),
            "override_build" to IntegerTyping,
            "override_pr" to IntegerTyping,
            "verbose" to BooleanTyping,
            "xcode" to BooleanTyping,
        )
    ),
    WrapperRequest(
        ActionCoords("coverallsapp", "github-action", "1.1.3"),
        mapOf(
            "parallel" to BooleanTyping,
            "parallel-finished" to BooleanTyping,
        ),
    ),
    WrapperRequest(
        ActionCoords("docker", "build-push-action", "v2", deprecatedByVersion = "v3"),
        mapOf(
            "add-hosts" to ListOfTypings("\\n"),
            "allow" to ListOfTypings("\\n"),
            "build-args" to ListOfTypings("\\n"),
            "build-contexts" to ListOfTypings("\\n"),
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
        ActionCoords("docker", "build-push-action", "v3"),
        mapOf(
            "add-hosts" to ListOfTypings("\\n"),
            "allow" to ListOfTypings("\\n"),
            "build-args" to ListOfTypings("\\n"),
            "build-contexts" to ListOfTypings("\\n"),
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
        ActionCoords("docker", "login-action", "v1", deprecatedByVersion = "v2"),
        mapOf(
            "ecr" to BooleanTyping,
            "logout" to BooleanTyping,
        )
    ),
    WrapperRequest(
        ActionCoords("docker", "login-action", "v2"),
        mapOf(
            "ecr" to BooleanTyping,
            "logout" to BooleanTyping,
        )
    ),
    WrapperRequest(
        ActionCoords("docker", "setup-buildx-action", "v1", deprecatedByVersion = "v2"),
        mapOf(
            "driver-opts" to ListOfTypings("\\n"),
            "install" to BooleanTyping,
            "use" to BooleanTyping,
        )
    ),
    WrapperRequest(
        ActionCoords("docker", "setup-buildx-action", "v2"),
        mapOf(
            "driver-opts" to ListOfTypings("\\n"),
            "install" to BooleanTyping,
            "use" to BooleanTyping,
        )
    ),
    WrapperRequest(
        ActionCoords("elgohr", "Publish-Docker-Github-Action", "v4"),
        mapOf(
            "buildargs" to ListOfTypings(","),
            "cache" to BooleanTyping,
            "no_push" to BooleanTyping,
            "tags" to ListOfTypings(","),
            "tag_names" to BooleanTyping,
            "tag_semver" to BooleanTyping,
            "snapshot" to BooleanTyping,
        )
    ),
    WrapperRequest(
        ActionCoords("EndBug", "add-and-commit", "v8", deprecatedByVersion = "v9"),
        mapOf(
            "default_author" to EnumTyping("DefaultActor", listOf("github_actor", "user_info", "github_actions")),
            "pathspec_error_handling" to EnumTyping("PathSpecErrorHandling", listOf("ignore", "exitImmediately", "exitAtEnd")),
            "push" to StringTyping,
        )
    ),
    WrapperRequest(
        ActionCoords("EndBug", "add-and-commit", "v9"),
        mapOf(
            "default_author" to EnumTyping("DefaultActor", listOf("github_actor", "user_info", "github_actions")),
            "pathspec_error_handling" to EnumTyping("PathSpecErrorHandling", listOf("ignore", "exitImmediately", "exitAtEnd")),
            "push" to StringTyping,
        )
    ),
    WrapperRequest(
        ActionCoords("gautamkrishnar", "blog-post-workflow", "1.7.2"),
        mapOf(
            "custom_tags" to ListOfTypings(","),
            "disable_html_encoding" to BooleanTyping,
            "disable_item_validation" to BooleanTyping,
            "disable_sort" to BooleanTyping,
            "enable_keepalive" to BooleanTyping,
            "feed_list" to ListOfTypings(","),
            "feed_names" to ListOfTypings(","),
            "filter_comments" to ListOfTypings(","),
            "max_post_count" to IntegerTyping,
            "output_only" to BooleanTyping,
            "readme_path" to ListOfTypings(","),
            "retry_count" to IntegerTyping,
            "retry_wait_time" to IntegerTyping,
            "tag_post_pre_newline" to BooleanTyping,
            "title_max_length" to IntegerTyping,
        ),
    ),
    WrapperRequest(
        ActionCoords("google-github-actions", "auth", "v0"),
        mapOf(
            "cleanup_credentials" to BooleanTyping,
            "delegates" to ListOfTypings(","),
            "token_format" to EnumTyping("TokenFormat", listOf("access_token", "id_token")),
            "create_credentials_file" to BooleanTyping,
            "access_token_scopes" to ListOfTypings(","),
            "id_token_include_email" to BooleanTyping,
            "export_environment_variables" to BooleanTyping,
            "retries" to IntegerTyping,
            "backoff" to IntegerTyping,
            "backoff_limit" to IntegerTyping,
        ),
    ),
    WrapperRequest(
        ActionCoords("google-github-actions", "setup-gcloud", "v0"),
        mapOf(
            "export_default_credentials" to BooleanTyping,
            "install_components" to ListOfTypings(
                ",",
                EnumTyping(
                    "Component",
                    // To regenerate, run: gcloud components list --format=json | jq -r '"\"" + .[].id + "\","' | sort
                    listOf(
                        "alpha",
                        "anthos-auth",
                        "appctl",
                        "app-engine-go",
                        "app-engine-java",
                        "app-engine-python",
                        "app-engine-python-extras",
                        "beta",
                        "bigtable",
                        "bq",
                        "bundled-python3-unix",
                        "cbt",
                        "cloud-build-local",
                        "cloud-datastore-emulator",
                        "cloud-firestore-emulator",
                        "cloud-spanner-emulator",
                        "cloud_sql_proxy",
                        "config-connector",
                        "core",
                        "datalab",
                        "docker-credential-gcr",
                        "gsutil",
                        "kpt",
                        "kubectl",
                        "kubectl-oidc",
                        "kustomize",
                        "local-extract",
                        "minikube",
                        "nomos",
                        "pkg",
                        "pubsub-emulator",
                        "skaffold",

                    )
                )
            ),
            "cleanup_credentials" to BooleanTyping,
        ),
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
        ActionCoords("gradle-update", "update-gradle-wrapper-action", "v1"),
        mapOf(
            "reviewers" to ListOfTypings(","),
            "team-reviewers" to ListOfTypings(","),
            "labels" to ListOfTypings(","),
            "set-distribution-checksum" to BooleanTyping,
            "paths" to ListOfTypings(","),
            "paths-ignore" to ListOfTypings(","),
            "release-channel" to EnumTyping("ReleaseChannel", listOf("stable", "release-candidate")),
        ),
    ),
    WrapperRequest(
        ActionCoords("gradle", "gradle-build-action", "v2"),
        mapOf(
            "cache-disabled" to BooleanTyping,
            "cache-read-only" to BooleanTyping,
            "generate-job-summary" to BooleanTyping,
            "gradle-home-cache-includes" to ListOfTypings("\\n"),
            "gradle-home-cache-excludes" to ListOfTypings("\\n"),
            "cache-write-only" to BooleanTyping,
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
        ActionCoords("JamesIves", "github-pages-deploy-action", "v4"),
        mapOf(
            "clean" to BooleanTyping,
            "folder" to StringTyping,
            "clean-exclude" to ListOfTypings("\\n"),
            "dry-run" to BooleanTyping,
            "single-commit" to BooleanTyping,
            "silent" to BooleanTyping,
            "force" to BooleanTyping,
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
        ActionCoords("julia-actions", "setup-julia", "v1"),
        mapOf(
            "arch" to EnumTyping("Architecture", listOf("x64", "x86", "aarch64")),
            "show-versioninfo" to BooleanTyping,
            "version" to StringTyping,
        ),
    ),
    WrapperRequest(
        ActionCoords("madhead", "check-gradle-version", "v1")
    ),
    WrapperRequest(
        ActionCoords("madhead", "read-java-properties", "latest"),
        mapOf("all" to BooleanTyping)
    ),
    WrapperRequest(
        ActionCoords("madhead", "semver-utils", "v2")
    ),
    WrapperRequest(
        ActionCoords("microsoft", "setup-msbuild", "v1.1"),
        mapOf(
            "vs-prerelease" to BooleanTyping,
            "msbuild-architecture" to EnumTyping("Architecture", listOf("x86", "x64")),
        ),
    ),

    WrapperRequest(
        ActionCoords("nobrayner", "discord-webhook", "v1"),
        mapOf(
            "include-details" to BooleanTyping,
        )
    ),
    WrapperRequest(
        ActionCoords("peaceiris", "actions-gh-pages", "v3"),
        mapOf(
            "allow_empty_commit" to BooleanTyping,
            "keep_files" to BooleanTyping,
            "force_orphan" to BooleanTyping,
            "enable_jekyll" to BooleanTyping,
            "disable_nojekyll" to BooleanTyping,
            "exclude_assets" to ListOfTypings(","),
        )
    ),
    WrapperRequest(
        ActionCoords("peaceiris", "actions-hugo", "v2"),
        mapOf(
            "extended" to BooleanTyping,
        ),
    ),
    WrapperRequest(
        ActionCoords("peter-evans", "create-issue-from-file", "v4"),
        mapOf(
            "issue-number" to IntegerTyping,
            "labels" to ListOfTypings(","),
            "assignees" to ListOfTypings(","),
        )
    ),
    WrapperRequest(
        ActionCoords("peter-evans", "create-pull-request", "v4"),
        mapOf(
            "add-paths" to ListOfTypings("\\n"),
            "signoff" to BooleanTyping,
            "delete-branch" to BooleanTyping,
            "labels" to ListOfTypings("\\n"),
            "assignees" to ListOfTypings("\\n"),
            "reviewers" to ListOfTypings("\\n"),
            "team-reviewers" to ListOfTypings("\\n"),
            "draft" to BooleanTyping,
        ),
    ),
    WrapperRequest(
        ActionCoords("peterjgrainger", "action-create-branch", "v2.2.0")
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
        ActionCoords("ruby", "setup-ruby", "v1"),
        mapOf(
            "bundler-cache" to BooleanTyping,
            "working-directory" to StringTyping,
        ),
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
    ),
    WrapperRequest(
        ActionCoords("softprops", "action-gh-release", "v1"),
        mapOf(
            "draft" to BooleanTyping,
            "prerelease" to BooleanTyping,
            "files" to ListOfTypings("\\n"),
            "fail_on_unmatched_files" to BooleanTyping,
            "generate_release_notes" to BooleanTyping,
        ),
    ),
    WrapperRequest(
        ActionCoords("subosito", "flutter-action", "v2"),
        mapOf(
            "cache" to BooleanTyping,
            "channel" to EnumTyping("Channel", listOf("stable", "beta", "master", "dev", "any")),
            "architecture" to EnumTyping("Architecture", listOf("x64", "arm64")),
        ),
    ),
    WrapperRequest(
        ActionCoords("supercharge", "mongodb-github-action", "1.7.0"),
        mapOf(
            "mongodb-port" to IntegerTyping,
        )
    ),
    WrapperRequest(
        ActionCoords("w9jds", "firebase-action", "v2.2.2"),
    ),
)
