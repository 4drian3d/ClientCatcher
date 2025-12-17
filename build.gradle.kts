plugins {
    kotlin("jvm") version "2.3.0"
    kotlin("kapt") version "2.3.0"
    alias(libs.plugins.runvelocity)
    alias(libs.plugins.idea.ext)
    alias(libs.plugins.blossom)
    alias(libs.plugins.shadow)
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly(kotlin("stdlib"))
    compileOnly(libs.velocity)
    kapt(libs.velocity)
    compileOnly(libs.miniplaceholders.api)
    compileOnly(libs.miniplaceholders.kotlin)

    implementation(libs.configurate.kotlin) {
        isTransitive = false
    }
    implementation(libs.bstats)
    implementation(libs.jdwebhooks) {
        isTransitive = false
    }
}

tasks {
    build {
        dependsOn(shadowJar)
    }
    clean {
        delete("run")
    }
    runVelocity {
        velocityVersion(libs.versions.velocity.get())
        downloadPlugins {
            modrinth("mckotlin", "E0RrjAEB")
            modrinth("miniplaceholders", "fW9GvvXS")
        }
    }
    shadowJar {
        archiveBaseName.set(rootProject.name)
        archiveClassifier.set("")
        listOf(
            "org.spongepowered.configurate.kotlin",
            "org.bstats",
            "io.github._4drian3d.jdwebhooks"
        ).forEach {
            relocate(it, "io.github._4drian3d.clientcatcher.libs.$it")
        }
    }
    compileKotlin {
        compilerOptions {
            apiVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_2)
            languageVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_2)
        }
    }
}


sourceSets {
    main {
        blossom {
            kotlinSources {
                property("version", project.version.toString())
            }
        }
    }
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

