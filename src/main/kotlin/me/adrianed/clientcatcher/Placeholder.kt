package me.adrianed.clientcatcher

import com.velocitypowered.api.proxy.Player
import me.dreamerzero.miniplaceholders.api.Expansion
import me.dreamerzero.miniplaceholders.api.utils.TagsUtils
import kotlin.jvm.optionals.getOrNull

fun registerExpansion() {
    //TODO: Migrate to MiniPlaceholder ext-kotlin 2.0.0
    Expansion.builder("clientcatcher")
        .filter(Player::class.java)
        .audiencePlaceholder("client") { aud, _, _ ->
            TagsUtils.staticTag((aud as Player).clientBrand ?: "")
        }
        .audiencePlaceholder("mods") { aud, _, _ ->
            val mods = (aud as Player).modInfo.getOrNull()?.mods
            TagsUtils.staticTag(mods?.joinToString(", ") { "${it.id}:${it.version}" } ?: "")
        }.build().register()
}