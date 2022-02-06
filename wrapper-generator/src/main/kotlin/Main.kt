fun main() {
    val wrapper = ActionCoords("actions", "checkout", "v2").generateWrapper { fetchManifest() }
    println(wrapper)
}
