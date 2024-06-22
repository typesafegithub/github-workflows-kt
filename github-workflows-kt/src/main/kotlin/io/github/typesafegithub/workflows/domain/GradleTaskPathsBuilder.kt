package io.github.typesafegithub.workflows.domain

public data class GradleTask(val path: String)

public sealed interface TaskTreeNode

public data class TaskTreeLeaf(val name: String) : TaskTreeNode

public data class TaskTree(val tree: Map<String, TaskTreeNode>) : TaskTreeNode

public data class GradleTasks(val taskTree: TaskTree =
    TaskTree(
        tree = mapOf(
            "foo" to TaskTreeLeaf("bar"),
            "baz" to TaskTree(
                tree = mapOf(
                    "goo" to TaskTreeLeaf("zoo"),
                ),
            ),
        ),
    ), val tasks: List<GradleTask>) {
    public operator fun get(name: String): GradleTasks {
//        if (name !in taskTree.tree) {
//            error("'$name' not in ${taskTree.tree.keys}" )
//        }

        return if (this.tasks.isEmpty()) {
            GradleTasks(tasks = listOf(GradleTask(name)))
        } else {
            GradleTasks(
                tasks = tasks.dropLast(1) + GradleTask(
                    path = "${tasks.last().path}:$name"))
        }
    }

    public operator fun plus(other: GradleTasks): GradleTasks {
        return GradleTasks(tasks = this.tasks + other.tasks)
    }
}
