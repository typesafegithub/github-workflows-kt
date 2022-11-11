This file describes various maintenance tasks, relevant for project maintainers only.

# Release a new version

It currently happens every two weeks, around Friday. We don't need to stick to this cadence strictly, the goal is to publish small frequent updates.

1. Run `GITHUB_TOKEN=<token> ./gradlew updateCommitHashes` to lock actions on newest available commits for each tag or branch. The token can be generated on https://github.com/settings/tokens. It needs to have public_repo scope.
1. Run `./gradlew :wrapper-generator:run` to regenerate files.
1. The previous commands will likely make some changes. If they do, review if some action inputs require typing adjustment. Omit actions where only commit hashes changed and no changes were made to the generated Kotlin files. Once done, create commits, a separate one for each action, using this patten for commit message: `feat(actions): update owner/name@version`.
1. Bump version starting from [/library/build.gradle.kts](https://github.com/krzema12/github-workflows-kt/blob/main/library/build.gradle.kts). By building the whole project with `./gradlew build`, you will learn what other places need to be adjusted. Once done, create a commit using this pattern for commit message: `chore: bump version to <version>`.
1. Create a PR with the above commits and let the CI checks run. Example PR: https://github.com/krzema12/github-workflows-kt/pull/551. Once green, merge by **rebasing, not squashing!**
1. Once CI is green for the newly merged commits, create and push a tag named `v<version>`, e. g. `v0.21.0`: `git tag v0.21.0 && git push origin v0.21.0`.
1. Ensure that the release job has succeeded.
