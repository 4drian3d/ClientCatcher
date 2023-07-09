package io.github._4drian3d.clientcatcher

import io.github.miniplaceholders.api.MiniPlaceholders
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver

val hasMiniPlaceholders by lazy {
    try {
        Class.forName("io.github.miniplaceholders.api.MiniPlaceholders")
        true
    } catch (e: ClassNotFoundException) {
        false
    }
}

private fun String.asMiniMessage(resolvers: Array<out TagResolver>) = MiniMessage.miniMessage().deserialize(this, *resolvers)
private fun String.asMiniMessage(resolver: TagResolver) = MiniMessage.miniMessage().deserialize(this, resolver)

fun Audience.sendMini(message: String, resolver: TagResolver) {
    if (message.isNotBlank()) {
        val builder = TagResolver.builder().resolver(resolver)
        if (hasMiniPlaceholders) {
            builder.resolver(MiniPlaceholders.getAudienceGlobalPlaceholders(this))
        }
        this.sendMessage(message.asMiniMessage(resolver))
    }
}

fun Audience.sendMini(message: String, vararg resolver: TagResolver) {
    if (message.isNotBlank()) {
        val builder = TagResolver.builder().resolvers(*resolver)
        if (hasMiniPlaceholders) {
            builder.resolver(MiniPlaceholders.getAudienceGlobalPlaceholders(this))
        }
        this.sendMessage(message.asMiniMessage(resolver))
    }
}

fun Audience.sendMini(message: String) {
    if (message.isNotBlank()) {
        val resolver =
            if (hasMiniPlaceholders) MiniPlaceholders.getAudienceGlobalPlaceholders(this)
            else TagResolver.empty()
        this.sendMessage(message.asMiniMessage(resolver))
    }
}
