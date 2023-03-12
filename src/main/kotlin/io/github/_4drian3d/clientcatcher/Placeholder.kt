package io.github._4drian3d.clientcatcher

import com.velocitypowered.api.proxy.Player
import com.velocitypowered.api.proxy.ProxyServer
import io.github.miniplaceholders.kotlin.asClosingTag
import io.github.miniplaceholders.kotlin.expansion
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue
import kotlin.jvm.optionals.getOrNull

fun registerExpansion(proxyServer: ProxyServer) {
    expansion("clientcatcher") {
        filter(Player::class.java)
        audiencePlaceholder("client") { aud, _, _ -> text((aud as Player).clientBrand ?: "").asClosingTag() }
        audiencePlaceholder("mods") { aud, _, _ ->
            val mods = (aud as Player).modInfo.getOrNull()?.mods
            text(mods?.joinToString(", ") { "${it.id}:${it.version}" } ?: "").asClosingTag()
        }
        .globalPlaceholder("player_client") { queue, ctx ->
            queue.player(proxyServer)?.let { player ->
                val mods = player.modInfo.getOrNull()?.mods
                text(mods?.joinToString(", ") { "${it.id}:${it.version}" } ?: "").asClosingTag()
            } ?: throw ctx.newException("Offline Player provided")
        }
        .globalPlaceholder("player_mods") { queue, ctx ->
            queue.player(proxyServer)?.let {
                text(it.clientBrand ?: "").asClosingTag()
            } ?: throw ctx.newException("Offline Player provided")
        }
    }.register()
}

private fun ArgumentQueue.player(proxyServer: ProxyServer): Player? =
    proxyServer.getPlayer(popOr("You need to provide a Player Name").value()).getOrNull()

