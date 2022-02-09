fun main() {
    listOf(
        ActionCoords("actions", "checkout", "v2"),
        ActionCoords("actions", "download-artifact", "v2"),
        ActionCoords("actions", "setup-java", "v2"),
        ActionCoords("actions", "upload-artifact", "v2"),

        ActionCoords("EndBug", "add-and-commit", "v8"),

        ActionCoords("gradle", "gradle-build-action", "v2"),
        ActionCoords("gradle", "wrapper-validation-action", "v1"),

        ActionCoords("gradle-update", "update-gradle-wrapper-action", "v1"),

        ActionCoords("madhead", "check-gradle-version", "v1"),
        ActionCoords("madhead", "read-java-properties", "latest"),
        ActionCoords("madhead", "semver-utils", "latest"),

        ActionCoords("peterjgrainger", "action-create-branch", "v2.1.0"),

        ActionCoords("repo-sync", "pull-request", "v2")
    ).forEach {
        println(it.fetchManifest())
    }
}
