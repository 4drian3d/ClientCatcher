plugins {
    kotlin("jvm") version "1.8.10"
    kotlin("kapt") version "1.8.10"
    alias(libs.plugins.runvelocity)
    alias(libs.plugins.blossom)
    alias(libs.plugins.shadow)
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/") {
        content {
            includeGroup("com.velocitypowered")
        }
    }
    maven("https://jitpack.io") {
        content {
            includeGroup("com.github.AlessioDP.libby")
        }
    }
    mavenCentral()
}

dependencies {
    compileOnly(kotlin("stdlib"))
    compileOnly(libs.velocity)
    kapt(libs.velocity)
    compileOnly(libs.caffeine)
    compileOnly(libs.configurate.hocon)
    compileOnly(libs.configurate.kotlin)
    implementation(libs.libby)
    implementation(libs.bstats)
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
    }
    shadowJar {
        listOf(
            "org.spongepowered",
            "net.byteflux",
            "io.leangen.geantyref",
            "org.bstats"
        ).forEach {
            relocate(it, "me.adrianed.clientcatcher.libs.$it")
        }
    }
}

blossom {
    replaceTokenIn("src/main/kotlin/me/adrianed/clientcatcher/Constants.kt")
    replaceToken("{version}", project.version)
    replaceToken("{configurate}", libs.versions.configurate.get())
    replaceToken("{geantyref}", libs.versions.geantyref.get())
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

