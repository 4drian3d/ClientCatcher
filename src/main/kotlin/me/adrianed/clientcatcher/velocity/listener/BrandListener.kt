package me.adrianed.clientcatcher.velocity.listener

import com.velocitypowered.api.event.Continuation
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.player.PlayerClientBrandEvent
import me.adrianed.clientcatcher.velocity.ClientCatcher
import me.adrianed.clientcatcher.velocity.asMiniMessage
import me.adrianed.clientcatcher.velocity.event.BlockedClientEvent
import me.adrianed.clientcatcher.velocity.objects.CatcherCommandSource
import net.kyori.adventure.permission.PermissionChecker

class BrandListener(private val plugin: ClientCatcher) {
    @Subscribe
    fun onBrand(event: PlayerClientBrandEvent, continuation: Continuation) {
        plugin.proxyServer
            .filterAudience {
                it.get(PermissionChecker.POINTER).map {
                        pointer -> pointer.test("clientcatcher.alert.client")
                }.orElse(false)
            }.sendMessage(plugin.messages.alert.client.asMiniMessage())

        for (client in plugin.configuration.blocked.clients) {
            if (event.brand == client.key) {
                for (command in client.value) {
                    plugin.commandManager.executeAsync(CatcherCommandSource, command)
                    plugin.eventManager.fireAndForget(BlockedClientEvent(command, event.player))
                    continuation.resume()
                    return
                }
            }
        }
        continuation.resume()
    }
}