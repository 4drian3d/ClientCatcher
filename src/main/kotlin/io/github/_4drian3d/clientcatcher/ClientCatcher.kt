package io.github._4drian3d.clientcatcher

import com.google.inject.Inject
import com.velocitypowered.api.command.CommandManager
import com.velocitypowered.api.event.EventManager
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.player.PlayerClientBrandEvent
import com.velocitypowered.api.event.player.PlayerModInfoEvent
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.plugin.Dependency
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.PluginManager
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer
import io.github._4drian3d.clientcatcher.command.ClientCatcherCommand
import io.github._4drian3d.clientcatcher.configuration.Configuration
import io.github._4drian3d.clientcatcher.configuration.Messages
import io.github._4drian3d.clientcatcher.configuration.load
import io.github._4drian3d.clientcatcher.listener.BrandListener
import io.github._4drian3d.clientcatcher.listener.ModListener
import org.bstats.velocity.Metrics
import org.slf4j.Logger
import java.nio.file.Path
import java.util.concurrent.CompletableFuture

@Plugin(
    id = "clientcatcher",
    name = "ClientCatcher",
    version = Constants.VERSION,
    authors = ["4drian3d"],
    dependencies = [
        Dependency(id = "mckotlin-velocity"),
        Dependency(id = "miniplaceholders", optional = true)
    ]
)
class ClientCatcher @Inject constructor(
    @DataDirectory private val path: Path,
    private val pluginManager: PluginManager,
    private val logger: Logger,
    private val metrics: Metrics.Factory,
    val proxyServer: ProxyServer,
    val commandManager: CommandManager,
    val eventManager: EventManager,
) {
    lateinit var configuration: Configuration
        private set
    lateinit var messages: Messages
        private set

    @Subscribe
    fun onProxyInitialization(event: ProxyInitializeEvent) {
        loadDependencies(this, logger, pluginManager, path)

        loadConfig().thenAcceptAsync {
            if (it) {
                ClientCatcherCommand(commandManager, this).register()
                BrandListener(this).register()
                ModListener(this).register()
                logger.info("Correctly loaded ClientCatcher")
                if (pluginManager.isLoaded("miniplaceholders")) {
                    registerExpansion(proxyServer)
                }
                metrics.make(this, 17830)
            }
        }
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