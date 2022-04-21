package it.krzeminski.githubactions.expr

/**
 * Type-safe expressions, i.e. something evaluated by GitHub.
 *
 * https://docs.github.com/en/actions/learn-github-actions/expressions#about-expressions
 * https://docs.github.com/en/actions/learn-github-actions/contexts
 */
object Expr : FunctionsContext() {

    val job: JobContext = JobContext

    val runner: RunnerContext = RunnerContext

    val github: GitHubContext = GitHubContext

    val strategy: StrategyContext = StrategyContext

    val matrix: MatrixContext = MatrixContext

    val env: Env = Env

    val secrets: Secrets = Secrets
}
