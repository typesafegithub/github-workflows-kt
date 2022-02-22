package it.krzeminski.githubactions.actions

abstract class ActionWithOutputs<T>(
    override val actionOwner: String,
    override val actionName: String,
    override val actionVersion: String,
) : Action(
    actionOwner = actionOwner,
    actionName = actionName,
    actionVersion = actionVersion,
) {
    abstract fun buildOutputObject(stepId: String): T
}
