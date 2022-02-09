package gradle

import ActionCoords
import generateKotlinPoet
import java.net.URL

/***
 * Run with
 *  ./gradlew :wrapper-generator:run --args https://github.com/peterjgrainger/action-create-branch/releases/tag/v2.1.0
 */
fun main(args: Array<String>) {
    val url = try {
        URL(args.firstOrNull())
    } catch (e: Exception) {
        usage()
    }
    val paths = url.path.split("/")
    when {
        url.host != "github.com" -> usage()
        paths.size < 6 -> usage()
        paths[3] != "releases" || paths[4] != "tag" -> usage()
    }
    val action = ActionCoords(paths[1], paths[2], paths[5])
    action.generateKotlinPoet()
}

fun usage(): Nothing {
    error("""
        |Usage: 
        |   ./gradlew :wrapper-generator:run --args https://github.com/peterjgrainger/action-create-branch/releases/tag/v2.1.0
    """.trimMargin())
}