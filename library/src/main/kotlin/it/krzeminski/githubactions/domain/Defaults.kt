package it.krzeminski.githubactions.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Use defaults to create a map of default settings that will apply to all jobs in the workflow.
 *
 * You can also set default settings that are only available to a job. For more information, see jobs.<job_id>.defaults.
 *
 * See https://docs.github.com/en/actions/using-workflows/workflow-syntax-for-github-actions#defaults
 * See https://docs.github.com/en/actions/using-workflows/workflow-syntax-for-github-actions#jobsjob_iddefaultsrun
 */
@Serializable
data class Defaults(
    val run: Run,
)

/**
 * You can use defaults.run to provide default shell and working-directory options for all run steps in a workflow.
 *
 * See https://docs.github.com/en/actions/using-workflows/workflow-syntax-for-github-actions#defaultsrun
 */
@Serializable
data class Run(
    val shell: String? = null,
    @SerialName("working-directory")
    val workingDirectory: String? = null
)

fun Run.requireAtLeastOneInput() {
    require(this.shell != null || this.workingDirectory != null) {
        "Run should at least have one of shell or working-directory defined!"
    }
}
