rootProject.name = "clientcatcher-parent"

arrayOf("velocity", "paper", "common").forEach {
    include("clientcatcher-$it")
    project(":clientcatcher-$it").projectDir = file(it)
}
