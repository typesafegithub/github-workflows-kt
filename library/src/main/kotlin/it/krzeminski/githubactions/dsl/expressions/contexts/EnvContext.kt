package it.krzeminski.githubactions.dsl.expressions.contexts

import it.krzeminski.githubactions.dsl.expressions.ExpressionContext
import it.krzeminski.githubactions.dsl.expressions.MapFromLambda

/**
 * Environment variables: https://docs.github.com/en/actions/learn-github-actions/environment-variables#about-environment-variables
 * Default Environment variables: https://docs.github.com/en/actions/learn-github-actions/environment-variables#default-environment-variables
 */
public object EnvContext : ExpressionContext(
    _path = "env",
    propertyToExprPath = MapFromLambda { key: String -> "\$$key" },
) {
    /** Always set to true. **/
    public val CI: String by propertyToExprPath

    /** The name of the action currently running, or the id of a step.
     * For example, for an action, __repo-owner_name-of-action-repo.GitHub removes special characters,
     * and uses the name __run when the current step runs a script without an id.
     * If you use the same script or action more than once in the same job,
     * the name will include a suffix that consists of the sequence number preceded by an underscore.
     * For example, the first script you run will have the name __run, and the second script will be named __run_2.
     * Similarly, the second invocation of actions/checkout will be actionscheckout2. **/
    public val GITHUB_ACTION: String by propertyToExprPath

    /** The path where an action is located. This property is only supported in composite actions.
     * You can use this path to access files located in the same repository as the action.
     * For example, /home/runner/work/_actions/repo-owner/name-of-action-repo/v1. **/
    public val GITHUB_ACTION_PATH: String by propertyToExprPath

    /** For a step executing an action, this is the owner and repository name of the action.
     * For example, actions/checkout. **/
    public val GITHUB_ACTION_REPOSITORY: String by propertyToExprPath

    /** Always set to true when GitHub Actions is running the workflow.
     * You can use this variable to differentiate when tests are being run locally or by GitHub Actions. **/
    public val GITHUB_ACTIONS: String by propertyToExprPath

    /** The name of the person or app that initiated the workflow.
     * For example, octocat. **/
    public val GITHUB_ACTOR: String by propertyToExprPath

    /** Returns the API URL.
     * For example: https://api.github.com. **/
    public val GITHUB_API_URL: String by propertyToExprPath

    /** The name of the base ref or target branch of the pull request in a workflow run.
     * This is only set when the event that triggers a workflow run is either pull_request or pull_request_target.
     * For example, main. **/
    public val GITHUB_BASE_REF: String by propertyToExprPath

    /** The path on the runner to the file that sets environment variables from workflow commands.
     * This file is unique to the current step and changes for each step in a job.
     * For example, /home/runner/work/_temp/_runner_file_commands/set_env_87406d6e-4979-4d42-98e1-3dab1f48b13a.
     * For more information, see "Workflow commands for GitHub Actions." **/
    public val GITHUB_ENV: String by propertyToExprPath

    /** The name of the event that triggered the workflow. For example, workflow_dispatch. **/
    public val GITHUB_EVENT_NAME: String by propertyToExprPath

    /** The path to the file on the runner that contains the full event webhook payload.
     * For example, /github/workflow/event.json. **/
    public val GITHUB_EVENT_PATH: String by propertyToExprPath

    /** Returns the GraphQL API URL. For example: https://api.github.com/graphql. **/
    public val GITHUB_GRAPHQL_URL: String by propertyToExprPath

    /** The head ref or source branch of the pull request in a workflow run.
     * This property is only set when the event that triggers
     * a workflow run is either pull_request or pull_request_target.
     * For example, feature-branch-1. **/
    public val GITHUB_HEAD_REF: String by propertyToExprPath

    /** The job_id of the current job.
     * For example, greeting_job. **/
    public val GITHUB_JOB: String by propertyToExprPath

    /** The path on the runner to the file that sets system PATH variables from workflow commands.
     * This file is unique to the current step and changes for each step in a job.
     * For example, /home/runner/work/_temp/_runner_file_commands/add_path_899b9445-ad4a-400c-aa89-249f18632cf5.
     * For more information, see "Workflow commands for GitHub Actions." **/
    public val GITHUB_PATH: String by propertyToExprPath

    /** The branch or tag ref that triggered the workflow run.
     * For branches this is the format refs/heads/<branch_name>,
     * for tags it is refs/tags/<tag_name>,
     * and for pull requests it is refs/pull/<pr_number>/merge.
     * This variable is only set if a branch or tag is available for the event type.
     * For example, refs/heads/feature-branch-1. **/
    public val GITHUB_REF: String by propertyToExprPath

    /** The branch or tag name that triggered the workflow run.
     * For example, feature-branch-1. **/
    public val GITHUB_REF_NAME: String by propertyToExprPath

    /** true if branch protections are configured for the ref that triggered the workflow run. **/
    public val GITHUB_REF_PROTECTED: String by propertyToExprPath

    /** The type of ref that triggered the workflow run.
     * Valid values are branch or tag. **/
    public val GITHUB_REF_TYPE: String by propertyToExprPath

    /** The owner and repository name.
     * For example, octocat/Hello-World. **/
    public val GITHUB_REPOSITORY: String by propertyToExprPath

    /** The repository owner's name.
     * For example, octocat. **/
    public val GITHUB_REPOSITORY_OWNER: String by propertyToExprPath

    /** The number of days that workflow run logs and artifacts are kept.
     * For example, 90. **/
    public val GITHUB_RETENTION_DAYS: String by propertyToExprPath

    /** A unique number for each attempt of a particular workflow run in a repository.
     * This number begins at 1 for the workflow run's first attempt,
     * and increments with each re-run.
     * For example, 3. **/
    public val GITHUB_RUN_ATTEMPT: String by propertyToExprPath

    /** A unique number for each workflow run within a repository.
     * This number does not change if you re-run the workflow run.
     * For example, 1658821493. **/
    public val GITHUB_RUN_ID: String by propertyToExprPath

    /** A unique number for each run of a particular workflow in a repository.
     * This number begins at 1 for the workflow's first run, and increments with each new run.
     * This number does not change if you re-run the workflow run.
     * For example, 3. **/
    public val GITHUB_RUN_NUMBER: String by propertyToExprPath

    /** The URL of the GitHub server.
     * For example: https://github.com. **/
    public val GITHUB_SERVER_URL: String by propertyToExprPath

    /** The commit SHA that triggered the workflow.
     * The value of this commit SHA depends on the event that triggered the workflow.
     * For more information, see Events that trigger workflows.
     * For example, ffac537e6cbbf934b08745a378932722df287a53. **/
    public val GITHUB_SHA: String by propertyToExprPath

    /** The name of the workflow. For example, My test workflow.
     * If the workflow file doesn't specify a name, the value of this variable is the full path
     * of the workflow file in the repository. **/
    public val GITHUB_WORKFLOW: String by propertyToExprPath

    /** The default working directory on the runner for steps,
     * and the default location of your repository when using the checkout action.
     * For example, /home/runner/work/my-repo-name/my-repo-name. **/
    public val GITHUB_WORKSPACE: String by propertyToExprPath

    /** The architecture of the runner executing the job.
     * Possible values are X86, X64, ARM, or ARM64. **/
    public val RUNNER_ARCH: String by propertyToExprPath

    /** The name of the runner executing the job.
     * For example, Hosted Agent **/
    public val RUNNER_NAME: String by propertyToExprPath

    /** The operating system of the runner executing the job.
     * Possible values are Linux, Windows, or macOS.
     * For example, Windows **/
    public val RUNNER_OS: String by propertyToExprPath

    /** The path to a temporary directory on the runner.
     * This directory is emptied at the beginning and end of each job.
     * Note that files will not be removed if the runner's user account does not have permission to delete them.
     * For example, D:\a\_temp **/
    public val RUNNER_TEMP: String by propertyToExprPath

    /** The path to the directory containing preinstalled tools for GitHub-hosted runners.
     * For more information, see "About GitHub-hosted runners".
     * For example, C:\hostedtoolcache\windows **/
    public val RUNNER_TOOL_CACHE: String by propertyToExprPath
}
