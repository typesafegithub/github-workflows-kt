This file describes various maintenance tasks, relevant for project maintainers only.

# Release a new version

It currently happens every two weeks, around Friday. We don't need to stick to this cadence strictly, the goal is to publish small frequent updates.

1. Remove `-SNAPSHOT` for version starting from [/library/build.gradle.kts](https://github.com/typesafegithub/github-workflows-kt/blob/main/library/build.gradle.kts). By building the whole project with `./gradlew build`, you will learn what other places need to be adjusted. Once done, create a commit using this pattern for commit message: `chore: prepare for releasing version <version>`.
1. Once CI is green for the newly merged commits, create and push an annotated tag named `v<version>`, e. g. `v0.21.0`: `git tag -a v0.21.0 -m "chore: release version 0.21.0" && git push origin v0.21.0`. The tag should point to the commit that removes `-SNAPSHOT` from the version.
1. On `main` branch, change version to prepare for the next development cycle, e.g. if it was `0.20.1-SNAPSHOT` before and we released it as `0.21.0`, change the version to `0.21.1-SNAPSHOT`.
1. Ensure that the release job has succeeded.
