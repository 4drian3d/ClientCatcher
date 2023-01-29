package me.adrianed.clientcatcher

import com.velocitypowered.api.plugin.PluginManager
import net.byteflux.libby.Library
import org.slf4j.Logger
import java.nio.file.Path
import net.byteflux.libby.VelocityLibraryManager
import net.byteflux.libby.relocation.Relocation

fun loadDependencies(plugin: ClientCatcher, logger: Logger, manager: PluginManager, path: Path) {
    val libraryManager = VelocityLibraryManager(logger, path, manager, plugin, "libs")
    val configurateRelocation = Relocation(
        "org{}spongepowered",
        "me.adrianed.clientcatcher.libs.org{}spongepowered"
    )
    val geantyrefRelocation = Relocation(
        "io{}leangen{}geantyref",
        "me.adrianed.clientcatcher.libs.io{}leangen{}geantyref"
    )
    val hocon = Library.builder()
        .groupId("org{}spongepowered")
        .artifactId("configurate-hocon")
        .version(Constants.CONFIGURATE)
        .id("configurate-hocon")
        .relocate(configurateRelocation)
        .relocate(geantyrefRelocation)
        .build()
    val confCore = Library.builder()
        .groupId("org{}spongepowered")
        .artifactId("configurate-core")
        .version(Constants.CONFIGURATE)
        .id("configurate-core")
        .relocate(configurateRelocation)
        .relocate(geantyrefRelocation)
        .build()
    val geantyref = Library.builder()
        .groupId("io{}leangen{}geantyref")
        .artifactId("geantyref")
        .version(Constants.GEANTYREF)
        .id("geantyref")
        .relocate(geantyrefRelocation)
        .build()
    libraryManager.addMavenCentral()
    libraryManager.loadLibrary(geantyref)
    libraryManager.loadLibrary(confCore)
    libraryManager.loadLibrary(hocon)
}