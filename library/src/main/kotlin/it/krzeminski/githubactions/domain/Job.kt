package it.krzeminski.githubactions.domain

data class Job(
    val name: String,
    val runsOn: RunnerType,
    val steps: List<Step>,
    val needs: List<Job> = emptyList(),
    val env: LinkedHashMap<String, String> = linkedMapOf(),
    val condition: String? = null,
    val strategyMatrix: Map<String, List<String>>? = null,
) {

    // Only chars from [jobNameRegex] are allowed https://docs.github.com/en/actions/using-jobs/using-jobs-in-a-workflow#setting-an-id-for-a-job
    val escapedName = name.trim().split(jobNameRegex)
        .joinToString(separator = "-")
        .replace(prefixRegex, "")
}

private val jobNameRegex = Regex("[^a-zA-Z0-9_-]+")
private val prefixRegex = Regex("^[0-9-]+")
