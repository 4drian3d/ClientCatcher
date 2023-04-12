package io.github._4drian3d.clientcatcher.listener

import com.velocitypowered.api.event.Continuation
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.player.PlayerClientBrandEvent
import io.github._4drian3d.clientcatcher.ClientCatcher
import io.github._4drian3d.clientcatcher.asMiniMessage
import io.github._4drian3d.clientcatcher.event.BlockedClientEvent
import io.github._4drian3d.clientcatcher.objects.CatcherCommandSource
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
            if (event.brand.equals(client.name, ignoreCase = true)) {
                plugin.eventManager.fireAndForget(BlockedClientEvent(event.brand, event.player))

                client.commands.map {
                    it.replace("<player>", event.player.username)
                        .replace("<client>", event.brand)
                }.forEach {
                    plugin.commandManager.executeAsync(
                        CatcherCommandSource, it
                    )
                }

                continuation.resume()
                return
            }
        }
        continuation.resume()
    }
}