import it.krzeminski.githubactions.domain.RunnerType.UbuntuLatest
import it.krzeminski.githubactions.domain.Trigger.WorkflowDispatch
import it.krzeminski.githubactions.dsl.workflow
import it.krzeminski.githubactions.yaml.toYaml

fun main() {
    val workflow = workflow(
        name = "Generate reports",
        on = listOf(WorkflowDispatch),
    ) {
        val generateReportsJob = job(
            name = "generate_reports",
            runsOn = UbuntuLatest,
        ) {
            run(
                name = "Hello world!",
                run = "echo 'hello!'")
        }

        job(
            name = "update_reports",
            runsOn = UbuntuLatest,
            needs = listOf(generateReportsJob),
        ) {
            run(
                name = "Hello world 2!",
                run = "echo 'hello here as well!'")
        }
    }

    val yaml = workflow.toYaml()
    println(yaml)
}