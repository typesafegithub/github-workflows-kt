# Contributing guidelines

Thanks for finding a moment for your contribution! We really appreciate it!
The below rules are here to speed things up and streamline work on both sides - us as library maintainers and you as the contributor.

## Reporting a bug

First check if it's not already reported. Use the "bug" tag for filtering.
If it's not, create a new one using the template. If it already exists, give it a thumb-up and optionally share any helpful details in the comments.

## Requesting a feature

There's also a designated template for it, and the rule of checking if the request is already tracked also applies.

## Requesting/adding support for an action

To request it, it's enough to fill in the issue using the corresponding template.

However, it's also pretty easy to add support for it yourself! In case of troubles, you can always ask for help on
[this Slack channel](https://kotlinlang.slack.com/archives/C02UUATR7RC).

1. First, the typings. In order for the action bindings to not have [stringly-typed](https://wiki.c2.com/?StringlyTyped)
   API, we need to learn actual types of its inputs and outputs. Proceed this way:
   1. Check if the action you want to add contains a file called `action-types.yml`.
   2. If yes, you're lucky! The build logic here will be able to fetch these typings straight from the action.
   3. If no, ask the owner politely to provide the typings using https://github.com/typesafegithub/github-actions-typing
   4. If the owner refuses or doesn't respond, add the typings yourself here: https://github.com/typesafegithub/github-actions-typing-catalog
2. Create a PR in this repo that adds support for the action:
  * create a new directory under `actions/<owner>/<name>/<version>`. If the action contains `action.yml` not in the root
    directory (rare), go with `actions/<owner>/<name>/<version>/<path-to-action-yml>`
  * create a first file: `commit-hash.txt`. It contains a full commit hash of the commit in the action's repo from which
    the metadata should be fetched. It improves reproducibility of builds in this repository. Check the hash by
    following the branch/tag corresponding to `<version>`
  * create a second file: `action`. It's just a marker file so that the code generator knows that it should generate a
    binding for an action with coordinates corresponding to this directory's path
  * run `./gradlew :automation:code-generator:run` to (re)generate various files
  * check if everything builds fine using `./gradlew build` and create the commit and the PR

## Contributing a feature

Each feature contribution must have an accompanying feature request issue, best if we discuss the approach in the issue first to avoid misunderstandings and wasted time. If the feature is simple, go ahead with creating the issue and the PR in parallel.

If you're creating a new feature branch in this repository (doesn't count if it's your fork), it has to start with issue number, e.g. `123-new-steps-API`, and the issue has to be open. Otherwise, the branch is assumed to be safe to delete.
