package me.adrianed.clientcatcher.velocity

import com.google.inject.Inject
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer

import me.adrianed.clientcatcher.common.*
import me.adrianed.clientcatcher.velocity.configuration.Configuration
import me.adrianed.clientcatcher.velocity.configuration.Messages
import me.adrianed.clientcatcher.velocity.configuration.load
import org.slf4j.Logger
import java.nio.file.Path
import java.util.concurrent.CompletableFuture

@Plugin(
    id = "clientcatcher",
    name = "ClientCatcher",
    version = Constants.VERSION,
    authors = ["4drian3d"]
)
class ClientCatcher @Inject constructor(
    val proxyServer: ProxyServer,
    @DataDirectory val path: Path,
    val logger: Logger
) {
    lateinit var configuration: Configuration
    lateinit var messages: Messages

    @Subscribe
    fun onProxyInitialization() {
        if (!loadConfig().join()) {
            return
        }
    }

    fun loadConfig() = CompletableFuture.supplyAsync {
        configuration = load(path)
        messages = load(path)
        true
    }.exceptionally {
        logger.error("Cannot load configuration", it)
        false
    }
}