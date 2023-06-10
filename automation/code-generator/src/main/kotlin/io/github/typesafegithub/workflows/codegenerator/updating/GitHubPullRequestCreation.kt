package io.github.typesafegithub.workflows.codegenerator.updating

import io.github.typesafegithub.workflows.codegenerator.versions.httpClient
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.accept
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.contentType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant

/**
 * Create a PR with the desired changes.
 *
 * @return Pull request ID.
 */
suspend fun createPullRequest(
    branchName: String,
    prTitle: String,
    prBody: String,
    fileNamesToContents: Map<String, String>,
    githubToken: String,
    githubRepoOwner: String,
    githubRepoName: String,
    baseBranch: String = "main",
): Int {
    println(branchName)
    println(fileNamesToContents)

    val prCreationContext = PrCreationContext(
        httpClient = httpClient,
        githubRepoOwner = githubRepoOwner,
        githubRepoName = githubRepoName,
        githubToken = githubToken,
    )
    val getHeadResponse = prCreationContext.getHead(baseBranch = baseBranch)
    println("Commit SHA: ${getHeadResponse.`object`.sha}")
    val getCommitResponse = prCreationContext.getCommit(sha = getHeadResponse.`object`.sha)
    println("Tree SHA: ${getCommitResponse.tree.sha}")

    val fileNamesToBlobShas = fileNamesToContents.mapValues { (_, value) ->
        val uploadBlobResponse = prCreationContext.uploadBlob(utf8Contents = value)
        uploadBlobResponse.sha
    }
    println(fileNamesToBlobShas)

    val createTreeResponse = prCreationContext.createTree(
        baseTree = getCommitResponse.tree.sha,
        fileNamesToBlobShas = fileNamesToBlobShas,
    )
    println("New tree SHA: ${createTreeResponse.sha}")

    val createCommitResponse = prCreationContext.createCommit(
        message = prTitle,
        treeSha = createTreeResponse.sha,
        parentCommitSha = getHeadResponse.`object`.sha,
        authorName = "github-actions[bot]",
        authorEmail = "41898282+github-actions[bot]@users.noreply.github.com",
        date = Instant.now(),
    )
    println("New commit SHA: ${createCommitResponse.sha}")

    prCreationContext.createRef(name = "refs/heads/$branchName", sha = createCommitResponse.sha)
    val createPullRequestResponse = prCreationContext.createPullRequest(
        title = prTitle,
        body = prBody,
        head = branchName,
        base = baseBranch,
    )
    println("PR created at: https://github.com/$githubRepoOwner/$githubRepoName/pull/${createPullRequestResponse.number}")

    return 1
}

private data class PrCreationContext(
    val httpClient: HttpClient,
    val githubRepoOwner: String,
    val githubRepoName: String,
    val githubToken: String,
)

@Serializable
private data class GetHeadResponse(
    val `object`: ObjectWithSha,
)

@Serializable
private data class ObjectWithSha(
    val sha: String,
)

private suspend fun PrCreationContext.getHead(baseBranch: String): GetHeadResponse =
    gitHubApiRequest(urlSuffix = "/git/ref/heads/$baseBranch")

@Serializable
private data class GetCommitResponse(
    val tree: ObjectWithSha,
)

private suspend fun PrCreationContext.getCommit(sha: String): GetCommitResponse =
    gitHubApiRequest(urlSuffix = "/git/commits/$sha")

@Serializable
private data class UploadBlobRequest(
    val content: String,
    val encoding: String,
)

private suspend fun PrCreationContext.uploadBlob(utf8Contents: String): ObjectWithSha =
    gitHubApiRequest(urlSuffix = "/git/blobs") {
        method = HttpMethod.Post
        contentType(ContentType(contentType = "application", contentSubtype = "vnd.github+json"))
        setBody(
            UploadBlobRequest(
                content = utf8Contents,
                encoding = "utf-8",
            ),
        )
    }

@Serializable
private data class CreateTreeRequest(
    @SerialName("base_tree")
    val baseTree: String,
    val tree: List<TreeItem>,
)

@Serializable
private data class TreeItem(
    val path: String,
    val mode: String,
    val type: String,
    val sha: String,
)

private suspend fun PrCreationContext.createTree(baseTree: String, fileNamesToBlobShas: Map<String, String>): ObjectWithSha =
    gitHubApiRequest(urlSuffix = "/git/trees") {
        method = HttpMethod.Post
        contentType(ContentType(contentType = "application", contentSubtype = "vnd.github+json"))
        setBody(
            CreateTreeRequest(
                baseTree = baseTree,
                tree = fileNamesToBlobShas.map { (key, value) ->
                    TreeItem(
                        path = key,
                        sha = value,
                        mode = "100644",
                        type = "blob",
                    )
                },
            ),
        )
    }

@Serializable
private data class CreateCommitRequest(
    val message: String,
    val parents: List<String>,
    val tree: String,
    val author: Author,
)

@Serializable
private data class Author(
    val name: String,
    val email: String,
    val date: String,
)

private suspend fun PrCreationContext.createCommit(
    message: String,
    treeSha: String,
    parentCommitSha: String,
    authorName: String,
    authorEmail: String,
    date: Instant,
): ObjectWithSha =
    gitHubApiRequest(urlSuffix = "/git/commits") {
        method = HttpMethod.Post
        contentType(ContentType(contentType = "application", contentSubtype = "vnd.github+json"))
        setBody(
            CreateCommitRequest(
                message = message,
                parents = listOf(parentCommitSha),
                tree = treeSha,
                author = Author(
                    name = authorName,
                    email = authorEmail,
                    date = date.toString(),
                ),
            ),
        )
    }

@Serializable
private data class CreateRefRequest(
    val ref: String,
    val sha: String,
)

private suspend fun PrCreationContext.createRef(name: String, sha: String): Unit =
    gitHubApiRequest(urlSuffix = "/git/refs") {
        method = HttpMethod.Post
        contentType(ContentType(contentType = "application", contentSubtype = "vnd.github+json"))
        setBody(
            CreateRefRequest(
                ref = name,
                sha = sha,
            ),
        )
    }

@Serializable
private data class CreatePullRequestRequest(
    val title: String,
    val body: String,
    val head: String,
    val base: String,
)

@Serializable
private data class CreatePullRequestResponse(
    val number: Int,
)

private suspend fun PrCreationContext.createPullRequest(
    title: String,
    body: String,
    head: String,
    base: String,
): CreatePullRequestResponse =
    gitHubApiRequest(urlSuffix = "/pulls") {
        method = HttpMethod.Post
        contentType(ContentType(contentType = "application", contentSubtype = "vnd.github+json"))
        setBody(
            CreatePullRequestRequest(
                title = title,
                body = body,
                head = head,
                base = base,
            ),
        )
    }

private suspend inline fun <reified T> PrCreationContext.gitHubApiRequest(
    urlSuffix: String,
    block: HttpRequestBuilder.() -> Unit = {},
): T =
    httpClient.request(urlString = "https://api.github.com/repos/$githubRepoOwner/$githubRepoName$urlSuffix") {
        bearerAuth(githubToken)
        accept(ContentType(contentType = "application", contentSubtype = "vnd.github+json"))
        header(key = "X-GitHub-Api-Version", value = "2022-11-28")
        block()
    }.body()
