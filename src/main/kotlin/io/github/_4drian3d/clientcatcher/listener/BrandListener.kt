package io.github._4drian3d.clientcatcher.listener

import com.velocitypowered.api.event.EventTask
import com.velocitypowered.api.event.player.PlayerClientBrandEvent
import io.github._4drian3d.clientcatcher.ClientCatcher
import io.github._4drian3d.clientcatcher.doWithFilteredAudience
import io.github._4drian3d.clientcatcher.event.BlockedClientEvent
import io.github._4drian3d.clientcatcher.objects.CatcherCommandSource
import io.github._4drian3d.clientcatcher.sendMini
import io.github._4drian3d.clientcatcher.sendWebHook
import io.github._4drian3d.clientcatcher.webhook.Replacer

class BrandListener(private val plugin: ClientCatcher) : Listener<PlayerClientBrandEvent> {
    override fun executeAsync(event: PlayerClientBrandEvent): EventTask {
        return EventTask.async {
            if (event.player.hasPermission("clientcatcher.bypass.brand")) {
                return@async
            }
            val resolver = Replacer.Client(event.brand, event.player.username)

            sendWebHook(plugin, resolver, plugin.configuration.webHook.client)

            plugin.proxyServer.doWithFilteredAudience("clientcatcher.alert.client") {
                it.sendMini(plugin.messages.alert.client, plugin.componentLogger, resolver)
            }

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

                    return@async
                }
            }
        }
    }

    override fun register() {
        plugin.eventManager.register(plugin, PlayerClientBrandEvent::class.java, this)
    }
}