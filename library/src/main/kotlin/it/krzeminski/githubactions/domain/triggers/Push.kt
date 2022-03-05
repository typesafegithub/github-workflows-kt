package it.krzeminski.githubactions.domain.triggers

@kotlinx.serialization.Serializable
data class Push(
    val branches: List<String>? = null,
    val branchesIgnore: List<String>? = null,
    val tags: List<String>? = null,
    val tagsIgnore: List<String>? = null,
    val paths: List<String>? = null,
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
