package me.adrianed.clientcatcher

import com.velocitypowered.api.proxy.Player
import io.github.miniplaceholders.kotlin.asClosingTag
import io.github.miniplaceholders.kotlin.expansion
import net.kyori.adventure.text.Component.text
import kotlin.jvm.optionals.getOrNull

fun registerExpansion() {
    expansion("clientcatcher") {
        filter(Player::class.java)
        audiencePlaceholder("client") { aud, _, _ -> text((aud as Player).clientBrand ?: "").asClosingTag() }
        audiencePlaceholder("mods") { aud, _, _ ->
            val mods = (aud as Player).modInfo.getOrNull()?.mods
            text(mods?.joinToString(", ") { "${it.id}:${it.version}" } ?: "").asClosingTag()
        }
    }.register()
}