package me.adrianed.clientcatcher.velocity.listener

import com.velocitypowered.api.event.Continuation
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.player.PlayerModInfoEvent
import me.adrianed.clientcatcher.velocity.ClientCatcher
import me.adrianed.clientcatcher.velocity.asMiniMessage
import me.adrianed.clientcatcher.velocity.event.BlockedModEvent
import me.adrianed.clientcatcher.velocity.objects.CatcherCommandSource
import net.kyori.adventure.permission.PermissionChecker

class ModListener(private val plugin: ClientCatcher) {
    @Subscribe
    fun onBrand(event: PlayerModInfoEvent, continuation: Continuation) {
        plugin.proxyServer
            .filterAudience {
                it.get(PermissionChecker.POINTER).map {
                        pointer -> pointer.test("clientcatcher.alert.mods")
                }.orElse(false)
            }.sendMessage(plugin.messages.alert.mods.asMiniMessage())

        for (mod in event.modInfo.mods) {
            for (blocked in plugin.configuration.blocked.mods) {
                if (blocked.key == mod.id) {
                    for (command in blocked.value) {
                        plugin.commandManager.executeAsync(CatcherCommandSource, command)
                        plugin.eventManager.fireAndForget(BlockedModEvent(mod, event.player))
                        continuation.resume()
                        return
                    }
                }
            }
        }
        continuation.resume()
    }
}