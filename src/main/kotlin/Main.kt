import it.krzeminski.githubactions.domain.RunnerType.UbuntuLatest
import it.krzeminski.githubactions.domain.Trigger.Push
import it.krzeminski.githubactions.dsl.workflow
import it.krzeminski.githubactions.yaml.toYaml

fun main() {
    val workflow = workflow(
        name = "Test workflow",
        on = listOf(Push),
    ) {
        job(
            name = "test_job",
            runsOn = UbuntuLatest,
        ) {
            run(
                name = "Hello world!",
                run = "echo 'hello!'")
        }
    }

    val yaml = workflow.toYaml()
    println(yaml)
}