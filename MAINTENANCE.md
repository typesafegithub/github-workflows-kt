This file describes various maintenance tasks, relevant for project maintainers only.

# Release a new version

It currently happens monthly, around the beginning of each month. We don't need to stick to this cadence strictly, the goal is to publish small frequent updates.

1. Remove `-SNAPSHOT` for version starting from [/github-workflows-kt/build.gradle.kts](https://github.com/typesafegithub/github-workflows-kt/blob/main/github-workflows-kt/build.gradle.kts). By building the whole project with `./gradlew build`, you will learn what other places need to be adjusted. Once done, create a commit using this pattern for commit message: `chore: prepare for releasing version <version>`.
1. Once CI is green for the newly merged commits, create and push an annotated tag:
   ```
   COMMIT_TITLE=`git log -1 --pretty=%B`
   VERSION=${COMMIT_TITLE#"chore: prepare for releasing version "}
   git tag -a "v$VERSION" -m "chore: release version $VERSION" && git push origin "v$VERSION"
   ```
1. On `main` branch, change version to prepare for the next development cycle, e.g. if it was `1.2.3-SNAPSHOT` before and we released it as `1.2.3`, change the version to `1.2.3-SNAPSHOT`.
1. Ensure that the release job has succeeded.
