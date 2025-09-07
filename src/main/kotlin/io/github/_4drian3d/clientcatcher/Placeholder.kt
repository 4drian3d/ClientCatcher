package io.github._4drian3d.clientcatcher

import com.velocitypowered.api.proxy.Player
import com.velocitypowered.api.proxy.ProxyServer
import io.github.miniplaceholders.kotlin.asClosingTag
import io.github.miniplaceholders.kotlin.audience
import io.github.miniplaceholders.kotlin.expansion
import io.github.miniplaceholders.kotlin.global
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue
import kotlin.jvm.optionals.getOrNull

fun registerExpansion(proxyServer: ProxyServer) {
    expansion("clientcatcher") {
        audience<Player>("client") { player, _, _ -> text(player.clientBrand ?: "").asClosingTag() }
        audience<Player>("mods") { player, _, _ ->
            val mods = player.modInfo.getOrNull()?.mods
            text(mods?.joinToString(", ") { "${it.id}:${it.version}" } ?: "").asClosingTag()
        }
        global("player_client") { queue, ctx ->
            queue.player(proxyServer)?.let { player ->
                val mods = player.modInfo.getOrNull()?.mods
                text(mods?.joinToString(", ") { "${it.id}:${it.version}" } ?: "").asClosingTag()
            } ?: throw ctx.newException("Invalid or Offline player provided")
        }
        global("player_mods") { queue, ctx ->
            queue.player(proxyServer)?.let {
                text(it.clientBrand ?: "").asClosingTag()
            } ?: throw ctx.newException("Invalid or offline player provided")
        }
    }.register()
}

private fun ArgumentQueue.player(proxyServer: ProxyServer): Player? =
    proxyServer.getPlayer(popOr("You need to provide a Player Name").value()).getOrNull()

