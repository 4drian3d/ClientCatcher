package me.adrianed.clientcatcher.listener

import com.velocitypowered.api.event.Continuation
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.player.PlayerClientBrandEvent
import me.adrianed.clientcatcher.ClientCatcher
import me.adrianed.clientcatcher.asMiniMessage
import me.adrianed.clientcatcher.event.BlockedClientEvent
import me.adrianed.clientcatcher.objects.CatcherCommandSource
import net.kyori.adventure.permission.PermissionChecker
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver

class BrandListener(private val plugin: ClientCatcher) {
    @Subscribe
    fun onBrand(event: PlayerClientBrandEvent, continuation: Continuation) {
        val resolver = with(TagResolver.builder()) {
            resolver(Placeholder.unparsed("player", event.player.username))
            resolver(Placeholder.unparsed("client", event.brand))
        }.build()

        plugin.proxyServer
            .filterAudience {
                it.get(PermissionChecker.POINTER).map { pointer ->
                    pointer.test("clientcatcher.alert.client")
                }.orElse(false)
            }.sendMessage(plugin.messages.alert.client.asMiniMessage(resolver))

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