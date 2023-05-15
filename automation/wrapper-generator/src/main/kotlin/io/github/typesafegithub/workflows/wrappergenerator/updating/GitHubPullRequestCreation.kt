package io.github.typesafegithub.workflows.wrappergenerator.updating

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.time.Instant

/**
 * Create a PR with the desired changes.
 *
 * @return Pull request ID.
 */
suspend fun createPullRequest(
    branchName: String,
    fileNamesToContents: Map<String, String>,
    githubToken: String,
    githubRepoOwner: String,
    githubRepoName: String,
    baseBranch: String = "main",
): Int {
    println(branchName)
    println(fileNamesToContents)
    val client = HttpClient {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                },
            )
        }
    }

    val prCreationContext = PrCreationContext(
        httpClient = client,
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
        message = "Test commit! ${Instant.now()}",
        treeSha = createTreeResponse.sha,
        parentCommitSha = getHeadResponse.`object`.sha,
        authorName = "Piotrek Krzemiski",
        authorEmail = "wohoo@krzeminski.it",
        date = Instant.now(),
    )
    println("New commit SHA: ${createCommitResponse.sha}")

    return 1
}

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

private data class PrCreationContext(
    val httpClient: HttpClient,
    val githubRepoOwner: String,
    val githubRepoName: String,
    val githubToken: String,
)

suspend fun main() {
    createPullRequest(
        branchName = "aloha",
        fileNamesToContents = mapOf(
            "foo-bar.txt" to "Test123",
            "test/path.txt" to "Foo\nbar\nbaz",
        ),
        githubToken = "redacted",
        githubRepoOwner = "typesafegithub",
        githubRepoName = "github-workflows-kt",
    )
}
