package io.github._4drian3d.clientcatcher.listener

import com.velocitypowered.api.event.EventTask
import com.velocitypowered.api.event.player.PlayerModInfoEvent
import io.github._4drian3d.clientcatcher.ClientCatcher
import io.github._4drian3d.clientcatcher.event.BlockedModEvent
import io.github._4drian3d.clientcatcher.objects.CatcherCommandSource
import io.github._4drian3d.clientcatcher.sendMini
import io.github._4drian3d.clientcatcher.sendWebHook
import io.github._4drian3d.clientcatcher.webhook.Replacer
import net.kyori.adventure.permission.PermissionChecker

class ModListener(private val plugin: ClientCatcher) : Listener<PlayerModInfoEvent> {
    override fun executeAsync(event: PlayerModInfoEvent): EventTask {
        return EventTask.async {
            if (event.player.hasPermission("clientcatcher.bypass.mods")) {
                return@async
            }
            val resolver = Replacer.Mods(event.modInfo, event.player.username)

            sendWebHook(plugin, resolver, plugin.configuration.webHook.mods)

            plugin.proxyServer
                .filterAudience {
                    it.get(PermissionChecker.POINTER).map { pointer ->
                        pointer.test("clientcatcher.alert.mods")
                    }.orElse(false)
                }.sendMini(plugin.messages.alert.mods, resolver)

            for (mod in event.modInfo.mods) for (blocked in plugin.configuration.blocked.mods) {
                if (blocked.name.equals(mod.id, ignoreCase = true)) {
                    plugin.eventManager.fireAndForget(BlockedModEvent(mod, event.player))

                    blocked.commands.map {
                        it.replace("<player>", event.player.username)
                            .replace("<mod>", mod.id)
                    }.forEach { plugin.commandManager.executeAsync(CatcherCommandSource, it) }

                    return@async
                }
            }
        }
    }

    override fun register() {
        plugin.eventManager.register(plugin, PlayerModInfoEvent::class.java, this)
    }
}