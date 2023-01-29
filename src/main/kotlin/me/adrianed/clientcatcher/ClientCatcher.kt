package me.adrianed.clientcatcher

import com.google.inject.Inject
import com.velocitypowered.api.command.CommandManager
import com.velocitypowered.api.event.EventManager
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.plugin.Dependency
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.PluginManager
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer

import me.adrianed.clientcatcher.command.register
import me.adrianed.clientcatcher.configuration.Configuration
import me.adrianed.clientcatcher.configuration.Messages
import me.adrianed.clientcatcher.configuration.load
import me.adrianed.clientcatcher.listener.BrandListener
import me.adrianed.clientcatcher.listener.ModListener
import org.slf4j.Logger
import java.nio.file.Path
import java.util.concurrent.CompletableFuture

@Plugin(
    id = "clientcatcher",
    name = "ClientCatcher",
    version = Constants.VERSION,
    authors = ["4drian3d"],
    dependencies = [Dependency(id = "mckotlin-velocity")]
)
class ClientCatcher @Inject constructor(
    val proxyServer: ProxyServer,
    @DataDirectory private val path: Path,
    private val logger: Logger,
    val commandManager: CommandManager,
    val eventManager: EventManager,
    private val pluginManager: PluginManager
) {
    lateinit var configuration: Configuration
        private set
    lateinit var messages: Messages
        private set

    @Subscribe
    fun onProxyInitialization(event: ProxyInitializeEvent) {
        loadDependencies(this, logger, pluginManager, path)

        if (!loadConfig().join()) {
            return
        }

        register(commandManager, this)
        eventManager.register(this, BrandListener(this))
        eventManager.register(this, ModListener(this))
    }

    fun loadConfig() = CompletableFuture.supplyAsync {
        configuration = load(path)
        messages = load(path)
        true
    }.exceptionally {
        logger.error("Cannot load configuration", it)
        false
    }!!
}