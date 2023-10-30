package io.github._4drian3d.clientcatcher.webhook

import com.velocitypowered.api.util.ModInfo
import net.kyori.adventure.text.minimessage.Context
import net.kyori.adventure.text.minimessage.tag.Tag
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import java.util.*

interface Replacer : TagResolver {
    fun replace(string: String): String

    class Client(private val client: String, private val player: String) : Replacer {
        override fun resolve(name: String, arguments: ArgumentQueue, ctx: Context): Tag? {
            return when (name.lowercase(Locale.ROOT)) {
                "client" -> Tag.preProcessParsed(client)
                "player" -> Tag.preProcessParsed(player)
                else -> null
            }
        }

        override fun has(name: String): Boolean {
            return name.equals("client", true)
                    || name.equals("player", true)
        }

        override fun replace(string: String): String {
            return string.replace("client", client)
                .replace("player", player)
        }

    }

    class Mods(private val modInfo: ModInfo, private val player: String) : Replacer {
        override fun resolve(name: String, arguments: ArgumentQueue, ctx: Context): Tag? {
            return when (name.lowercase(Locale.ROOT)) {
                "mods" -> Tag.preProcessParsed(modInfo.mods.joinToString(", ") { "${it.id}:${it.version}" })
                "player" -> Tag.preProcessParsed(player)
                else -> null
            }
        }

        override fun has(name: String): Boolean {
            return name.equals("client", true)
                    || name.equals("player", true)
        }

        override fun replace(string: String): String {
            return string.replace("player", player)
                .replace("mods", modInfo.mods.joinToString(", ") { "${it.id}:${it.version}" })
        }

    }
}