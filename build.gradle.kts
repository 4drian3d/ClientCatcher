import com.github.jengelman.gradle.plugins.shadow.tasks.ConfigureShadowRelocation

plugins {
    java
	id("net.kyori.blossom") version "1.2.0"
    id("com.github.johnrengelman.shadow") version "7.1.0"
}

repositories {
    mavenCentral()
    maven {
        name = "Velocity Repository"
        url = uri("https://nexus.velocitypowered.com/repository/maven-public/")
    }

    maven {
        name = "Jitpack"
        url = uri("https://jitpack.io")
    }

    maven {
        url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
    }
}

dependencies {
    shadow("com.github.simplix-softworks:simplixstorage:3.2.3")
    shadow("net.kyori:adventure-text-minimessage:4.2.0-SNAPSHOT"){
        exclude("net.kyori", "adventure-api")
    }

    compileOnly("com.velocitypowered:velocity-api:3.0.2-SNAPSHOT")
    annotationProcessor("com.velocitypowered:velocity-api:3.0.2-SNAPSHOT")
}

group = "net.dreamerzero.ClientCatcher"
version = "2.2.1"
description = "A simple Velocity plugin to catch the client version"
java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

blossom{
    val constants = "src/main/java/net/dreamerzero/clientcatcher/utils/Constants.java"
    replaceToken("{name}", rootProject.name, constants)
    replaceToken("{version}", version, constants)
    replaceToken("{description}", description, constants)
    replaceToken("{url}", "https://polymart.org/resource/clientcatcher-mods-support.1388", constants)
}

tasks {
    build {
        dependsOn(shadowJar)
    }
    shadowJar {
        dependsOn(getByName("relocateShadowJar") as ConfigureShadowRelocation)
        minimize()
        archiveFileName.set("ClientCatcher.jar")
        configurations = listOf(project.configurations.shadow.get())
    }

    create<ConfigureShadowRelocation>("relocateShadowJar") {
        target = shadowJar.get()
        prefix = "net.dreamerzero.clientcatcher.libs"
    }
}

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}
