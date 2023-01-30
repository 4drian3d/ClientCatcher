package me.adrianed.clientcatcher.listener

import com.velocitypowered.api.event.Continuation
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.player.PlayerModInfoEvent
import me.adrianed.clientcatcher.ClientCatcher
import me.adrianed.clientcatcher.asMiniMessage
import me.adrianed.clientcatcher.event.BlockedModEvent
import me.adrianed.clientcatcher.objects.CatcherCommandSource
import net.kyori.adventure.permission.PermissionChecker
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver

class ModListener(private val plugin: ClientCatcher) {
    @Subscribe
    fun onBrand(event: PlayerModInfoEvent, continuation: Continuation) {
        val resolver = with(TagResolver.builder()) {
            resolver(Placeholder.unparsed("player", event.player.username))
            resolver(
                Placeholder.unparsed("mods",
                    event.modInfo.mods.joinToString(", ") { "${it.id}:${it.version}" })
            )
        }.build()


        plugin.proxyServer
            .filterAudience {
                it.get(PermissionChecker.POINTER).map { pointer ->
                    pointer.test("clientcatcher.alert.mods")
                }.orElse(false)
            }.sendMessage(plugin.messages.alert.mods.asMiniMessage(resolver))

        for (mod in event.modInfo.mods) {
            for (blocked in plugin.configuration.blocked.mods) {
                if (blocked.name.equals(mod.id, ignoreCase = true)) {
                    for (command in blocked.commands) {
                        plugin.commandManager.executeAsync(CatcherCommandSource, command
                            .replace("<player>", event.player.username)
                            .replace("<mod>", mod.id))
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