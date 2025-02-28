plugins {
    kotlin("jvm") version "2.0.21"
    kotlin("kapt") version "2.0.21"
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
            url("https://cdn.modrinth.com/data/1iWA0pjH/versions/3PY3lTnr/MCKotlinVelocity-1.5.1-k2.0.21.jar")
            url("https://cdn.modrinth.com/data/HQyibRsN/versions/pxgKwgNJ/MiniPlaceholders-Velocity-2.2.4.jar")
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
            apiVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_0)
            languageVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_0)
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
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

