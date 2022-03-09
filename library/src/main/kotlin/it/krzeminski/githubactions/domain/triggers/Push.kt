package it.krzeminski.githubactions.domain.triggers

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class Push(
    val branches: List<String>? = null,
    @SerialName("branches-ignore")
    val branchesIgnore: List<String>? = null,
    val tags: List<String>? = null,
    @SerialName("tags-ignore")
    val tagsIgnore: List<String>? = null,
    val paths: List<String>? = null,
    @SerialName("paths-ignore")
    val pathsIgnore: List<String>? = null,
) : Trigger() {

    init {
        require(!(branches != null && branchesIgnore != null)) {
            "Cannot define both 'branches' and 'branchesIgnore'!"
        }
        require(!(tags != null && tagsIgnore != null)) {
            "Cannot define both 'tags' and 'tagsIgnore'!"
        }
        require(!(paths != null && pathsIgnore != null)) {
            "Cannot define both 'paths' and 'pathsIgnore'!"
        }
    }
}
