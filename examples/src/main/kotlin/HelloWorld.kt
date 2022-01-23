import it.krzeminski.githubactions.actions.CheckoutV2
import it.krzeminski.githubactions.domain.RunnerType.UbuntuLatest
import it.krzeminski.githubactions.domain.Trigger.Push
import it.krzeminski.githubactions.dsl.workflow
import it.krzeminski.githubactions.yaml.toYaml
import java.nio.file.Paths

fun main() {
    val workflow = workflow(
        name = "Test workflow",
        on = listOf(Push),
        sourceFile = Paths.get("script.main.kts"),
        targetFile = Paths.get("some_workflow.yaml"),
    ) {
        job(
            name = "test_job",
            runsOn = UbuntuLatest,
        ) {
            uses(
                name = "Check out",
                action = CheckoutV2(),
            )

            run(
                name = "Hello world!",
                command = "echo 'hello!'",
            )
        }
    }
    println(workflow.toYaml())
}
