enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("VERSION_CATALOGS")

rootProject.name = "clientcatcher-parent"

arrayOf("velocity", "paper", "common").forEach {
    include("clientcatcher-$it")
    project(":clientcatcher-$it").projectDir = file(it)
}
