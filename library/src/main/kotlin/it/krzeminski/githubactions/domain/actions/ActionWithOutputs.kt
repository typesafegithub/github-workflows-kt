package it.krzeminski.githubactions.domain.actions

public abstract class ActionWithOutputs<T>(
    override val actionOwner: String,
    override val actionName: String,
    override val actionVersion: String,
) : Action(
    actionOwner = actionOwner,
    actionName = actionName,
    actionVersion = actionVersion,
) {
    public abstract fun buildOutputObject(stepId: String): T
}
