package it.krzeminski.githubactions.dsl.expressions.contexts

import it.krzeminski.githubactions.domain.Job
import it.krzeminski.githubactions.dsl.expressions.ExpressionContext
import it.krzeminski.githubactions.dsl.expressions.MapFromLambda
import it.krzeminski.githubactions.dsl.expressions.expr

class JobOutputContext(val job: Job) : ExpressionContext(
    "needs.${job.id}.outputs",
    propertyToExprPath = MapFromLambda { propertyName ->
        require(job.outputsMapping.containsKey(propertyName)) {
            "'$propertyName' must be in output mapping of job ${job.id}"
        }
        "needs.${job.id}.outputs.$propertyName"
    }
) {
    override operator fun get(key: String): String {
        return "needs.${job.id}.outputs.$key"
    }
}
