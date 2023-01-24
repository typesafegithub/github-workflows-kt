# Contributing guidelines

Thanks for finding a moment for your contribution! We really appreciate it!
The below rules are here to speed things up and streamline work on both sides - us as library maintainers and you as the contributor.

## Reporting a bug

First check if it's not already reported. Use the "bug" tag for filtering.
If it's not, create a new one using the template. If it already exists, give it a thumb-up and optionally share any helpful details in the comments.

## Requesting a feature

There's also a designated template for it, and the rule of checking if the request is already tracked also applies.

## Requesting/adding support for an action

Apart from filing an issue using the template, we strongly encourage you to:
* ask the owner to provide the typings using https://github.com/krzema12/github-actions-typing - it greatly limits the amount of work needed in this DSL library, both when adding and maintaining support for actions. For inspiration how such request could look, see https://github.com/burrunan/gradle-cache-action/issues/58
* create a PR that adds support for the action. Start by adding an appropriate file to the [actions](actions) directory
  * the layout is as follows: `actions/<owner>/<name>/<version>`
  * each such directory contains a `commit-hash.txt` file pointing to the commit in the action's repo from which the metadata should be fetched
  * in each directory, there's either a `action-types.yml` file that contains typings compatible with https://github.com/krzema12/github-actions-typing, or an empty `typings-hosted-by-action` file denoting that the action itself hosts `action-types.yml`
  * run `./gradlew :automation:wrapper-generator:run` to (re)generate various files
  * check if everything builds fine using `./gradlew build` and create the commit and the PR

## Contributing a feature

Each feature contribution must have an accompanying feature request issue, best if we discuss the approach in the issue first to avoid misunderstandings and wasted time. If the feature is simple, go ahead with creating the issue and the PR in parallel.

If you're creating a new feature branch in this repository (doesn't count if it's your fork), it has to start with issue number, e.g. `123-new-steps-API`, and the issue has to be open. Otherwise, the branch is assumed to be safe to delete.
