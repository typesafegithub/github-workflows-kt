import it.krzeminski.githubactions.actions.Checkout
import it.krzeminski.githubactions.actions.FetchDepth.Infinite
import it.krzeminski.githubactions.domain.RunnerType.UbuntuLatest
import it.krzeminski.githubactions.domain.Trigger.Push
import it.krzeminski.githubactions.dsl.workflow
import it.krzeminski.githubactions.yaml.toYaml

fun main() {
    val workflow = workflow(
        name = "Test workflow",
        on = listOf(Push),
    ) {
        val testJob = job(name = "test_job", runsOn = UbuntuLatest) {
            uses(Checkout(fetchDepth = Infinite))

            run(name = "Install Kotlin for scripting",
                command = "sudo snap install --classic kotlin")
        }

        job(name = "test_job2", runsOn = UbuntuLatest, needs = listOf(testJob)) {
            run(name = "Some random echo",
                command = "echo 'hello!'")
        }
    }

    val yaml = workflow.toYaml()
    println(yaml)
}