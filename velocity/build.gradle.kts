plugins {
    kotlin("jvm")
    kotlin("kapt")
}

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    compileOnly(kotlin("stdlib"))
    compileOnly(libs.velocity)
    kapt(libs.velocity)
    implementation(projects.clientcatcherCommon)
    compileOnly(libs.caffeine)
    implementation(libs.configurate)
}