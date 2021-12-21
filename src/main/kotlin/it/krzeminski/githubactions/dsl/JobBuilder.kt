package it.krzeminski.githubactions.dsl

import it.krzeminski.githubactions.actions.Action
import it.krzeminski.githubactions.domain.CommandStep
import it.krzeminski.githubactions.domain.ExternalActionStep
import it.krzeminski.githubactions.domain.Job
import it.krzeminski.githubactions.domain.RunnerType

class JobBuilder(
    val name: String,
    val runsOn: RunnerType,
    val needs: List<Job>,
    val strategyMatrix: Map<String, List<String>>?,
) {
    private var job = Job(
        name = name,
        runsOn = runsOn,
        needs = needs,
        steps = emptyList(),
        strategyMatrix = strategyMatrix,
    )

    fun run(
        name: String,
        command: String,
    ): CommandStep {
        val newStep = CommandStep(
            name = name,
            command = command,
        )
        job = job.copy(steps = job.steps + newStep)
        return newStep
    }

    fun uses(
        action: Action,
    ): ExternalActionStep {
        val newStep = ExternalActionStep(
            name = name,
            action = action,
        )
        job = job.copy(steps = job.steps + newStep)
        return newStep
    }

    fun build() = job
}