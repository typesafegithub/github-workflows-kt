import it.krzeminski.githubactions.dsl.workflow

fun main() {
    val workflow = workflow(name = "Generate reports") {
        job(name = "generate_reports")
        job(name = "update_reports")
    }
    println(workflow)
}