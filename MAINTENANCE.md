This file describes various maintenance tasks, relevant for project maintainers only.

# Release a new version

It currently happens whenever necessary, there's no agreed cadence. Whenever we see there's an important bug fix or a feature to roll out, or an important dependency update, we release.

1. Remove `-SNAPSHOT` for version starting from [build.gradle.kts](https://github.com/typesafegithub/github-workflows-kt/blob/main/build.gradle.kts). By building the whole project with `./gradlew build`, you will learn what other places need to be adjusted. There's one place that needs extra care: in PomBuilding.kt, there's `LATEST_RELASED_LIBRARY_VERSION` - set it to the version you're going to deploy in a minute. Once done, create a commit using this pattern for commit message: `chore: prepare for releasing version <version>`.
1. Once CI is green for the newly merged commits, create and push an annotated tag:
   ```
   COMMIT_TITLE=`git log -1 --pretty=%B`
   VERSION=${COMMIT_TITLE#"chore: prepare for releasing version "}
   git tag -a "v$VERSION" -m "chore: release version $VERSION" && git push origin "v$VERSION"
   ```
1. On `main` branch, change version to prepare for the next development cycle, e.g. if it was `1.2.3-SNAPSHOT` before and we released it as `1.2.3`, change the version to `1.2.4-SNAPSHOT` (a minimal increase, in the patch version number).
1. Ensure that the release job has succeeded.
