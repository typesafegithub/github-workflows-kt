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
            "push" to StringTyping, // don't suggest a boolean, it's not
            "default_author" to EnumTyping("DefaultActor", listOf("github_actor", "user_info", "github_actions")),
            "pathspec_error_handling" to EnumTyping("PathSpecErrorHandling",
                listOf("ignore", "exitImmediately", "exitAtEnd")),
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
            "cache-write-only" to BooleanTyping,
            "gradle-home-cache-strict-match" to BooleanTyping,
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
            "docker_build_args" to ListOfStringsTyping(" "),
        )
    ),

    WrapperRequest(ActionCoords("10up", "action-wordpress-plugin-deploy", "v2.0.0"),
        mapOf()
    ),
    WrapperRequest(
        ActionCoords("8398a7", "action-slack", "v3"),
        mapOf()
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
        )
    ),
    WrapperRequest(
        ActionCoords("Azure", "webapps-deploy", "v2"),
        mapOf()
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
        )
    ),
    WrapperRequest(
        ActionCoords("JasonEtco", "create-an-issue", "v2"),
        mapOf(
            "assignees" to ListOfStringsTyping(","),
            "search_existing" to ListOfStringsTyping(","),
        )
    ),
    WrapperRequest(ActionCoords("SamKirkland", "FTP-Deploy-Action", "v4.3.0"),
        mapOf()
    ),
    WrapperRequest(
        ActionCoords("actions", "cache", "v2"),
        mapOf(
            "path" to ListOfStringsTyping("\\n"),
            "restore-keys" to ListOfStringsTyping("\\n"),
        )
    ),
    WrapperRequest(
        ActionCoords("actions", "create-release", "v1"),
        mapOf(
            "draft" to BooleanTyping,
            "prerelease" to BooleanTyping,
        )
    ),
    WrapperRequest(
        ActionCoords("actions", "first-interaction", "v1"),
        mapOf()
    ),
    WrapperRequest(
        ActionCoords("actions", "github-script", "v6"),
        mapOf(
            "debug" to BooleanTyping,
            "previews" to ListOfStringsTyping(","),
        )
    ),
    WrapperRequest(
        ActionCoords("actions", "labeler", "v3"),
        mapOf(
            "sync-labels" to BooleanTyping,
        )
    ),
    WrapperRequest(
        ActionCoords("actions", "setup-dotnet", "v1"),
        mapOf()
    ),
    WrapperRequest(
        ActionCoords("actions", "setup-go", "v2"),
        mapOf(
            "check-latest" to BooleanTyping,
            "stable" to BooleanTyping,
        )
    ),
    WrapperRequest(
        ActionCoords("actions", "stale", "v4"),
        mapOf(
            "exempt-all-milestones" to BooleanTyping,
            "remove-stale-when-updated" to BooleanTyping,
            "debug-only" to BooleanTyping,
            "ascending" to BooleanTyping,
            "delete-branch" to BooleanTyping,
            "exempt-all-assignees" to BooleanTyping,
            "exempt-draft-pr" to BooleanTyping,
            "enable-statistics" to BooleanTyping,
            "ignore-updates" to BooleanTyping,
            "days-before-stale" to IntegerTyping,
            "days-before-close" to IntegerTyping,
            "operations-per-run" to IntegerTyping,
            "only-labels" to ListOfStringsTyping(","),
            "any-of-labels" to ListOfStringsTyping(","),
            "any-of-issue-labels" to ListOfStringsTyping(","),
            "any-of-pr-labels" to ListOfStringsTyping(","),
            "only-issue-labels" to ListOfStringsTyping(","),
            "only-pr-labels" to ListOfStringsTyping(","),
            "labels-to-add-when-unstale" to ListOfStringsTyping(","),
            "labels-to-remove-when-unstale" to ListOfStringsTyping(","),
        )
    ),
    WrapperRequest(
        ActionCoords("actions-rs", "audit-check", "v1"),
        mapOf()
    ),
    WrapperRequest(
        ActionCoords("actions-rs", "cargo", "v1"),
        mapOf(
            "use-cross" to BooleanTyping,
        )
    ),
    WrapperRequest(
        ActionCoords("actions-rs", "clippy-check", "v1"),
        mapOf(
            "use-cross" to BooleanTyping,
        )
    ),
    WrapperRequest(
        ActionCoords("actions-rs", "toolchain", "v1"),
        mapOf(
            "default" to BooleanTyping,
            "override" to BooleanTyping,
            "components" to ListOfStringsTyping(","),
        )
    ),
    WrapperRequest(
        ActionCoords("ad-m", "github-push-action", "v0.6.0"),
        mapOf()
    ),
    WrapperRequest(
        ActionCoords("anmol098", "waka-readme-stats", "v4"),
        mapOf(
            "SHOW_OS" to StringTyping,
            "SHOW_PROJECTS" to StringTyping,
        )
    ),
    WrapperRequest(ActionCoords("anothrNick", "github-tag-action", "v1.36.0"),
        mapOf()
    ),
    WrapperRequest(ActionCoords("appleboy", "scp-action", "v0.1.2"),
        mapOf()
    ),
    WrapperRequest(ActionCoords("appleboy", "ssh-action", "v0.1.4"),
        mapOf()
    ),
    WrapperRequest(ActionCoords("appleboy", "telegram-action", "v0.1.1"),
        mapOf()
    ),
    WrapperRequest(ActionCoords("aws-actions", "amazon-ecr-login", "v1"),
        mapOf()
    ),
    WrapperRequest(
        ActionCoords("aws-actions", "amazon-ecs-deploy-task-definition", "v1"),
        mapOf()
    ),
    WrapperRequest(
        ActionCoords("aws-actions", "amazon-ecs-render-task-definition", "v1"),
        mapOf()
    ),
    WrapperRequest(
        ActionCoords("aws-actions", "configure-aws-credentials", "v1"),
        mapOf()
    ),
    WrapperRequest(
        ActionCoords("bahmutov", "npm-install", "v1"),
        mapOf()
    ),
    WrapperRequest(
        ActionCoords("cachix", "install-nix-action", "v16"),
        mapOf()
    ),
    WrapperRequest(ActionCoords("calibreapp", "image-actions", "v1.1.0"),
        mapOf()
    ),
    WrapperRequest(
        ActionCoords("codecov", "codecov-action", "v2"),
        mapOf(
            "files" to ListOfStringsTyping(","),
            "functionalities" to ListOfStringsTyping(","),
        )
    ),
    WrapperRequest(ActionCoords("coverallsapp", "github-action", "v1.1.3"),
        mapOf()
    ),
    WrapperRequest(
        ActionCoords("crazy-max", "ghaction-github-pages", "v2"),
        mapOf(
            "keep_history" to BooleanTyping,
            "allow_empty_commit" to BooleanTyping,
            "absolute_build_dir" to BooleanTyping,
            "follow_symlinks" to BooleanTyping,
            "jekyll" to BooleanTyping,
            "dry_run" to BooleanTyping,
            "verbose" to BooleanTyping,
        )
    ),
    WrapperRequest(
        ActionCoords("cycjimmy", "semantic-release-action", "v2"),
        mapOf()
    ),
    WrapperRequest(
        ActionCoords("cypress-io", "github-action", "v3"),
        mapOf(
            "record" to BooleanTyping,
        )
    ),
    WrapperRequest(
        ActionCoords("digitalocean", "action-doctl", "v2"),
        mapOf()
    ),
    WrapperRequest(
        ActionCoords("docker", "build-push-action", "v2"),
        mapOf(
            "load" to BooleanTyping,
            "no-cache" to BooleanTyping,
            "pull" to BooleanTyping,
            "push" to BooleanTyping,
            "add-hosts" to ListOfStringsTyping(","),
            "allow" to ListOfStringsTyping(","),
            "build-args" to ListOfStringsTyping(","),
            "cache-from" to ListOfStringsTyping(","),
            "cache-to" to ListOfStringsTyping(","),
            "labels" to ListOfStringsTyping(","),
            "outputs" to ListOfStringsTyping(","),
            "platforms" to ListOfStringsTyping(","),
            "secrets" to ListOfStringsTyping(","),
            "secret-files" to ListOfStringsTyping(","),
            "ssh" to ListOfStringsTyping(","),
            "tags" to ListOfStringsTyping(","),
        )
    ),
    WrapperRequest(
        ActionCoords("easingthemes", "ssh-deploy", "v2"),
        mapOf(
            "REMOTE_PORT" to IntegerTyping,
        )
    ),
    WrapperRequest(ActionCoords("elgohr", "Publish-Docker-Github-Action", "v3.04"),
        mapOf()
    ),
    WrapperRequest(
        ActionCoords("enriikke", "gatsby-gh-pages-action", "v2"),
        mapOf(
            "skip-publish" to BooleanTyping,
        )
    ),
    WrapperRequest(ActionCoords("gautamkrishnar", "blog-post-workflow", "v1.6.9"),
        mapOf()
    ),
    WrapperRequest(
        ActionCoords("github", "super-linter", "v4"),
        mapOf()
    ),
    WrapperRequest(
        ActionCoords("golangci", "golangci-lint-action", "v2"),
        mapOf(
            "only-new-issues" to BooleanTyping,
            "skip-go-installation" to BooleanTyping,
            "skip-pkg-cache" to BooleanTyping,
            "skip-build-cache" to BooleanTyping,
        )
    ),
    WrapperRequest(
        ActionCoords("hashicorp", "setup-terraform", "v1"),
        mapOf(
            "terraform_wrapper" to BooleanTyping,
        )
    ),
    WrapperRequest(
        ActionCoords("helaili", "jekyll-action", "v2"),
        mapOf()
    ),
    WrapperRequest(
        ActionCoords("hmarr", "auto-approve-action", "v2"),
        mapOf()
    ),
    WrapperRequest(ActionCoords("homoluctus", "slatify", "v3.0.0"),
        mapOf()
    ),
    WrapperRequest(ActionCoords("jakejarvis", "s3-sync-action", "v0.5.1"),
        mapOf()
    ),
    WrapperRequest(
        ActionCoords("julia-actions", "setup-julia", "v1"),
        mapOf(
            "show-versioninfo" to BooleanTyping,
            "version" to IntegerTyping,
        )
    ),
    WrapperRequest(
        ActionCoords("jurplel", "install-qt-action", "v2"),
        mapOf(
            "install-deps" to BooleanTyping,
            "cached" to BooleanTyping,
            "setup-python" to BooleanTyping,
            "set-env" to BooleanTyping,
            "tools-only" to BooleanTyping,
            "tools" to ListOfStringsTyping(","),
        )
    ),
    WrapperRequest(ActionCoords("maxheld83", "ghpages", "v0.3.0"),
        mapOf()
    ),
    WrapperRequest(
        ActionCoords("microsoft", "setup-msbuild", "v1"),
        mapOf()
    ),
    WrapperRequest(ActionCoords("mikeal", "merge-release", "v4.3.0"),
        mapOf()
    ),
    WrapperRequest(
        ActionCoords("mxschmitt", "action-tmate", "v3"),
        mapOf(
            "sudo" to BooleanTyping,
            "install-dependencies" to BooleanTyping,
            "limit-access-to-actor" to BooleanTyping,
        )
    ),
    WrapperRequest(
        ActionCoords("ncipollo", "release-action", "v1"),
        mapOf(
            "generateReleaseNotes" to BooleanTyping,
            "omitBody" to BooleanTyping,
            "omitBodyDuringUpdate" to BooleanTyping,
            "omitName" to BooleanTyping,
            "omitNameDuringUpdate" to BooleanTyping,
            "omitPrereleaseDuringUpdate" to BooleanTyping,
            "removeArtifacts" to BooleanTyping,
            "replacesArtifacts" to BooleanTyping,
            "artifacts" to ListOfStringsTyping(","),
            "artifact" to ListOfStringsTyping(","),
        )
    ),
    WrapperRequest(
        ActionCoords("nwtgck", "actions-netlify", "v1"),
        mapOf()
    ),
    WrapperRequest(
        ActionCoords("olafurpg", "setup-scala", "v13"),
        mapOf()
    ),
    WrapperRequest(ActionCoords("pascalgn", "automerge-action", "v0.14.3"),
        mapOf()
    ),
    WrapperRequest(ActionCoords("pascalgn", "npm-publish-action", "v1.3.8"),
        mapOf()
    ),
    WrapperRequest(
        ActionCoords("peaceiris", "actions-gh-pages", "v3"),
        mapOf(
            "allow_empty_commit" to BooleanTyping,
            "keep_files" to BooleanTyping,
            "force_orphan" to BooleanTyping,
            "enable_jekyll" to BooleanTyping,
            "disable_nojekyll" to BooleanTyping,
        )
    ),
    WrapperRequest(
        ActionCoords("peaceiris", "actions-hugo", "v2"),
        mapOf(
            "extended" to BooleanTyping,
        )
    ),
    WrapperRequest(
        ActionCoords("peter-evans", "create-pull-request", "v3"),
        mapOf(
            "signoff" to BooleanTyping,
            "delete-branch" to BooleanTyping,
            "draft" to BooleanTyping,
            "add-paths" to ListOfStringsTyping(","),
            "labels" to ListOfStringsTyping(","),
            "assignees" to ListOfStringsTyping(","),
            "reviewers" to ListOfStringsTyping(","),
            "team-reviewers" to ListOfStringsTyping(","),
        )
    ),
    WrapperRequest(
        ActionCoords("peter-evans", "dockerhub-description", "v2"),
        mapOf()
    ),
    WrapperRequest(
        ActionCoords("peter-evans", "repository-dispatch", "v1"),
        mapOf()
    ),
    WrapperRequest(ActionCoords("pre-commit", "action", "v2.0.3"),
        mapOf()
    ),
    WrapperRequest(
        ActionCoords("preactjs", "compressed-size-action", "v2"),
        mapOf(
            "show-total" to BooleanTyping,
            "collapse-unchanged" to BooleanTyping,
            "minimum-change-threshold" to IntegerTyping,
        )
    ),
    WrapperRequest(ActionCoords("pypa", "gh-action-pypi-publish", "v1.5.0"),
        mapOf()
    ),
    WrapperRequest(
        ActionCoords("release-drafter", "release-drafter", "v5"),
        mapOf()
    ),
    WrapperRequest(
        ActionCoords("rtCamp", "action-slack-notify", "v2"),
        mapOf()
    ),
    WrapperRequest(
        ActionCoords("ruby", "setup-ruby", "v1"),
        mapOf(
            "bundler-cache" to BooleanTyping,
            "cache-version" to IntegerTyping,
            "working-directory" to StringTyping,
        )
    ),
    WrapperRequest(
        ActionCoords("samuelmeuli", "action-electron-builder", "v1"),
        mapOf(
            "release" to BooleanTyping,
            "skip_build" to BooleanTyping,
            "use_vue_cli" to BooleanTyping,
            "max_attempts" to IntegerTyping,
        )
    ),
    WrapperRequest(
        ActionCoords("shimataro", "ssh-key-action", "v2"),
        mapOf()
    ),
    WrapperRequest(ActionCoords("softprops", "action-gh-release", "v0.1.14"),
        mapOf()
    ),
    WrapperRequest(ActionCoords("styfle", "cancel-workflow-action", "v0.9.1"),
        mapOf()
    ),
    WrapperRequest(
        ActionCoords("subosito", "flutter-action", "v2"),
        mapOf()
    ),
    WrapperRequest(
        ActionCoords("svenstaro", "upload-release-action", "v2"),
        mapOf()
    ),
    WrapperRequest(
        ActionCoords("treosh", "lighthouse-ci-action", "v9"),
        mapOf(
            "urls" to ListOfStringsTyping("\\n"),
        )
    ),
    WrapperRequest(
        ActionCoords("w9jds", "firebase-action", "v2.0.0"),
        mapOf()
    ),
    WrapperRequest(
        ActionCoords("wagoid", "commitlint-github-action", "v2"),
        mapOf(
            "firstParent" to BooleanTyping,
            "failOnWarnings" to BooleanTyping,
        )
    ),
    WrapperRequest(
        ActionCoords("xu-cheng", "latex-action", "v2"),
        mapOf()
    ),

    )
